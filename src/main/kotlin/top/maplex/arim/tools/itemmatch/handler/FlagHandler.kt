package top.maplex.arim.tools.itemmatch.handler

import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import top.maplex.arim.tools.itemmatch.model.CompoundType
import top.maplex.arim.tools.itemmatch.model.MatchCondition
import top.maplex.arim.tools.itemmatch.util.ParserUtils

class FlagHandler : ItemHandler {

    override fun check(item: ItemStack, value: String): Boolean {
        val meta = item.itemMeta ?: return false
        return when (val condition = ParserUtils.parseListCondition(value)) {
            is MatchCondition.StringCondition -> checkSingleFlag(meta, condition.values.first())
            is MatchCondition.CompoundCondition -> checkCompoundCondition(meta, condition)
            else -> false
        }
    }

    private fun checkCompoundCondition(meta: ItemMeta, condition: MatchCondition.CompoundCondition): Boolean {
        return when (condition.type) {
            CompoundType.ANY -> condition.conditions.any {
                checkSingleFlag(meta, (it as MatchCondition.StringCondition).values.first())
            }

            CompoundType.ALL -> condition.conditions.all {
                checkSingleFlag(meta, (it as MatchCondition.StringCondition).values.first())
            }

            CompoundType.NONE -> condition.conditions.none {
                checkSingleFlag(meta, (it as MatchCondition.StringCondition).values.first())
            }
        }
    }

    private fun checkSingleFlag(meta: ItemMeta, value: String): Boolean {
        val flag = ItemFlag.entries.firstOrNull { it.name.equals(value, true) } ?: return false
        return meta.hasItemFlag(flag)
    }
}
