package top.maplex.arim.tools.itemmatch.handler

import org.bukkit.inventory.ItemStack
import taboolib.module.nms.getItemTag
import top.maplex.arim.tools.itemmatch.util.ParserUtils

class NbtHandler : ItemHandler {

    override fun check(item: ItemStack, value: String): Boolean {
        if (!value.startsWith("{") || !value.endsWith("}")) return false
        val nbtData = ParserUtils.parseNbt(value.removeSurrounding("{", "}")) ?: return false
        val tag = item.getItemTag()
        return nbtData.all { (path, expected) ->
            val actual = tag.getDeep(path) ?: return@all false
            when (expected) {
                is String -> actual.asString() == expected
                is Number -> actual.asDouble() == expected.toDouble()
                is Boolean -> actual.asString() == expected.toString()
                else -> false
            }
        }
    }
}
