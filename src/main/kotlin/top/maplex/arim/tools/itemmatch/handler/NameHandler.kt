package top.maplex.arim.tools.itemmatch.handler

import org.bukkit.inventory.ItemStack
import taboolib.module.chat.uncolored
import top.maplex.arim.tools.itemmatch.model.CompoundType
import top.maplex.arim.tools.itemmatch.model.MatchCondition
import top.maplex.arim.tools.itemmatch.model.StringOperation
import top.maplex.arim.tools.itemmatch.util.ParserUtils

class NameHandler : ItemHandler {

    override fun check(item: ItemStack, value: String): Boolean {
        val meta = item.itemMeta ?: return false
        if (!meta.hasDisplayName()) {
            return false
        }
        val displayName = meta.displayName
        return when (val condition = ParserUtils.parseListCondition(value)) {
            is MatchCondition.StringCondition -> {
                checkStringCondition(displayName, condition)
            }
            is MatchCondition.CompoundCondition -> {
                when (condition.type) {
                    CompoundType.ANY -> condition.conditions.any { checkStringCondition(displayName, it as MatchCondition.StringCondition) }
                    CompoundType.ALL -> condition.conditions.all { checkStringCondition(displayName, it as MatchCondition.StringCondition) }
                    CompoundType.NONE -> condition.conditions.none { checkStringCondition(displayName, it as MatchCondition.StringCondition) }
                }
            }
            else -> false
        }
    }

    private fun checkStringCondition(displayName: String, condition: MatchCondition.StringCondition): Boolean {
        return when (condition.operation) {
            StringOperation.EXACT -> displayName == condition.values.first()
            StringOperation.CONTAINS -> condition.values.any { displayName.contains(it, true) }
            StringOperation.REGEX -> condition.values.any { Regex(it, RegexOption.IGNORE_CASE).containsMatchIn(displayName) }
            StringOperation.STARTS_WITH -> condition.values.any { displayName.startsWith(it, true) }
            StringOperation.ENDS_WITH -> condition.values.any { displayName.endsWith(it, true) }
        }
    }
}
