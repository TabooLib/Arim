package top.maplex.arim.tools.entitymatch.handler

import ink.ptms.adyeshach.core.entity.EntityInstance
import ink.ptms.adyeshach.core.entity.type.AdyHuman
import org.bukkit.entity.LivingEntity
import taboolib.module.chat.uncolored
import taboolib.module.nms.getI18nName
import top.maplex.arim.tools.entitymatch.hook.BaseEntityInstance
import top.maplex.arim.tools.entitymatch.model.CompoundType
import top.maplex.arim.tools.entitymatch.model.MatchCondition
import top.maplex.arim.tools.entitymatch.model.StringOperation
import top.maplex.arim.tools.entitymatch.util.ParserUtils


class NameHandler : EntityHandler {
    override fun check(entity: LivingEntity, value: String): Boolean {
        return checkEntity(entity.customName ?: entity.getI18nName(), value)
    }

    override fun check(entity: BaseEntityInstance, value: String): Boolean {
        return checkEntity(entity.getName(), value)
    }

    private fun checkEntity(entityName: String, value: String): Boolean {
        return when (val condition = ParserUtils.parseListCondition(value)) {
            is MatchCondition.StringCondition -> checkStringCondition(entityName, condition)
            is MatchCondition.CompoundCondition -> when (condition.type) {
                CompoundType.ANY -> condition.conditions.any {
                    checkStringCondition(
                        entityName,
                        it as MatchCondition.StringCondition
                    )
                }

                CompoundType.ALL -> condition.conditions.all {
                    checkStringCondition(
                        entityName,
                        it as MatchCondition.StringCondition
                    )
                }

                CompoundType.NONE -> condition.conditions.none {
                    checkStringCondition(
                        entityName,
                        it as MatchCondition.StringCondition
                    )
                }
            }

            else -> false
        }
    }

    private fun checkStringCondition(string: String, condition: MatchCondition.StringCondition): Boolean {
        val entityName = string
            .let { if (condition.modifiers.contains("uncolored") || condition.modifiers.contains("uc")) it.uncolored() else it }
            .let { if (condition.modifiers.contains("lowercase") || condition.modifiers.contains("lc")) it.lowercase() else it }

        return when (condition.operation) {
            StringOperation.EXACT -> entityName == condition.values.first()
            StringOperation.CONTAINS -> condition.values.any { entityName.contains(it, true) }
            StringOperation.REGEX -> condition.values.any {
                Regex(it, RegexOption.IGNORE_CASE).containsMatchIn(
                    entityName
                )
            }

            StringOperation.STARTS_WITH -> condition.values.any { entityName.startsWith(it, true) }
            StringOperation.ENDS_WITH -> condition.values.any { entityName.endsWith(it, true) }
        }
    }
    fun EntityInstance.getName(): String {
        return if (this is AdyHuman) getName() else getDisplayName()
    }
}