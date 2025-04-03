package top.maplex.arim.tools.itemmanager.source

import com.pxpmc.pxrpg.api.MAPI
import com.pxpmc.pxrpg.api.Module
import com.pxpmc.pxrpg.api.modules.item.ItemManager
import com.pxpmc.pxrpg.api.modules.item.ItemModule
import com.pxpmc.pxrpg.api.util.ParameterResolver
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import top.maplex.arim.tools.itemmanager.ItemSource


class SourcePxRpg : ItemSource {

    override val name: String
        get() = "pxrpg"

    override val alias: List<String>
        get() = listOf("pr")

    override val pluginName: String
        get() = "PxRpg"

    private lateinit var manager: ItemManager

    /**
     * id,level=2;bind=已绑定
     */
    override fun build(id: String, player: Player?): ItemStack {
        if (!::manager.isInitialized) {
            manager = Module.getModule(ItemModule::class.java).itemManager
        }
        val args = id.split(",").getOrNull(1)
        val itemId = id.split(",")[0]
        val itemConfig = manager.getRegister(itemId)
        val pxPlayer = MAPI.getBukkitPxRpgAPI().toPxRpgPlayer(player)
        val argsParser = ParameterResolver()
        args?.let {
            argsParser.parser(args)
        }
        val item = manager.spawnItemStack(
            itemConfig,
            pxPlayer,
            null,
            argsParser
        )
        return MAPI.getBukkitPxRpgAPI().toBukkitItemStack(item) ?: warnItemNotFound(itemId)
    }
}