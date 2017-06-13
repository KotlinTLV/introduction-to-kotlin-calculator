import java.util.*


interface Calculator {
    sealed class Expr {
        class Operand(val number: Int) : Expr()
        class BinaryOperator(val priority: Int, val expression: (Int, Int) -> Int) : Expr() {
            fun apply(constant1: Operand, constant2: Operand): Operand =
                    Operand(expression.invoke(constant1.number, constant2.number))
        }
    }

    class Impl {
        private val operandStack: Stack<Expr.Operand> = Stack()
        private val operationStack: Stack<Expr.BinaryOperator> = Stack()
        private val operandMap = mapOf(
                "+" to Expr.BinaryOperator(1, { x, y -> x + y }),
                "-" to Expr.BinaryOperator(2, { x, y -> x - y }),
                "*" to Expr.BinaryOperator(3, { x, y -> x * y }),
                "/" to Expr.BinaryOperator(4, { x, y -> x / y })
        )

        private fun getOperatorForExpression(expression: String): Expr.BinaryOperator {
            if (operandMap.containsKey(expression)) {
                return operandMap[expression]!!
            }
            throw IllegalArgumentException("Not an operator!")
        }

        private fun updateStringBuilderIfNecessary(builder: StringBuilder): StringBuilder {
            var builderCopy = builder
            if (builderCopy.isNotEmpty()) {
                operandStack.push(Expr.Operand(builderCopy.toInteger()))
                builderCopy = StringBuilder()
            }
            return builderCopy
        }

        private fun shouldContinueLoop(operator: Expr.BinaryOperator) = !operationStack.empty() &&
                operator.priority <= operationStack.peek().priority

        fun calculate(expression: String): Int {
            var builder: StringBuilder = StringBuilder()
            for (i in 0 until expression.length) {
                val current = expression[i].toString()
                when (current) {
                    in operandMap.keys -> {
                        builder = updateStringBuilderIfNecessary(builder)
                        val operator: Expr.BinaryOperator = getOperatorForExpression(current)
                        if (!operationStack.empty()) {
                            while (shouldContinueLoop(operator)) {
                                operandStack.push(operator.apply(operandStack.pop(), operandStack.pop()))
                            }
                        }
                    }

                    in "0".."9" -> {
                        builder.append(current)
                    }

                    else -> {
                        builder = updateStringBuilderIfNecessary(builder)
                    }
                }
            }
            return operandStack.pop().number
        }
    }
}