package top.maplex.arim.tools.itemmanager.source


import ink.ptms.um.Mythic
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import top.maplex.arim.tools.itemmanager.ItemSource


class SourceMythic : ItemSource {

    override val name: String
        get() = "mythicmobs"

    override val alias: List<String>
        get() = listOf("mm", "mythic")

    override val pluginName: String
        get() = "MythicMobs"

    override fun build(id: String, player: Player?): ItemStack {
        return Mythic.API.getItemStack(id, player) ?: warnItemNotFound(id)
    }
}