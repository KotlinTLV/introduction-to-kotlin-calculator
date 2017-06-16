sealed class Expr {
    companion object {
        fun isExpressionOperator(expression: String): Boolean {
            return expression in operatorMap
        }

        private val operatorMap = mapOf(
                "+" to Expr.BinaryOperator(1, { x, y -> x + y }),
                "-" to Expr.BinaryOperator(2, { x, y -> x - y }),
                "*" to Expr.BinaryOperator(3, { x, y -> x * y }),
                "/" to Expr.BinaryOperator(4, { x, y -> x / y })
        )
    }


    class Operand(val number: Int) : Expr() {
        constructor(number: String) : this(Integer.parseInt(number))
    }

    class BinaryOperator(val priority: Int, val expression: (Int, Int) -> Int) : Expr() {
        constructor(expression: String): this(operatorMap[expression]!!.priority, operatorMap[expression]!!.expression)

        fun apply(constant1: Operand, constant2: Operand): Operand =
                Operand(expression.invoke(constant1.number, constant2.number))
    }
}
