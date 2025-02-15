package top.maplex.arim.tools.itemmatch.handler

import org.bukkit.inventory.ItemStack
import top.maplex.arim.tools.itemmatch.model.StringOperation
import top.maplex.arim.tools.itemmatch.util.ParserUtils

class MaterialHandler : ItemHandler {

    override fun check(item: ItemStack, value: String): Boolean {
        val materialName = item.type.name
        val condition = ParserUtils.parseStringCondition(value)
        return when (condition.operation) {
            StringOperation.EXACT -> condition.values.any { materialName.equals(it, true) }
            StringOperation.CONTAINS -> condition.values.any { materialName.contains(it, true) }
            StringOperation.REGEX -> condition.values.any {
                Regex(it, RegexOption.IGNORE_CASE).containsMatchIn(materialName)
            }
            StringOperation.STARTS_WITH -> condition.values.any { materialName.startsWith(it, true) }
            StringOperation.ENDS_WITH -> condition.values.any { materialName.endsWith(it, true) }
        }
    }
}
