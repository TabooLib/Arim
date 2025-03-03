package top.maplex.arim.tools.itemmanager.source


import ink.ptms.zaphkiel.Zaphkiel
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import top.maplex.arim.tools.itemmanager.ItemSource


class SourceZaphkiel : ItemSource {

    override val name: String
        get() = "zaphkiel"

    override val alias: List<String>
        get() = listOf("zap", "zl")

    override val pluginName: String
        get() = "Zaphkiel"

    override fun build(id: String, player: Player?): ItemStack {
        val stack =
            Zaphkiel.api().getItemManager().generateItemStack(id, player) ?: warnItemNotFound(id)
        return stack
    }
}