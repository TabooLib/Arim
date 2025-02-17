package top.maplex.arim.tools.weightrandom

import taboolib.common.util.random
import top.maplex.arim.Arim

class WeightRandom {

    fun <T> getWeightRandom(categories: Collection<WeightCategory<T>>): T? {
        return getRandom(categories)?.category
    }

    fun <T> getRandom(categories: Collection<T>, processing: (T) -> Int): WeightCategory<T>? {
        categories.map { WeightCategory(it, processing(it)) }.let {
            return getRandom(it)
        }
    }

    fun <T> getRandom(categories: Collection<WeightCategory<T>>): WeightCategory<T>? {
        var weightSum = 0
        categories.forEach { wc ->
            weightSum += wc.weight
        }
        if (weightSum <= 0) {
            return null
        }
        val n = random().nextInt(weightSum)
        var m = 0
        categories.forEach { wc ->
            if (m <= n && n < m + wc.weight) {
                return wc
            }
            m += wc.weight
        }
        return null
    }

    data class WeightCategory<T>(
        var category: T,
        var weight: Int
    )


}

fun <T> Collection<T>.randomWeight(processing: (T) -> Int): WeightRandom.WeightCategory<T>? {
    return Arim.weightRandom.getRandom(this, processing)
}

fun <T> Collection<T>.randomWeightValue(processing: (T) -> Int): T? {
    return Arim.weightRandom.getRandom(this, processing)?.category
}
