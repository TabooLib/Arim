package top.maplex.arim.tools.itemmatch.handler

import org.bukkit.inventory.ItemStack

interface ItemHandler {
    fun check(item: ItemStack, value: String): Boolean
}
