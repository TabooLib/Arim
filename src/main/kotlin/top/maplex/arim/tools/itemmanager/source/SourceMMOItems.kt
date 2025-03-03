package top.maplex.arim.tools.itemmanager.source


import net.Indyuce.mmoitems.MMOItems
import net.Indyuce.mmoitems.api.Type
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import top.maplex.arim.tools.itemmanager.ItemSource


class SourceMMOItems : ItemSource {

    override val name: String
        get() = "mmoitems"

    override val alias: List<String>
        get() = listOf("mi", "mmo")

    override val pluginName: String
        get() = "MMOItems"

    override fun build(id: String, player: Player?): ItemStack {
        val (type, mmoID) = id.split(",", limit = 2)
        val stack = MMOItems.plugin.getItem(Type.get(type), mmoID) ?: warnItemNotFound(id)
        return stack
    }
}