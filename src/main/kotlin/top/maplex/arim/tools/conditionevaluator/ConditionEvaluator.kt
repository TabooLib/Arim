package top.maplex.arim.tools.conditionevaluator

import taboolib.common5.util.replace

/**
 * 条件表达式求值器
 * 用于解析并求值条件表达式
 * @Author FxRayHughes
 *
 * example:
 * ```
 * val evaluator = ConditionEvaluator() // 不需要手动创建对象，从Arim来创建即可
 * val result = evaluator.evaluate("1 > 2")
 * ```
 */
class ConditionEvaluator {

    // 运算符优先级
    private val precedence = mapOf(
        "&&" to 1,
        "||" to 1,
        ">" to 2,
        "<" to 2,
        ">=" to 2,
        "<=" to 2,
        "==" to 2,
        "!=" to 2,
        "(" to 0,
        ")" to 0
    )

    // 判断是否为数字
    private fun isNumber(str: String): Boolean {
        return str.toDoubleOrNull() != null
    }

    // 判断是否为字符串（被单引号或双引号包围）
    private fun isString(str: String): Boolean {
        return (str.startsWith("'") && str.endsWith("'")) ||
                (str.startsWith("\"") && str.endsWith("\""))
    }

    // 判断是否为运算符
    private fun isOperator(str: String): Boolean {
        return precedence.containsKey(str) && str != "(" && str != ")"
    }

    // 比较运算符优先级
    private fun hasPrecedence(op1: String, op2: String): Boolean {
        val p1 = precedence[op1] ?: 0
        val p2 = precedence[op2] ?: 0
        return p1 >= p2
    }

    // 包装类用于存储不同类型的值
    sealed class Value {
        data class NumberValue(val value: Double) : Value()
        data class StringValue(val value: String) : Value()
    }

    // 解析值
    private fun parseValue(token: String): Value {
        return when {
            isNumber(token) -> Value.NumberValue(token.toDouble())
            isString(token) -> Value.StringValue(token.substring(1, token.length - 1))
            else -> throw IllegalArgumentException("Invalid value: $token")
        }
    }

    // 执行比较操作
    private fun compare(operator: String, b: Value, a: Value): Boolean {
        return when {
            a is Value.NumberValue && b is Value.NumberValue -> {
                when (operator) {
                    ">" -> a.value > b.value
                    "<" -> a.value < b.value
                    ">=" -> a.value >= b.value
                    "<=" -> a.value <= b.value
                    "==" -> a.value == b.value
                    "!=" -> a.value != b.value
                    else -> throw IllegalArgumentException("Invalid operator for numbers: $operator")
                }
            }

            a is Value.StringValue && b is Value.StringValue -> {
                when (operator) {
                    ">" -> a.value > b.value
                    "<" -> a.value < b.value
                    ">=" -> a.value >= b.value
                    "<=" -> a.value <= b.value
                    "==" -> a.value == b.value
                    "!=" -> a.value != b.value
                    else -> throw IllegalArgumentException("Invalid operator for strings: $operator")
                }
            }

            else -> throw IllegalArgumentException("Cannot compare different types")
        }
    }

    fun evaluate(expression: String, variable: Map<String, Any>): Boolean {
        return evaluate(expression.replace(*variable.map { it.key to it.value.toString() }.toTypedArray()))
    }

    fun evaluate(expression: String, vararg variable: Pair<String, Any>): Boolean {
        return evaluate(expression.replace(*variable))
    }

    fun evaluate(expression: String): Boolean {
        val tokens = expression.split(" ").filter { it.isNotEmpty() }
        val values = ArrayDeque<Value>()
        val operators = ArrayDeque<String>()

        for (token in tokens) {
            when {
                isNumber(token) || isString(token) -> {
                    values.addLast(parseValue(token))
                }

                token == "(" -> {
                    operators.addLast(token)
                }

                token == ")" -> {
                    while (operators.isNotEmpty() && operators.last() != "(") {
                        val b = values.removeLast()
                        val a = values.removeLast()
                        val op = operators.removeLast()
                        val result = when (op) {
                            "&&" -> Value.NumberValue(
                                if ((a is Value.NumberValue && a.value != 0.0) &&
                                    (b is Value.NumberValue && b.value != 0.0)
                                ) 1.0 else 0.0
                            )

                            "||" -> Value.NumberValue(
                                if ((a is Value.NumberValue && a.value != 0.0) ||
                                    (b is Value.NumberValue && b.value != 0.0)
                                ) 1.0 else 0.0
                            )

                            else -> Value.NumberValue(if (compare(op, b, a)) 1.0 else 0.0)
                        }
                        values.addLast(result)
                    }
                    if (operators.isNotEmpty() && operators.last() == "(") {
                        operators.removeLast()
                    }
                }

                isOperator(token) -> {
                    while (operators.isNotEmpty() && operators.last() != "(" && hasPrecedence(operators.last(), token)) {
                        val b = values.removeLast()
                        val a = values.removeLast()
                        val op = operators.removeLast()
                        val result = when (op) {
                            "&&" -> Value.NumberValue(
                                if ((a is Value.NumberValue && a.value != 0.0) &&
                                    (b is Value.NumberValue && b.value != 0.0)
                                ) 1.0 else 0.0
                            )

                            "||" -> Value.NumberValue(
                                if ((a is Value.NumberValue && a.value != 0.0) ||
                                    (b is Value.NumberValue && b.value != 0.0)
                                ) 1.0 else 0.0
                            )

                            else -> Value.NumberValue(if (compare(op, b, a)) 1.0 else 0.0)
                        }
                        values.addLast(result)
                    }
                    operators.addLast(token)
                }
            }
        }

        while (operators.isNotEmpty()) {
            val b = values.removeLast()
            val a = values.removeLast()
            val op = operators.removeLast()
            val result = when (op) {
                "&&" -> Value.NumberValue(
                    if ((a is Value.NumberValue && a.value != 0.0) &&
                        (b is Value.NumberValue && b.value != 0.0)
                    ) 1.0 else 0.0
                )

                "||" -> Value.NumberValue(
                    if ((a is Value.NumberValue && a.value != 0.0) ||
                        (b is Value.NumberValue && b.value != 0.0)
                    ) 1.0 else 0.0
                )

                else -> Value.NumberValue(if (compare(op, b, a)) 1.0 else 0.0)
            }
            values.addLast(result)
        }

        return (values.last() as Value.NumberValue).value != 0.0
    }

}
