package top.maplex.arim.tools.glow.internal.util

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.protocol.entity.data.EntityData
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.util.Vector3d
import com.github.retrooper.packetevents.util.Vector3i
import com.github.retrooper.packetevents.wrapper.play.server.*
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.ScoreBoardTeamInfo
import io.github.retrooper.packetevents.util.SpigotReflectionUtil
import top.maplex.arim.tools.glow.internal.nms.NMS
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Player
import taboolib.common.platform.function.warning
import taboolib.common.util.t
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.MinecraftVersion.V1_19
import java.util.*

/**
 * TiatGlowAPIKT
 * @author Gei
 * @since 2025/01/28
 **/
internal object PacketUtil {

    fun Player.sendEntityMetadataPacket(entityID: Int, index: Int, type: EntityDataType<*>, dataItems: List<*>) {
        val packetPlayOutEntityMetadata = WrapperPlayServerEntityMetadata(
            entityID,
            dataItems.map { EntityData(index, type, it) }
        )

        PacketEvents.getAPI().playerManager.sendPacket(this, packetPlayOutEntityMetadata)
    }

    fun Player.sendColorBasedTeamCreatePacket(color: NamedTextColor) {
        val teamPrefix =
            if (MinecraftVersion.isLowerOrEqual(11202)) Component.text(color.toLowVersionChatColor().toString()) else null //Mojang我干你丫的

        val packetPlayOutScoreboardTeam = WrapperPlayServerTeams(
            "glow-$color",
            WrapperPlayServerTeams.TeamMode.CREATE,
            ScoreBoardTeamInfo(
                Component.text("glow-$color"),
                teamPrefix,
                null,
                WrapperPlayServerTeams.NameTagVisibility.ALWAYS,
                WrapperPlayServerTeams.CollisionRule.NEVER,
                color,
                WrapperPlayServerTeams.OptionData.NONE
            ),
            emptyList()
        )

        PacketEvents.getAPI().playerManager.sendPacket(this, packetPlayOutScoreboardTeam)
    }

    fun Player.sendColorBasedTeamDestroyPacket(color: NamedTextColor) {
        val info: ScoreBoardTeamInfo? = null
        val packetPlayOutScoreboardTeam = WrapperPlayServerTeams(
            "glow-$color",
            WrapperPlayServerTeams.TeamMode.REMOVE,
            info,
            emptyList<String>()
        )

        PacketEvents.getAPI().playerManager.sendPacket(this, packetPlayOutScoreboardTeam)
    }

    fun Player.sendColorBasedTeamEntityAddPacket(teamID: String, color: NamedTextColor) {
        val info: ScoreBoardTeamInfo? = null
        val packetPlayOutScoreboardTeam = WrapperPlayServerTeams(
            "glow-$color",
            WrapperPlayServerTeams.TeamMode.ADD_ENTITIES,
            info,
            listOf(teamID)
        )

        PacketEvents.getAPI().playerManager.sendPacket(this, packetPlayOutScoreboardTeam)
    }

    fun Player.sendColorBasedTeamEntityRemovePacket(teamID: String, color: NamedTextColor) {
        val info: ScoreBoardTeamInfo? = null
        val packetPlayOutScoreboardTeam = WrapperPlayServerTeams(
            "glow-$color",
            WrapperPlayServerTeams.TeamMode.REMOVE_ENTITIES,
            info,
            listOf(teamID)
        )

        PacketEvents.getAPI().playerManager.sendPacket(this, packetPlayOutScoreboardTeam)
    }

    fun Player.sendCreateDummyFallingBlockOn(location: Location): Pair<Int, String>? {
        //经测试在11802，11904或更高版本FallingBlock不可被设为除原版方块（如沙子等）以外的材质，故此方法不提供给高版本使用
        val entityUUID = UUID.randomUUID()
        val entityID = SpigotReflectionUtil.generateEntityId()
        val blockCombinedID = NMS.INSTANCE.getCombinedID(location) ?: return null

        val packetPlayOutSpawnEntity = WrapperPlayServerSpawnEntity(
            entityID,
            entityUUID,
            EntityTypes.FALLING_BLOCK,
            com.github.retrooper.packetevents.protocol.world.Location(
                location.x,
                location.y,
                location.z,
                location.yaw,
                location.pitch
            ),
            0f,
            blockCombinedID,
            Vector3d.zero(),
        )

        PacketEvents.getAPI().playerManager.sendPacket(this, packetPlayOutSpawnEntity)

        return Pair(entityID, entityUUID.toString())
    }

    fun Player.sendRemoveDummyFallingBlock(entityID: Int, blockLocation: Location, id: Int) {
        val packetPlayOutEntityDestroy = WrapperPlayServerDestroyEntities(entityID)
        PacketEvents.getAPI().playerManager.sendPacket(this, packetPlayOutEntityDestroy)

        //恢复方块
        val packetPlayOutBlockChange = WrapperPlayServerBlockChange(
            Vector3i(
                blockLocation.blockX,
                blockLocation.blockY,
                blockLocation.blockZ
            ),
            id
        )
        PacketEvents.getAPI().playerManager.sendPacket(this, packetPlayOutBlockChange)
    }

    fun Player.sendCreateDummyEntityShulkerOn(location: Location): Pair<Int, String> {
        val entityUUID = UUID.randomUUID()
        val entityID = SpigotReflectionUtil.generateEntityId()

        if (MinecraftVersion.isLower(V1_19)) {
            val packetPlayOutSpawnEntityLiving = WrapperPlayServerSpawnLivingEntity(
                entityID,
                entityUUID,
                EntityTypes.SHULKER,
                com.github.retrooper.packetevents.protocol.world.Location(
                    location.x,
                    location.y,
                    location.z,
                    location.yaw,
                    location.pitch
                ),
                0f,
                Vector3d.zero(),
                emptyList()
            )

            PacketEvents.getAPI().playerManager.sendPacket(this, packetPlayOutSpawnEntityLiving)
        } else {
            val packetPlayOutSpawnEntityLiving = WrapperPlayServerSpawnEntity(
                entityID,
                entityUUID,
                EntityTypes.SHULKER,
                com.github.retrooper.packetevents.protocol.world.Location(
                    location.x,
                    location.y,
                    location.z,
                    location.yaw,
                    location.pitch
                ),
                0f,
                0,
                Vector3d.zero(),
            )

            PacketEvents.getAPI().playerManager.sendPacket(this, packetPlayOutSpawnEntityLiving)
        }

        return Pair(entityID, entityUUID.toString())
    }

    fun Player.sendRemoveDummyEntityShulker(entityID: Int) {
        val packetPlayOutEntityDestroy = WrapperPlayServerDestroyEntities(entityID)
        PacketEvents.getAPI().playerManager.sendPacket(this, packetPlayOutEntityDestroy)
    }

    /**
     * 此方法用于将在低版本中将Adventure的颜色转为Bukkit的ChatColor
     * 只因Mojang在低版本写了一个颜色字段但没有用
     * 非常傻逼
     */
    private fun NamedTextColor.toLowVersionChatColor(): ChatColor {
        return when (this) {
            NamedTextColor.BLACK -> ChatColor.BLACK
            NamedTextColor.DARK_BLUE -> ChatColor.DARK_BLUE
            NamedTextColor.DARK_GREEN -> ChatColor.DARK_GREEN
            NamedTextColor.DARK_AQUA -> ChatColor.DARK_AQUA
            NamedTextColor.DARK_RED -> ChatColor.RED
            NamedTextColor.DARK_PURPLE -> ChatColor.DARK_PURPLE
            NamedTextColor.GOLD -> ChatColor.GOLD
            NamedTextColor.GRAY -> ChatColor.GRAY
            NamedTextColor.DARK_GRAY -> ChatColor.DARK_GRAY
            NamedTextColor.BLUE -> ChatColor.BLUE
            NamedTextColor.GREEN -> ChatColor.GREEN
            NamedTextColor.AQUA -> ChatColor.AQUA
            NamedTextColor.RED -> ChatColor.RED
            NamedTextColor.LIGHT_PURPLE -> ChatColor.LIGHT_PURPLE
            NamedTextColor.YELLOW -> ChatColor.YELLOW
            NamedTextColor.WHITE -> ChatColor.WHITE
            else -> {
                warning(
                    """
                        不支持的颜色: ${this.value()}
                        Unsupported color: ${this.value()}
                    """.t()
                )
                ChatColor.WHITE
            }
        }
    }
}
