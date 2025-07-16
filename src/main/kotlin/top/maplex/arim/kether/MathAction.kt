package top.maplex.arim.kether

import com.notkamui.keval.Keval
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * 用法
 *
 * arim-math (1+1)*10.5/0.5
 *
 * 由于 Java15+ 移除 Nashorn JavaScript，则使用 Keval 替代完成数学运算
 */
@KetherParser(["arim-math"], shared = true)
fun parseMath() = combinationParser {
    it.group(
        text()
    ).apply(it) { math ->
        now {
            Keval.eval(math)
        }
    }
}