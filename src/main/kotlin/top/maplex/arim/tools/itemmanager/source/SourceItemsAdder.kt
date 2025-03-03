package top.maplex.arim.tools.itemmanager.source


import dev.lone.itemsadder.api.CustomStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import top.maplex.arim.tools.itemmanager.ItemSource


class SourceItemsAdder : ItemSource {

    override val name: String
        get() = "itemsadder"

    override val alias: List<String>
        get() = listOf("ia")

    override val pluginName: String
        get() = "ItemsAdder"

    override fun build(id: String, player: Player?): ItemStack {
        val stack = CustomStack.getInstance(id)?.itemStack ?: warnItemNotFound(id)
        return stack
    }
}