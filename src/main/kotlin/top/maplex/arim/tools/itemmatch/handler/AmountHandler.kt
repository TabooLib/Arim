package top.maplex.arim.tools.itemmatch.handler

import org.bukkit.inventory.ItemStack
import top.maplex.arim.tools.itemmatch.model.NumberOperator
import top.maplex.arim.tools.itemmatch.util.ParserUtils

class AmountHandler : ItemHandler {

    override fun check(item: ItemStack, value: String): Boolean {
        val condition = ParserUtils.parseNumberCondition(value)
        return when (condition.operator) {
            NumberOperator.GREATER_EQUAL -> item.amount >= condition.value
            NumberOperator.LESS_EQUAL -> item.amount <= condition.value
            NumberOperator.GREATER -> item.amount > condition.value
            NumberOperator.LESS -> item.amount < condition.value
            else -> item.amount == condition.value
        }
    }
}
