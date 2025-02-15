package top.maplex.arim

import top.maplex.arim.tools.conditionevaluator.ConditionEvaluator
import top.maplex.arim.tools.fixedcalculator.FixedCalculator
import top.maplex.arim.tools.itemmatch.ItemMatch
import top.maplex.arim.tools.variablecalculator.VariableCalculator

/**
 * Arim工具库
 * 使用文档: https://taboolib.feishu.cn/wiki/PisKwWgRHirVeRkAz9ncDY7Cn2d
 */
object Arim {

    /**
     * 条件表达式求值器
     * evaluator.evaluate("1 > 2") => false
     * evaluator.evaluate("a < 2",a to 5) => false
     *
     * 变量值会自动转换为字符串类型
     * evaluator.evaluate("status == 'ok'", "status" to "ok") // true
     *
     * 支持括号
     * evaluator.evaluate("(5 > 3 || 2 < 1) && 'a' != 'b'") // true
     *
     * 必须使用空格分隔元素
     * ✅ 正确：var > 5 && var < 10
     * ❌ 错误：var>5&&var<10
     *
     * 字符串必须加引号
     * ✅ 正确：name == 'John'
     * ❌ 错误：name == John
     */
    val evaluator by lazy { ConditionEvaluator() }

    /**
     *  固定算数表达式计算器
     *  相比之下，这个计算器更适合计算一些固定公式
     *  如果自己处理变量的解析则不如变量算数表达式计算器
     *
     *  fixedCalculator.evaluate("1 + 2 * 3") => 7
     */
    val fixedCalculator by lazy { FixedCalculator() }


    val variableCalculator by lazy { VariableCalculator() }


    val itemMatch by lazy { ItemMatch() }

}
