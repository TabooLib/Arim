package top.maplex.arim.tools.entitymatch.handler

import org.bukkit.entity.LivingEntity

interface EntityHandler {
    fun check(entity: LivingEntity, value: String): Boolean
}
