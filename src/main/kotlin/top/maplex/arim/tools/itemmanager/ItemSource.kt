package top.maplex.arim.tools.itemmanager

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.common.platform.function.warning
import taboolib.library.xseries.XMaterial
import taboolib.module.chat.colored
import taboolib.platform.util.buildItem
import top.maplex.arim.tools.itemmanager.source.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 示例
 * ```kotlin
 * class SourceMyCustomItem : ItemSource {
 *
 *     override val name: String
 *         get() = "mythicmobs"
 *
 *     override val alias: List<String>
 *         get() = listOf("mm", "mythic")
 *
 *     override val pluginName: String
 *         get() = "MythicMobs"
 *
 *     override val isLoaded: Boolean
 *         get() = ItemSource.getPluginLoaded(pluginName)
 *
 *     override fun build(id: String, player: Player?): ItemStack {
 *         return Mythic.API.getItemStack(id, player) ?: ItemSource.warnItemNotFound(pluginName, id)
 *     }
 * }
 *
 * // 启动时使用 API 注册
 * ItemSource.register(SourceMyCustomItem())
 * ```
 *
 * @property name 物品库名，纯小写
 * @property alias 物品库别名
 * @property pluginName 物品库插件名
 * @property isLoaded 是否已加载插件
 */
interface ItemSource {

    val name: String

    val alias: List<String>

    val pluginName: String

    val isLoaded: Boolean
        get() = getPluginLoaded(pluginName)

    fun build(id: String, player: Player? = null): ItemStack

    fun warnItemNotFound(value: String): ItemStack {
        return buildItem(XMaterial.BEDROCK).also {
            info("&c获取 *$pluginName* 物品时出错 *$value* (物品库不存在该ID)".colored())
        }
    }

    companion object {

        private val sources = ConcurrentHashMap<String, ItemSource>()

        @JvmStatic
        operator fun get(source: String): ItemSource? {
            return sources[source]
        }

        private fun getPluginLoaded(pluginName: String): Boolean {
            return Bukkit.getPluginManager().getPlugin(pluginName) != null
        }

        @Awake(LifeCycle.ACTIVE)
        fun registerSource() {
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
            ).forEach(Companion::register)
        }

        @JvmStatic
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

        @JvmStatic
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
        @JvmStatic
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
}