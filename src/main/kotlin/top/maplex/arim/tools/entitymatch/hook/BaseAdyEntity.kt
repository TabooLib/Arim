package top.maplex.arim.tools.entitymatch.hook

import ink.ptms.adyeshach.core.entity.type.AdyEntity
import ink.ptms.adyeshach.core.entity.type.AdyHuman

abstract class BaseAdyEntity : AdyEntity {
    /**
     * 获取单位名称，如果是玩家则获取玩家名称
     * @author Bkm016
     */
    fun getName(): String {
        return if (this is AdyHuman) getName() else getDisplayName()
    }
}