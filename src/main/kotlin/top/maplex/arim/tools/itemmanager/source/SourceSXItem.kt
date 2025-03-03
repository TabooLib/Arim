package top.maplex.arim.tools.itemmanager.source


import github.saukiya.sxitem.SXItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import top.maplex.arim.tools.itemmanager.ItemSource


class SourceSXItem : ItemSource {

    override val name: String
        get() = "sxitem"

    override val alias: List<String>
        get() = listOf("sx", "sx-item", "si")

    override val pluginName: String
        get() = "SX-Item"

    override fun build(id: String, player: Player?): ItemStack {
        val sxItem = id.split(":")
        val sxID = sxItem[0]
        val sxArgs = sxItem.toList().drop(1)
        val stack = player?.let { SXItem.getItemManager().getItem(sxID, it, *sxArgs.toTypedArray()) }
            ?: warnItemNotFound(sxID)
        return stack
    }
}