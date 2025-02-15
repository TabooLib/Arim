package top.maplex.arim.tools.variablecalculator.impl

import top.maplex.arim.tools.variablecalculator.Node

data class ValueNode(
    val value: Double
) : Node {
    override fun evaluate(variableMap: Map<String, Double>): Double {
        return value
    }
}
