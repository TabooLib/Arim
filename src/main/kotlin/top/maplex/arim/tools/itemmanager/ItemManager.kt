package top.maplex.arim.tools.itemmanager

import org.bukkit.entity.Player
import taboolib.common.platform.function.info
import taboolib.common.platform.function.warning
import taboolib.module.chat.colored
import top.maplex.arim.tools.itemmanager.source.*
import java.util.concurrent.ConcurrentHashMap

class ItemManager {
    private val sources = ConcurrentHashMap<String, ItemSource>()

    operator fun get(source: String): ItemSource? {
        return sources[source]
    }

    init {
        listOf(
            SourceAzureFlow(),
            SourceCraftEngine(),
            SourceItemsAdder(),
            SourceMinecraft(),
            SourceMMOItems(),
            SourceMythic(),
            SourceNeigeItems(),
            SourceOraxen(),
            SourcePxRpg(),
            SourceSXItem(),
            SourceZaphkiel()
        ).forEach(::register)
    }

    fun register(instance: ItemSource) {
        if (sources.contains(instance.name)) {
            info("已存在 &c${instance.name} &r物品源挂钩，请勿重新挂钩".colored())
            return
        }
        if (instance.isLoaded) {
            sources += instance.name to instance
            info("已挂钩软依赖 &c${instance.pluginName} &r作为物品源".colored())
        }
    }

    fun getSource(source: String): ItemSource {
        val noHookMsg = "解析物品源 $source 未挂钩"
        return sources.values.firstOrNull {
            it.name == source || it.alias.contains(source)
        } ?: sources["minecraft"]!!.also { warning(noHookMsg.colored()) }
    }

    /**
     * @param id 物品ID
     * @sample "minecraft:stone"
     */
    fun parse2ItemStack(id: String, player: Player? = null): BuildSourceItem {
        val split = id.split(":")
        var source = split[0]
        val itemID = split.getOrElse(1) { source.also { source = "minecraft" } }

        var sourceName = "Minecraft"
        val itemStack = try {
            val s = getSource(source)
            s.build(itemID.trim(), player).also { sourceName = s.pluginName }
        } catch (e: Exception) {
            e.printStackTrace()
            getSource("Minecraft").build(itemID.trim(), player)
        }
        return BuildSourceItem(player, itemID, itemStack, sourceName)
    }
}