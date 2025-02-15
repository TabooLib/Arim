package top.maplex.arim.tools.variablecalculator.impl

import top.maplex.arim.tools.variablecalculator.Node

data class DynamicNode(
    val variable: String? = null,
) : Node {
    override fun evaluate(variableMap: Map<String, Double>): Double {
        return variableMap.getOrDefault(variable, 0.0)
    }
}
