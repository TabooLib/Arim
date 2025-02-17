package top.maplex.arim.tools.entitymatch.handler

import org.bukkit.entity.LivingEntity
import top.maplex.arim.tools.entitymatch.model.StringOperation
import top.maplex.arim.tools.entitymatch.util.ParserUtils

class TypeHandler: EntityHandler {
    override fun check(entity: LivingEntity, value: String): Boolean {
        val entityType = entity.type.name
        val condition = ParserUtils.parseStringCondition(value)
        return when (condition.operation) {
            StringOperation.EXACT -> condition.values.any { entityType.equals(it, true) }
            StringOperation.CONTAINS -> condition.values.any { entityType.contains(it, true) }
            StringOperation.REGEX -> condition.values.any {
                Regex(it, RegexOption.IGNORE_CASE).containsMatchIn(entityType)
            }
            StringOperation.STARTS_WITH -> condition.values.any { entityType.startsWith(it, true) }
            StringOperation.ENDS_WITH -> condition.values.any { entityType.endsWith(it, true) }
        }
    }
}