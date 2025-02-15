package top.maplex.arim.tools.itemmatch.handler

import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import top.maplex.arim.tools.itemmatch.model.CompoundType
import top.maplex.arim.tools.itemmatch.model.MatchCondition
import top.maplex.arim.tools.itemmatch.model.NumberOperator
import top.maplex.arim.tools.itemmatch.model.StringOperation

class EnchantHandler : ItemHandler {

    override fun check(item: ItemStack, value: String): Boolean {
        return when (val condition = parseEnchantCondition(value)) {
            is MatchCondition.CompoundCondition -> checkCompoundCondition(item, condition)
            is MatchCondition.NumberCondition -> checkSingleEnchant(item, value)
            else -> false
        }
    }

    private fun parseEnchantCondition(value: String): MatchCondition {
        return when {
            value.contains('|') -> {
                val subConditions = value.split('|').map { parseSingleCondition(it.trim()) }
                MatchCondition.CompoundCondition(CompoundType.ANY, subConditions)
            }

            value.contains('&') -> {
                val subConditions = value.split('&').map { parseSingleCondition(it.trim()) }
                MatchCondition.CompoundCondition(CompoundType.ALL, subConditions)
            }

            else -> parseSingleCondition(value)
        }
    }

    private fun parseSingleCondition(condition: String): MatchCondition {
        val pattern = Regex("""(\w+)(>=|<=|>|<|=)?(\d+)""")
        val match = pattern.matchEntire(condition) ?: return MatchCondition.StringCondition(
            StringOperation.EXACT,
            listOf(condition)
        )

        return MatchCondition.NumberCondition(
            when (match.groupValues[2]) {
                ">=" -> NumberOperator.GREATER_EQUAL
                "<=" -> NumberOperator.LESS_EQUAL
                ">" -> NumberOperator.GREATER
                "<" -> NumberOperator.LESS
                else -> NumberOperator.EQUAL
            },
            match.groupValues[3].toInt()
        ).apply {
            tag = match.groupValues[1].uppercase()
        }
    }

    private fun checkSingleEnchant(item: ItemStack, value: String): Boolean {
        val (enchantName, operator, level) = parseEnchantExpression(value) ?: return false
        val enchant = Enchantment.getByName(enchantName) ?: return false
        val actualLevel = item.getEnchantmentLevel(enchant)

        return when (operator) {
            ">" -> actualLevel > level
            "<" -> actualLevel < level
            ">=" -> actualLevel >= level
            "<=" -> actualLevel <= level
            else -> actualLevel == level
        }
    }

    private fun checkCompoundCondition(item: ItemStack, condition: MatchCondition.CompoundCondition): Boolean {
        return when (condition.type) {
            CompoundType.ANY -> condition.conditions.any { checkCondition(item, it) }
            CompoundType.ALL -> condition.conditions.all { checkCondition(item, it) }
            CompoundType.NONE -> condition.conditions.none { checkCondition(item, it) }
        }
    }

    private fun checkCondition(item: ItemStack, condition: MatchCondition): Boolean {
        return when (condition) {
            is MatchCondition.NumberCondition -> {
                val enchant = Enchantment.getByName(condition.tag) ?: return false
                val actualLevel = item.getEnchantmentLevel(enchant)
                when (condition.operator) {
                    NumberOperator.GREATER_EQUAL -> actualLevel >= condition.value
                    NumberOperator.LESS_EQUAL -> actualLevel <= condition.value
                    NumberOperator.GREATER -> actualLevel > condition.value
                    NumberOperator.LESS -> actualLevel < condition.value
                    else -> actualLevel == condition.value
                }
            }

            is MatchCondition.StringCondition -> {
                Enchantment.values().any { it.name.equals(condition.values.first(), true) }
            }

            else -> false
        }
    }

    private fun parseEnchantExpression(exp: String): Triple<String, String, Int>? {
        val pattern = Regex("""(\w+)(>=|<=|>|<|=)?(\d+)""")
        val match = pattern.matchEntire(exp) ?: return null
        return Triple(
            match.groupValues[1],
            match.groupValues[2].takeIf { it.isNotEmpty() } ?: "=",
            match.groupValues[3].toInt()
        )
    }
}
