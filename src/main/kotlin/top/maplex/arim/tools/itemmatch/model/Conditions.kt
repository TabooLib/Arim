package top.maplex.arim.tools.itemmatch.model

sealed class MatchCondition {
    data class StringCondition(
        val operation: StringOperation,
        val values: List<String>
    ) : MatchCondition()

    data class NumberCondition(
        val operator: NumberOperator,
        val value: Int
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
