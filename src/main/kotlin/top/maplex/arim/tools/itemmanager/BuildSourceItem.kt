package top.maplex.arim.tools.itemmanager

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

data class BuildSourceItem(
    val player: Player?,
    val id: String,
    val itemStack: ItemStack,
    val source: String
)
