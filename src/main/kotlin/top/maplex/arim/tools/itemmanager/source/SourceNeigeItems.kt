package top.maplex.arim.tools.itemmanager.source


import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.manager.ItemManager
import top.maplex.arim.tools.itemmanager.ItemSource


class SourceNeigeItems : ItemSource {

    override val name: String
        get() = "neigeitems"

    override val alias: List<String>
        get() = listOf("ni")

    override val pluginName: String
        get() = "NeigeItems"

    override fun build(id: String, player: Player?): ItemStack {
        val stack = ItemManager.getItemStack(id, player) ?: warnItemNotFound(id)
        return stack
    }
}