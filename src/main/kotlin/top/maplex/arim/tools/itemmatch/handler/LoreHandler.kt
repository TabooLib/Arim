package top.maplex.arim.tools.itemmatch.handler

import org.bukkit.inventory.ItemStack
import taboolib.module.chat.uncolored
import top.maplex.arim.tools.itemmatch.model.CompoundType
import top.maplex.arim.tools.itemmatch.model.MatchCondition
import top.maplex.arim.tools.itemmatch.model.StringOperation.*
import top.maplex.arim.tools.itemmatch.util.ParserUtils

class LoreHandler : ItemHandler {

    override fun check(item: ItemStack, value: String): Boolean {
        val meta = item.itemMeta ?: return false
        val lore = meta.lore ?: emptyList()
        if (lore.isEmpty()) return value == "none"

        return when (val condition = ParserUtils.parseListCondition(value)) {
            is MatchCondition.StringCondition -> checkStringCondition(lore, condition)
            is MatchCondition.CompoundCondition -> checkCompoundCondition(lore, condition)
            else -> false
        }
    }

    private fun checkStringCondition(stringList: List<String>, condition: MatchCondition.StringCondition): Boolean {
        var lore = stringList
        if (condition.modifiers.containsAll(listOf("uncolored", "uc"))) {
            lore = lore.uncolored()
        }
        if (condition.modifiers.containsAll(listOf("lowercase", "lc"))) {
            lore = lore.map { it.lowercase() }
        }
        return when (condition.operation) {
            EXACT -> lore == condition.values
            CONTAINS -> {
                condition.values.any { v ->
                    lore.any { it.contains(v, true) }
                }
            }

            REGEX -> {
                condition.values.any { v ->
                    lore.any { Regex(v, RegexOption.IGNORE_CASE).containsMatchIn(it) }
                }
            }

            STARTS_WITH -> {
                condition.values.any { v ->
                    lore.any { it.startsWith(v, true) }
                }
            }

            ENDS_WITH -> {
                condition.values.any { v ->
                    lore.any { it.endsWith(v, true) }
                }
            }
        }
    }

    private fun checkCompoundCondition(lore: List<String>, condition: MatchCondition.CompoundCondition): Boolean {
        return when (condition.type) {
            CompoundType.ANY -> condition.conditions.any { checkStringCondition(lore, it as MatchCondition.StringCondition) }
            CompoundType.ALL -> condition.conditions.all { checkStringCondition(lore, it as MatchCondition.StringCondition) }
            CompoundType.NONE -> condition.conditions.none { checkStringCondition(lore, it as MatchCondition.StringCondition) }
        }
    }
}
