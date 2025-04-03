package top.maplex.arim.tools.itemmanager

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.info
import taboolib.library.xseries.XMaterial
import taboolib.module.chat.colored
import taboolib.platform.util.buildItem

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

        @JvmStatic
        fun getPluginLoaded(pluginName: String): Boolean {
            return Bukkit.getPluginManager().getPlugin(pluginName) != null
        }
    }
}