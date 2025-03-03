package top.maplex.arim.kether

import org.bukkit.entity.Player
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.module.kether.player
import taboolib.platform.util.countItem
import taboolib.platform.util.hasItem
import taboolib.platform.util.takeItem
import top.maplex.arim.Arim
import kotlin.jvm.optionals.getOrElse

/**
 * 用法
 * https://taboolib.feishu.cn/wiki/SRg4wO9q0iI3kbkdX79cHQRWncb
 *
 * arim-inv / arim-inventory
 *
 * arim-inv {token} {action} [amount {action}]
 *
 * arim-inv check "name:all(start(&c机械),c(靴))" amount 10
 * arim-inv take "name:all(start(&c机械),c(靴))" amount 10
 * arim-inv count "name:all(start(&c机械),c(靴))"
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
            val inventory = player.inventory
            when (type) {
                "check", "has" -> {
                    inventory.hasItem(amount.getOrElse { 1 }) { itemStack ->
                        Arim.itemMatch.match(itemStack, condition)
                    }
                }

                "take" -> {
                    val has = inventory.hasItem(amount.getOrElse { 1 }) { itemStack ->
                        Arim.itemMatch.match(itemStack, condition)
                    }
                    inventory.takeItem(amount.getOrElse { 1 }) { itemStack ->
                        Arim.itemMatch.match(itemStack, condition)
                    }
                    has
                }

                "count" -> {
                    inventory.countItem { itemStack ->
                        Arim.itemMatch.match(itemStack, condition)
                    }
                }

                else -> null
            }
        }
    }
}