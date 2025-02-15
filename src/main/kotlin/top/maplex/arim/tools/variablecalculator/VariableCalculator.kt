package top.maplex.arim.tools.variablecalculator

import top.maplex.arim.tools.variablecalculator.impl.*
import java.util.*
import kotlin.math.pow


/**
 * 变量计算器，适合复杂的计算表达式，包含变量
 * @Author Saukiya
 */
class VariableCalculator {

    // 简单计算
    fun calculate(expression: String): Double {
        return parse(expression).evaluate(mapOf())
    }

    // 创建上下文
    fun parseContext(expression: String): NodeContext {
        return NodeContext(parse(expression))
    }

    fun parse(expression: String): Node {
        val nodes = ArrayDeque<Node>()
        val operator = ArrayDeque<Char>()
        operator.push('?')

        var num = 0.0
        var numBits = 0
        var canNegative = true
        var dynamic: StringBuilder? = null

        for (c in expression.toCharArray()) {
            if (dynamic != null) {
                when (c) {
                    '+', '-', '*', '/', '(', ')', ' ' -> {
                        nodes.add(DynamicNode(dynamic.toString()))
                        dynamic = null
                        canNegative = false
                    }
                    else -> {
                        dynamic.append(c)
                        continue
                    }
                }
            }
            when (c) {
                '(' -> {
                    canNegative = true
                    operator.push(c)
                    if (numBits != 0) {
                        nodes.push(ValueNode(num))
                        num = 0.0
                        numBits = 0
                    }
                }

                ')' -> {
                    canNegative = false
                    var currentNode = if (numBits != 0) ValueNode(num) else nodes.pop()
                    while (operator.peek() != '(') {
                        currentNode = operator(nodes.pop(), currentNode, operator.pop())
                    }
                    operator.pop() // 弹出 '('
                    nodes.push(currentNode)
                    num = 0.0
                    numBits = 0
                }

                '+', '*', '/', '-', '%' -> {
                    if (canNegative) {
                        if (c == '-') {
                            nodes.push(ValueNode(0.0))
                            operator.push(c)
                            continue
                        }
//                        // 变量载入
//                        if (c == '&') {
//                            dynamic = StringBuilder()
//                            continue
//                        }
                    }
                    canNegative = true
                    var currentNode = if (numBits != 0) ValueNode(num) else nodes.pop()
                    val priority = getPriority(c)
                    while (priority <= getPriority(operator.peek())) {
                        currentNode = operator(nodes.pop(), currentNode, operator.pop())
                    }
                    operator.push(c)
                    nodes.push(currentNode)
                    num = 0.0
                    numBits = 0
                }

                in '0'..'9' -> {
                    canNegative = false
                    if (numBits >= 0) {
                        num = num * 10 + (c - '0').toDouble()
                        numBits++
                    } else {
                        num += (c - '0') * 10.0.pow(numBits.toDouble())
                        numBits--
                    }
                }

                '.' -> {
                    if (numBits > 0) {
                        numBits = -1
                    }
                }

                else -> {
                    dynamic = StringBuilder()
                    if (numBits != 0) {
                        dynamic.append(num.toInt())
                        numBits = 0
                        num = 0.0
                    }
                    dynamic.append(c)
                }
            }
        }

        var currentNode = if (numBits != 0) ValueNode(num) else nodes.pop()

        while (operator.peek() != '?') {
            currentNode = operator(nodes.pop(), currentNode, operator.pop())
        }
        return currentNode
    }

    private fun operator(a1: Node, a2: Node, op: Char): Node = when (op) {
        '+' -> AdditionNode(a1, a2)
        '-' -> SubtractionNode(a1, a2)
        '*' -> MultiplicationNode(a1, a2)
        '/' -> DivisionNode(a1, a2)
        '%' -> ModulusNode(a1, a2)
        else -> throw IllegalStateException("非法操作符: $op")
    }

    private fun getPriority(op: Char): Int = when (op) {
        '?' -> 0
        '(' -> 1
        '+', '-' -> 2
        '*', '/', '%' -> 3
        else -> throw IllegalStateException("非法操作符: $op")
    }


}
