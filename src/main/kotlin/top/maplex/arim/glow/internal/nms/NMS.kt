package top.maplex.arim.glow.internal.nms

import org.bukkit.Location
import org.bukkit.entity.Entity
import taboolib.common.util.unsafeLazy
import taboolib.module.nms.nmsProxy
import java.util.*

/**
 * TiatGlowAPIKT
 * @author Gei
 * @since 2025/02/15
 **/
abstract class NMS {
    /**
     * 获取生物的DataWatcher标志位Flags
     * @return DataWatcherItem<Byte>
     */
    abstract val entitySharedFlagsID: Any?

    /**
     * 获取NMS Entity DataWatcher的标志位Flags
     */
    abstract fun getEntityFlags(entity: Entity): Byte?

    /**
     * 获取所在位置所在方块的CombinedID
     */
    abstract fun getCombinedID(location: Location): Int?

    companion object {
        val INSTANCE by unsafeLazy {
            nmsProxy<NMS>()
        }
    }
}