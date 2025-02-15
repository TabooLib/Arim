package top.maplex.arim.tools.itemmatch.handler

import org.bukkit.inventory.ItemStack
import top.maplex.arim.tools.itemmatch.model.NumberOperator
import top.maplex.arim.tools.itemmatch.util.ParserUtils

class DurabilityHandler : ItemHandler {

    override fun check(item: ItemStack, value: String): Boolean {
        val maxDurability = item.type.maxDurability
        if (maxDurability <= 0) return false
        val current = maxDurability - item.durability
        val condition = ParserUtils.parseNumberCondition(value)
        return when (condition.operator) {
            NumberOperator.GREATER_EQUAL -> current >= condition.value
            NumberOperator.LESS_EQUAL -> current <= condition.value
            NumberOperator.GREATER -> current > condition.value
            NumberOperator.LESS -> current < condition.value
            else -> current == condition.value
        }
    }
}
