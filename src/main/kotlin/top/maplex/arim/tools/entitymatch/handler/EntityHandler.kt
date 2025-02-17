package top.maplex.arim.tools.entitymatch.handler

import ink.ptms.adyeshach.core.entity.type.AdyEntity
import org.bukkit.entity.LivingEntity

interface EntityHandler {
    fun check(entity: LivingEntity, value: String): Boolean
    fun check(entity: AdyEntity, value: String): Boolean

}
