package top.maplex.arim.utils

/**
 * 判断列表是否包含另一个列表中的任意元素
 */
fun containsList(list1: List<String>, list2: List<String>): Boolean {
    // 将较短的列表转为 HashSet 以优化空间和查找效率
    val (smallerList, largerList) = if (list1.size < list2.size) list1 to list2 else list2 to list1
    val set = smallerList.toHashSet()
    // 遍历较长的列表，利用 HashSet 的 O(1) 查找特性
    return largerList.any { it in set }
}

/**
 * 判断列表是否包含另一个列表中的任意元素
 */
fun <T> List<T>.containsAny(other: List<T>): Boolean {
    val (smallerList, largerList) = if (this.size < other.size) this to other else other to this
    val set = smallerList.toHashSet()
    return largerList.any { it in set }
}

