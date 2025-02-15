package top.maplex.arim.tools.fixedcalculator

import kotlin.math.pow
import java.util.ArrayDeque as Deque


/**
 * 计算器工具 - 处理数学表达式 (枫溪: 随地大小算)
 * @Author Saukiya
 * ```
 * FixedCalculator.calculator("12.42+24.13-36.2*2.3/-(-4.1+6.5)%3")
 * FixedCalculator.calculator("-3 + ((4 * (10 - (6 / 2))) - (8 % 3) + (5 + (-7) / 2))")
 * ```
 */
class FixedCalculator {

    fun evaluate(expression: String): Double {
        /*数字栈*/
        val number = Deque<Double>()
        /*符号栈*/
        val operator = Deque<Char>()
        // 在栈顶压人一个?，配合它的优先级，目的是减少下面程序的判断
        operator.push('?')

        var num = 0.0
        var numBits = 0
        var canNegative = true
        for (c_ in expression.toCharArray()) {
            var c = c_
            when (c) {
                '(' -> {
                    canNegative = true
                    operator.push('(')
                    if (numBits != 0) {
                        number.push(num)
                        numBits = 0
                        num = 0.0
                    }
                }

                ')' -> {
                    canNegative = false
                    num = if (numBits != 0) num else number.pop()
                    while ((operator.pop().also { c = it }) != '(') {
                        num = operator(number.pop(), num, c)
                    }
                    number.push(num)
                    run {
                        numBits = 0
                        num = 0.0
                    }
                }

                '+', '*', '/', '%', '-' -> {
                    if (canNegative && c == '-') {
                        // 补位
                        number.push(0.0)
                        operator.push(c)
                        continue
                    }
                    canNegative = true
                    num = if (numBits != 0) num else number.pop()
                    val priority = getPriority(c)
                    while (priority <= getPriority(operator.peek())) {
                        num = operator(number.pop(), num, operator.pop())
                    }
                    operator.push(c)
                    number.push(num)
                    run {
                        numBits = 0
                        num = 0.0
                    }
                }

                in '0'..'9' -> {
                    canNegative = false
                    if (numBits >= 0) {
                        num = (num * 10) + (c.code - 48)
                        numBits++
                    } else {
                        num += (c.code - 48) * 10.0.pow((numBits--).toDouble())
                    }
                }

                '.' -> numBits = -1
                else -> {}
            }
        }

        if (numBits == 0) {
            num = number.pop()
        }

        while (operator.peek() != '?') { //遍历结束后，符号栈数字栈依次弹栈计算，并将结果压入数字栈
            num = operator(number.pop(), num, operator.pop())
        }
        return num
    }

    private fun operator(a1: Double, a2: Double, operator: Char): Double {
        when (operator) {
            '+' -> return a1 + a2
            '-' -> return a1 - a2
            '*' -> return a1 * a2
            '/' -> return a1 / a2
            '%' -> return a1 % a2
            else -> {}
        }
        throw java.lang.IllegalStateException("illegal operator: $operator")
    }

    private fun getPriority(operator: Char): Int {
        when (operator) {
            '?' -> return 0
            '(' -> return 1
            '+', '-' -> return 2
            '*', '/', '%' -> return 3
            else -> {}
        }
        throw IllegalStateException("illegal operator: $operator")
    }

}
