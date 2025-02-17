package top.maplex.arim.tools.entitymatch.handler

import ink.ptms.adyeshach.core.entity.type.AdyEntity
import org.bukkit.entity.LivingEntity
import top.maplex.arim.tools.entitymatch.model.NumberOperator
import top.maplex.arim.tools.entitymatch.util.ParserUtils

class HealthHandler : EntityHandler {
    override fun check(entity: LivingEntity, value: String): Boolean {
        val health = entity.health
        val condition = ParserUtils.parseNumberCondition(value)
        return when (condition.operator) {
            NumberOperator.GREATER_EQUAL -> health >= condition.value
            NumberOperator.LESS_EQUAL -> health <= condition.value
            NumberOperator.GREATER -> health > condition.value
            NumberOperator.LESS -> health < condition.value
            else -> health == condition.value
        }
    }

    override fun check(entity: AdyEntity, value: String): Boolean {
        return false
    }
}