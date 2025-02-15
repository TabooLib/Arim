package top.maplex.arim

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object Test {

    fun main(itemStack: ItemStack) {

        Arim.itemMatch.match(itemStack,"name:all(startswith(&c机械),c(靴))")
    }

}
