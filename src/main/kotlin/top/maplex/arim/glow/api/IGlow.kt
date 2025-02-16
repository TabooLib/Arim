package top.maplex.arim.glow.api

import top.maplex.arim.glow.internal.manager.GlowManager
import top.maplex.arim.glow.internal.pojo.BlockGlowMode
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.common.util.unsafeLazy

/**
 * TiatGlowAPIKT
 * @author Gei
 * @since 2025/01/28
 **/
class IGlow {
    /**
     * 设置/取消生物发光
     * @param entity 目标
     * @param receiver 观察者
     * @param color 颜色, null为取消发光
     */
    fun setGlowing(entity: Entity, receiver: Player, color: NamedTextColor?) {
        if (color != null) INSTANCE.setEntityGlowing(entity, receiver, color)
        else INSTANCE.unsetEntityGlowing(entity, receiver)
    }

    /**
     * 设置/取消方块发光
     * 目前不支持空气方块发光
     * @param block 目标
     * @param receiver 观察者
     * @param color 颜色, null为取消发光
     */
    fun setGlowing(block: Block, receiver: Player, color: NamedTextColor?, mode: BlockGlowMode) {
        if (color != null) INSTANCE.setBlockGlowing(block, receiver, color, mode)
        else INSTANCE.unsetBlockGlowing(block, receiver)
    }

    companion object {
        val INSTANCE: GlowManager by unsafeLazy {
            GlowManager()
        }
    }
}