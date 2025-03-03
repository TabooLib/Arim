package top.maplex.arim.tools.itemmanager.source


import net.momirealms.craftengine.bukkit.api.CraftEngineItems
import net.momirealms.craftengine.bukkit.plugin.BukkitCraftEngine
import net.momirealms.craftengine.core.util.Key
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import top.maplex.arim.tools.itemmanager.ItemSource


class SourceCraftEngine : ItemSource {

    override val name: String
        get() = "craftengine"

    override val alias: List<String>
        get() = listOf("ce")

    override val pluginName: String
        get() = "CraftEngine"

    override fun build(id: String, player: Player?): ItemStack {
        val (namespace, material) = id.split(":", limit = 2)
        val craftPlayer = BukkitCraftEngine.instance().adapt(player)
        val item = CraftEngineItems.byId(Key(namespace, material))
        return item?.buildItemStack(craftPlayer) ?: warnItemNotFound(id)
    }
}