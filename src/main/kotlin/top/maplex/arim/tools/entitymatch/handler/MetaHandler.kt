package top.maplex.arim.tools.entitymatch.handler

import ink.ptms.adyeshach.core.entity.type.AdyEntity
import org.bukkit.entity.LivingEntity
import taboolib.platform.util.hasMeta
import top.maplex.arim.tools.itemmatch.util.ParserUtils

class MetaHandler : EntityHandler {
    override fun check(entity: LivingEntity, value: String): Boolean {
        val condition = ParserUtils.parseStringCondition(value)
        val key = condition.values.firstOrNull() ?: return false
        return entity.hasMeta(key)
    }
    override fun check(entity: AdyEntity, value: String): Boolean {
        val condition = ParserUtils.parseStringCondition(value)
        val key = condition.values.firstOrNull() ?: return false
        return entity.hasTag(key)
    }
}