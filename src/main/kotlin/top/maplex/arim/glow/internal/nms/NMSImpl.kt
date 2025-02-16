package top.maplex.arim.glow.internal.nms

import org.bukkit.Location
import taboolib.common.platform.function.warning
import taboolib.common.util.t
import taboolib.common.util.unsafeLazy
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.module.nms.MinecraftVersion
import java.util.*

typealias NMSLegacyEntity = net.minecraft.server.v1_12_R1.Entity
typealias NMSUniversalEntity = net.minecraft.world.entity.Entity

typealias NMSLegacyDataWatcherObjectByte = net.minecraft.server.v1_12_R1.DataWatcherObject<Byte>
typealias NMSUniversalDataWatcherObjectByte = net.minecraft.network.syncher.DataWatcherObject<Byte>

typealias NMS12R1LegacyEntityFallingBlock = net.minecraft.server.v1_12_R1.EntityFallingBlock
typealias NMS16R3LegacyEntityFallingBlock = net.minecraft.server.v1_16_R3.EntityFallingBlock
typealias NMSUniversalEntityFallingBlock = net.minecraft.world.entity.item.EntityFallingBlock

typealias NMS12R1LegacyEntityShulker = net.minecraft.server.v1_12_R1.EntityShulker
typealias NMS16R3LegacyEntityShulker = net.minecraft.server.v1_16_R3.EntityShulker
typealias NMSUniversalEntityShulker = net.minecraft.world.entity.monster.EntityShulker

typealias NMS16R3LegacyEntityTypes = net.minecraft.server.v1_16_R3.EntityTypes<*>
typealias NMSUniversalEntityTypes = net.minecraft.world.entity.EntityTypes<*>

typealias NMS12R1LegacyCraftWorld = org.bukkit.craftbukkit.v1_12_R1.CraftWorld
typealias NMS16R3LegacyCraftWorld = org.bukkit.craftbukkit.v1_16_R3.CraftWorld
typealias NMSUniversalCraftWorld = org.bukkit.craftbukkit.v1_20_R3.CraftWorld

typealias NMSLegacyDataWatcher = net.minecraft.server.v1_12_R1.DataWatcher
typealias NMSUniversalDataWatcher = net.minecraft.network.syncher.DataWatcher

typealias NMSLegacyBlock = net.minecraft.server.v1_12_R1.Block
typealias NMSUniversalBlock = net.minecraft.world.level.block.Block

typealias BukkitCraftEntity = org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity

typealias Bukkit12R1LegacyCraftBlock = org.bukkit.craftbukkit.v1_12_R1.block.CraftBlock
typealias Bukkit16R3LegacyCraftBlock = org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock
typealias BukkitUniversalCraftBlock = org.bukkit.craftbukkit.v1_20_R3.block.CraftBlock

/**
 * TiatGlowAPIKT
 * @author Gei
 * @since 2025/02/15
 **/
class NMSImpl: NMS() {
    private val nmsLegacyEntityInst = NMSLegacyEntity::class.java
    private val nmsUniversalEntityInst = NMSUniversalEntity::class.java
    private val nmsLegacyBlockInst = NMSLegacyBlock::class.java
    private val nmsUniversalBlockInst = NMSUniversalBlock::class.java

    override val entitySharedFlagsID: Any? by unsafeLazy {
        return@unsafeLazy if (!MinecraftVersion.isUniversal) {
            //目前只支持1.12.2 1.16.5
            when (MinecraftVersion.versionId) {
                11202 -> nmsLegacyEntityInst.getProperty<NMSLegacyDataWatcherObjectByte>("Z", isStatic = true)
                11604 -> nmsLegacyEntityInst.getProperty<NMSLegacyDataWatcherObjectByte>("S", isStatic = true)
                else -> {
                    warning(
                        """
                            Unsupported version: ${MinecraftVersion.versionId}
                            不支持的版本: ${MinecraftVersion.versionId}
                        """.t()
                    )
                    null
                }
            }
        } else {
            nmsUniversalEntityInst.getProperty<NMSUniversalDataWatcherObjectByte>("DATA_SHARED_FLAGS_ID", isStatic = true)
        }
    }

    override fun getEntityFlags(entity: org.bukkit.entity.Entity): Byte? {
        if (entitySharedFlagsID == null) {
            warning(
                """
                    Unsupported version: ${MinecraftVersion.versionId}
                    不支持的版本: ${MinecraftVersion.versionId}
                """.t()
            )
            return null
        }

        val nmsEntity = (entity as BukkitCraftEntity).handle
        val dataWatcher: Any? =
            if (MinecraftVersion.isUniversal) nmsEntity.invokeMethod<NMSUniversalDataWatcher>("getEntityData")
            else nmsEntity.invokeMethod<NMSLegacyDataWatcher>("getDataWatcher")

        if (dataWatcher == null) {
            warning(
                """
                    Unsupported version: ${MinecraftVersion.versionId}
                    不支持的版本: ${MinecraftVersion.versionId}
                """.t()
            )
            return null
        }

        return dataWatcher.invokeMethod<Byte>("get", entitySharedFlagsID)
    }

    override fun getCombinedID(location: Location): Int? {
        if (!MinecraftVersion.isUniversal) {
            when (MinecraftVersion.versionId) {
                11202 -> {
                    return nmsLegacyBlockInst.invokeMethod<Int>(
                        "getCombinedId",
                        (location.block as Bukkit12R1LegacyCraftBlock).invokeMethod<Any>("getData0"),
                        isStatic = true
                    )
                }
                11604 -> {
                    return nmsLegacyBlockInst.invokeMethod<Int>(
                        "getCombinedId",
                        (location.block as Bukkit16R3LegacyCraftBlock).nms,
                        isStatic = true
                    )
                }
                else -> {
                    warning(
                        """
                            Unsupported version: ${MinecraftVersion.versionId}
                            不支持的版本: ${MinecraftVersion.versionId}
                        """.t()
                    )
                    return null
                }
            }
        } else return nmsUniversalBlockInst.invokeMethod<Int>(
            "getId",
            (location.block as BukkitUniversalCraftBlock).nms,
            isStatic = true
        )
    }
}