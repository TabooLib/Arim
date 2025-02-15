package top.maplex.arim.tools.itemmatch

import org.bukkit.inventory.ItemStack
import taboolib.module.chat.colored
import top.maplex.arim.tools.itemmatch.handler.*
import top.maplex.arim.tools.itemmatch.util.ParserUtils
import java.util.concurrent.ConcurrentHashMap

/**
 * 物品匹配公式
 * @Author FxRayHughes
 */
class ItemMatch {

    private val handlers = ConcurrentHashMap<String, ItemHandler>()

    fun registerHandler(name: String, handler: ItemHandler) {
        handlers[name] = handler
    }

    fun unregisterHandler(name: String) {
        handlers.remove(name)
    }

    init {
        registerHandler("name", NameHandler())
        registerHandler("lore", LoreHandler())
        registerHandler("nbt", NbtHandler())
        registerHandler("enchant", EnchantHandler())
        registerHandler("flag", FlagHandler())
        registerHandler("material", MaterialHandler())
        registerHandler("amount", AmountHandler())
        registerHandler("durability", DurabilityHandler())
        registerHandler("unbreakable", UnbreakableHandler())
    }

    fun match(itemStack: ItemStack, match: String): Boolean {
        return ParserUtils.splitConditions(match).all { condition ->
            val (key, value) = ParserUtils.parseKeyValue(condition) ?: return@all false
            handlers[key]?.check(itemStack, value.colored()) ?: false
        }
    }

    fun matchTry(itemStack: ItemStack, match: String): Boolean {
        return try {
            match(itemStack, match)
        } catch (e: Exception) {
            false
        }
    }

}
