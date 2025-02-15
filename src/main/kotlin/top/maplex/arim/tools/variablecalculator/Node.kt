package top.maplex.arim.tools.variablecalculator

interface Node {
    fun evaluate(variableMap: Map<String, Double>): Double
}
