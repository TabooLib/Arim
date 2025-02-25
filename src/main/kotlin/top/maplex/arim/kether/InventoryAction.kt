package top.maplex.arim.kether

import org.bukkit.entity.Player
import taboolib.module.kether.*
import taboolib.platform.util.hasItem
import top.maplex.arim.Arim
import kotlin.jvm.optionals.getOrElse

/**
 * 用法
 *
 * arim-inv / arim-inventory
 *
 * arim-inv {action} [amount {action}]
 *
 * arim-inv "name:all(start(&c机械),c(靴))" amount 10
 */
@KetherParser(["arim-inv", "arim-inventory"], shared = true)
fun parseInventory() = combinationParser {
    it.group(
        text(),
        command("amount", then = int()).optional()
    ).apply(it) { condition, amount ->
        now {
            val player = player().castSafely<Player>() ?: return@now false
            player.inventory.hasItem(amount.getOrElse { 1 }) { itemStack ->
                Arim.itemMatch.match(itemStack, condition)
            }
        }
    }
}