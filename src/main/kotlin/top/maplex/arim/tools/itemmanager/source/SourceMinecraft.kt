package top.maplex.arim.tools.itemmanager.source

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.util.Strings
import taboolib.common5.util.startsWithAny
import taboolib.common5.util.substringAfterAny
import taboolib.library.xseries.XMaterial
import taboolib.type.BukkitEquipment
import top.maplex.arim.tools.itemmanager.ItemSource

class SourceMinecraft : ItemSource {

    override val name: String
        get() = "minecraft"

    override val alias: List<String>
        get() = listOf("mc", "eq", "equip")

    override val pluginName: String
        get() = "Minecraft"

    override val isLoaded: Boolean
        get() = true

    override fun build(id: String, player: Player?): ItemStack {
        return if (id.startsWithAny("eq", "equip")) {
            val slot = id.substringAfterAny("eq", "equip").trim()
            val equip = BukkitEquipment.fromNMS(slot)
            equip?.getItem(player)
                ?: BukkitEquipment.HAND.getItem(player)
                ?: (XMaterial.values().find { it.name.equals(id, ignoreCase = true) }
                    ?: XMaterial.values().find { it.legacy.any { legacy -> legacy.equals(id, ignoreCase = true) } }
                    ?: XMaterial.values().maxByOrNull { Strings.similarDegree(id.uppercase(), it.name) }
                        )?.parseItem()
                ?: ItemStack(Material.AIR)

        } else {
            (XMaterial.values().find { it.name.equals(id, ignoreCase = true) }
                ?: XMaterial.values().find { it.legacy.any { legacy -> legacy.equals(id, ignoreCase = true) } }
                ?: XMaterial.values().maxByOrNull { Strings.similarDegree(id.uppercase(), it.name) }
                    )?.parseItem()
                ?: ItemStack(Material.valueOf(id.uppercase()))
        }

    }
}