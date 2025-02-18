package top.maplex.arim.tools.entitymatch.handler

import ink.ptms.um.Mythic
import org.bukkit.entity.LivingEntity
import top.maplex.arim.tools.entitymatch.hook.BaseEntityInstance
import top.maplex.arim.tools.entitymatch.model.CompoundType
import top.maplex.arim.tools.entitymatch.model.MatchCondition
import top.maplex.arim.tools.entitymatch.model.StringOperation
import top.maplex.arim.tools.entitymatch.util.ParserUtils

class MMHandler : EntityHandler {
    override fun check(entity: LivingEntity, value: String): Boolean {
        if (Mythic.API.getMob(entity) == null) return false
        val entityID = Mythic.API.getMob(entity)?.id ?: return false
        return checkEntity(entityID, value)
    }

    override fun check(entity: BaseEntityInstance, value: String): Boolean {
        return false
    }

    private fun checkEntity(entityID: String, value: String): Boolean {
        return when (val condition = ParserUtils.parseListCondition(value)) {
            is MatchCondition.StringCondition -> checkStringCondition(entityID, condition)
            is MatchCondition.CompoundCondition -> when (condition.type) {
                CompoundType.ANY -> condition.conditions.any {
                    checkStringCondition(
                        entityID,
                        it as MatchCondition.StringCondition
                    )
                }

                CompoundType.ALL -> condition.conditions.all {
                    checkStringCondition(
                        entityID,
                        it as MatchCondition.StringCondition
                    )
                }

                CompoundType.NONE -> condition.conditions.none {
                    checkStringCondition(
                        entityID,
                        it as MatchCondition.StringCondition
                    )
                }
            }

            else -> false
        }
    }

    private fun checkStringCondition(string: String, condition: MatchCondition.StringCondition): Boolean {
        val entityID = string
            .let { if (condition.modifiers.contains("lowercase") || condition.modifiers.contains("lc")) it.lowercase() else it }

        return when (condition.operation) {
            StringOperation.EXACT -> entityID == condition.values.first()
            StringOperation.CONTAINS -> condition.values.any { entityID.contains(it, true) }
            StringOperation.REGEX -> condition.values.any {
                Regex(
                    it,
                    RegexOption.IGNORE_CASE
                ).containsMatchIn(entityID)
            }

            StringOperation.STARTS_WITH -> condition.values.any { entityID.startsWith(it, true) }
            StringOperation.ENDS_WITH -> condition.values.any { entityID.endsWith(it, true) }
        }
    }
}