package top.maplex.arim

import ink.ptms.um.Mythic
import top.maplex.arim.tools.conditionevaluator.ConditionEvaluator
import top.maplex.arim.tools.entitymatch.EntityMatch
import top.maplex.arim.tools.fixedcalculator.FixedCalculator
import top.maplex.arim.tools.glow.api.IGlow
import top.maplex.arim.tools.itemmatch.ItemMatch
import top.maplex.arim.tools.variablecalculator.VariableCalculator
import top.maplex.arim.tools.weightrandom.WeightRandom

/**
 * Arim工具库
 * 使用文档: https://taboolib.feishu.cn/wiki/PisKwWgRHirVeRkAz9ncDY7Cn2d
 */
object Arim {

    /**
     * 条件表达式求值器
     */
    val evaluator by lazy { ConditionEvaluator() }

    /**
     *  固定算数表达式计算器
     */
    val fixedCalculator by lazy { FixedCalculator() }


    /**
     *  变量计算器
     */
    val variableCalculator by lazy { VariableCalculator() }

    /**
     * 物品匹配工具
     */
    val itemMatch by lazy { ItemMatch() }

    /**
     * 实体匹配工具
     */
    val entityMatch by lazy { EntityMatch() }

    /**
     * 生物&方块发光工具
     * 生物发光:
     * 可用版本: 1.12.2 1.16.5 1.17以上(已测试1.18.2, 1.19.4, 1.20.1, 1.20.4, 1.21.1, 1.21.4)
     * 方块发光
     * 可用版本: 根据不同模式, 见[top.maplex.arim.tools.glow.internal.pojo.BlockGlowMode]
     */
    val glow by lazy { IGlow() }

    /**
     *  文件夹读取器
     *  请使用拓展函数作为入口
     */
    val folderReader = {}

    /**
     *  TabooLib5 风格命令帮助
     *  请使用拓展函数作为入口
     */
    val tabooLegacyStyleCommandHelper = {}

    /**
     *  权重随机工具
     */
    val weightRandom by lazy {  WeightRandom() }

}
