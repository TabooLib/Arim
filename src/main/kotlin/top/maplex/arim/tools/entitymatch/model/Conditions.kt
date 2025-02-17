package top.maplex.arim.tools.entitymatch.model

sealed class MatchCondition(
    var modifiers: List<String> = emptyList()
) {
    data class StringCondition(
        val operation: StringOperation,
        var values: List<String>
    ) : MatchCondition()

    data class NumberCondition(
        val operator: NumberOperator,
        val value: Double
    ) : MatchCondition() {
        var tag: String = ""
    }

    data class CompoundCondition(
        val type: CompoundType,
        val conditions: List<MatchCondition>
    ) : MatchCondition()
}

enum class StringOperation { EXACT, CONTAINS, REGEX, STARTS_WITH, ENDS_WITH }
enum class NumberOperator { EQUAL, GREATER, LESS, GREATER_EQUAL, LESS_EQUAL }
enum class CompoundType { ANY, ALL, NONE }
