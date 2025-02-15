package top.maplex.arim.tools.itemmatch.handler

import org.bukkit.inventory.ItemStack

class UnbreakableHandler : ItemHandler {

    override fun check(item: ItemStack, value: String): Boolean {
        val meta = item.itemMeta ?: return false
        return when (value.lowercase()) {
            "true" -> meta.isUnbreakable
            "false" -> !meta.isUnbreakable
            else -> false
        }
    }
}
