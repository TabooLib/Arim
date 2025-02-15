package top.maplex.arim.tools.variablecalculator

class NodeContext(
    var node: Node
) {
    fun evaluate(variable: Map<String, Double>): Double {
        return node.evaluate(variable)
    }

    fun evaluate(vararg variables: Pair<String, Double>): Double {
        return evaluate(variables.toMap())
    }
}
