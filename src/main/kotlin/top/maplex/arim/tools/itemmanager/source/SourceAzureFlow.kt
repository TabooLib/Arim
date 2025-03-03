package top.maplex.arim.tools.itemmanager.source


import io.rokuko.azureflow.api.AzureFlowAPI
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import top.maplex.arim.tools.itemmanager.ItemSource


class SourceAzureFlow : ItemSource {

    override val name: String
        get() = "azureflow"

    override val alias: List<String>
        get() = listOf("af")

    override val pluginName: String
        get() = "AzureFlow"

    override fun build(id: String, player: Player?): ItemStack {
        val factory = AzureFlowAPI.getFactory(id)
        val stack = factory?.build()?.virtualItemStack(player) ?: warnItemNotFound(id)
        return stack
    }
}