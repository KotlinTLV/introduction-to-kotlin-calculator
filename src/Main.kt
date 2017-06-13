fun main(args: Array<String>) {

    val calculator = Calculator.Impl()
    println("Welcome to the Kotlin Example: ")
    println("Please enter an expression: ")
    val expression = readLine()
    expression?.let {
        if (expression.isEmpty()) {
            println("Nothing was entered")
        } else if (expression.contains(Regex("[a-zA-Z]"))) {
            println("Expression may not contain alphanumeric characters")
        } else {
            println("Result is ${calculator.calculate(expression)}")
        }
    }
}