package top.maplex.arim.kether

import taboolib.common5.cint
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import top.maplex.arim.Arim
import top.maplex.arim.tools.weightrandom.WeightRandom
import kotlin.jvm.optionals.getOrNull

/**
 * 用法
 *
 * arim-weight / arim-random
 *
 * arim-weight {action list} [(by|with) {action list}] [def {action}]
 *
 * arim-weight [ ele1 ele2 ele3 ] by [ 1 null 3 ] def 1
 */
@KetherParser(["arim-weight", "arim-random"], shared = true)
fun parseWeightRandom() = combinationParser {
    it.group(
        originList(),
        command("by", "with", then = originList()).optional(),
        command("def", "default", then = text()).optional()
    ).apply(it) { list, weightOrNull, default ->
        now {
            val weight = weightOrNull?.getOrNull()?.map { w -> w?.cint ?: 1 } ?: list.map { 1 }

            val weightMap = list.mapIndexedNotNull { index, ele ->
                WeightRandom.WeightCategory(ele ?: default.getOrNull(), weight.getOrNull(index)?.cint ?: 1)
            }

            Arim.weightRandom.getWeightRandom(weightMap) ?: default.getOrNull()
        }
    }
}