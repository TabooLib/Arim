package top.maplex.arim.tools.entitymatch.handler

import org.bukkit.entity.LivingEntity
import top.maplex.arim.tools.entitymatch.hook.BaseEntityInstance

interface EntityHandler {
    fun check(entity: LivingEntity, value: String): Boolean
    fun check(entity: BaseEntityInstance, value: String): Boolean

}
