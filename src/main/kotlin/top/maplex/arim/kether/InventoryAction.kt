package top.maplex.arim.kether

import org.bukkit.entity.Player
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.module.kether.player
import taboolib.platform.util.hasItem
import top.maplex.arim.Arim
import kotlin.jvm.optionals.getOrElse

/**
 * 用法
 *
 * arim-inv / arim-inventory
 *
 * arim-inv {token} {action} [amount {action}]
 *
 * arim-inv check "name:all(start(&c机械),c(靴))" amount 10
 */
@KetherParser(["arim-inv", "arim-inventory"], shared = true)
fun parseInventory() = combinationParser {
    it.group(
        symbol(),
        text(),
        command("amount", then = int()).optional()
    ).apply(it) { type, condition, amount ->
        now {
            val player = player().castSafely<Player>() ?: return@now false
            when (type) {
                "check", "has" -> {
                    player.inventory.hasItem(amount.getOrElse { 1 }) { itemStack ->
                        Arim.itemMatch.match(itemStack, condition)
                    }
                }

                else -> null
            }
        }
    }
}