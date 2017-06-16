import java.util.*

interface Calculator {
    fun calculate(expression: String): Int

    class Impl:Calculator {
        private val operandStack: Stack<Expr.Operand> = Stack()
        private val operationStack: Stack<Expr.BinaryOperator> = Stack()

        private fun getOperatorForExpression(expression: String): Expr.BinaryOperator {
            if (Expr.isExpressionOperator(expression)) {
                return Expr.BinaryOperator(expression)
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

        private fun translate(current: String) =
            if (Expr.isExpressionOperator(current)) {
                getOperatorForExpression(current)
            } else {
                Expr.Operand(current)
            }

        override fun calculate(expression: String): Int {
            var builder: StringBuilder = StringBuilder()
            for (i in 0 until expression.length) {
                val current = expression[i].toString()
                if (current.isNullOrBlank()) continue
                val translatedAsExpr = translate(current)
                when (translatedAsExpr) {
                     is Expr.BinaryOperator -> {
                        builder = updateStringBuilderIfNecessary(builder)
                        if (!operationStack.empty()) {
                            while (shouldContinueLoop(translatedAsExpr)) {
                                operandStack.push(operationStack.pop()
                                        .apply(operandStack.pop(), operandStack.pop()))
                            }
                        }
                        operationStack.push(translatedAsExpr)
                    }

                    is Expr.Operand -> {
                        builder.append(translatedAsExpr.number)
                    }
                }
            }
            operandStack.push(Expr.Operand(builder.toInteger()))
            if (operationStack.size == 1) {
                // postfix += stack.pop().getSign();
                operandStack.push(operationStack.pop().apply(operandStack.pop(), operandStack.pop()))
            }

            return operandStack.pop().number
        }
    }
}