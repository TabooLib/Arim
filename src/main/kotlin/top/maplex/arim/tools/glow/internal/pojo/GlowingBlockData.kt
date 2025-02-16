package top.maplex.arim.tools.glow.internal.pojo

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location

/**
 * TiatGlowAPIKT
 * @author Gei
 * @since 2025/01/29
 **/
class GlowingBlockData(
    val entityID: Int,
    val entityUUID: String,
    var color: NamedTextColor,
    val mode: BlockGlowMode,
    val location: Location?,
    val blockID: Int?,
)
