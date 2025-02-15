package top.maplex.arim.tools.variablecalculator.impl

import top.maplex.arim.tools.variablecalculator.Node

data class SubtractionNode(
    val left: Node,
    val right: Node
) : Node {
    override fun evaluate(variableMap: Map<String, Double>): Double {
        return left.evaluate(variableMap) - right.evaluate(variableMap)
    }
}
