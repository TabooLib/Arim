package top.maplex.arim.tools.itemmanager.source


import io.th0rgal.oraxen.api.OraxenItems
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import top.maplex.arim.tools.itemmanager.ItemSource


class SourceOraxen : ItemSource {

    override val name: String
        get() = "oraxen"

    override val alias: List<String>
        get() = listOf("ox")

    override val pluginName: String
        get() = "Oraxen"

    override fun build(id: String, player: Player?): ItemStack {
        val stack = OraxenItems.getItemById(id)?.build() ?: warnItemNotFound(id)
        return stack
    }
}