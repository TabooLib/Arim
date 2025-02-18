package top.maplex.arim.tools.entitymatch.handler

import org.bukkit.entity.LivingEntity
import taboolib.platform.util.hasMeta
import top.maplex.arim.tools.entitymatch.hook.BaseAdyEntity
import top.maplex.arim.tools.itemmatch.util.ParserUtils

class MetaHandler : EntityHandler {
    override fun check(entity: LivingEntity, value: String): Boolean {
        val key = condition(value) ?: return false
        return entity.hasMeta(key)
    }
    override fun check(entity: BaseAdyEntity, value: String): Boolean {
        val key = condition(value) ?: return false
        return entity.hasTag(key)
    }
    fun condition(value: String): String? {
        val condition = ParserUtils.parseStringCondition(value)
        return condition.values.firstOrNull()
    }
}