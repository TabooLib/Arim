package top.maplex.arim.tools.entitymatch.handler

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import taboolib.common.platform.function.info
import taboolib.module.chat.uncolored
import taboolib.module.nms.getI18nName
import top.maplex.arim.tools.entitymatch.model.CompoundType
import top.maplex.arim.tools.entitymatch.model.MatchCondition
import top.maplex.arim.tools.entitymatch.model.StringOperation
import top.maplex.arim.tools.entitymatch.util.ParserUtils


class NameHandler: EntityHandler {
    override fun check(entity: LivingEntity, value: String): Boolean {
        val entityName = entity.customName ?: entity.getI18nName()
        return when (val condition = ParserUtils.parseListCondition(value)) {
            is MatchCondition.StringCondition -> {
                checkStringCondition(entityName, condition)
            }
            is MatchCondition.CompoundCondition -> {
                when (condition.type) {
                    CompoundType.ANY -> condition.conditions.any { checkStringCondition(entityName, it as MatchCondition.StringCondition) }
                    CompoundType.ALL -> condition.conditions.all { checkStringCondition(entityName, it as MatchCondition.StringCondition) }
                    CompoundType.NONE -> condition.conditions.none { checkStringCondition(entityName, it as MatchCondition.StringCondition) }
                }
            }
            else -> false
        }
    }
    private fun checkStringCondition(string: String, condition: MatchCondition.StringCondition): Boolean {
        var entityName = string
        if (condition.modifiers.containsAll(listOf("uncolored", "uc"))) {
            entityName = entityName.uncolored()
        }
        if (condition.modifiers.containsAll(listOf("lowercase", "lc"))) {
            entityName = entityName.lowercase()
        }
        return when (condition.operation) {
            StringOperation.EXACT -> entityName == condition.values.first()
            StringOperation.CONTAINS -> condition.values.any { entityName.contains(it, true) }
            StringOperation.REGEX -> condition.values.any { Regex(it, RegexOption.IGNORE_CASE).containsMatchIn(entityName) }
            StringOperation.STARTS_WITH -> condition.values.any { entityName.startsWith(it, true) }
            StringOperation.ENDS_WITH -> condition.values.any { entityName.endsWith(it, true) }
        }
    }
}