package uk.andrewnorman

import java.io.File


fun main() {
    val lines = File("src/main/resources/input.txt").bufferedReader().readLines()

    //val lines = listOf("190: 10 19", "3267: 81 40 27")
    //val lines = listOf("7740: 6 8 6 15")

    println(lines.parallelStream()
        .map{ line -> Equation(line) }
        .map{ equation -> if (equation.process(0, 0, Equation.Operator.START)) { equation.testValue } else { 0 }}
        .toList().sum())
}

class Equation {

    val testValue: Long
    val numbers: List<Long>

    constructor(line: String) {
        val sections = line.split(":")
        testValue = sections[0].toLong()
        numbers = Regex(" \\d+").findAll(sections[1])
            .map { it -> it.value.toString().trim().toLong() }
            .toList()
    }

    fun process(sum: Long, index: Int, previousOperator: Operator): Boolean {
        if (index >= numbers.size) {
            return sum == testValue
        } else if (index == 0) {
            return process(numbers[index], 1, previousOperator)
        }
        // Test plus
        val plus = when {
            (numbers[index] + sum <= testValue) -> process(numbers[index] + sum, index+1, Operator.ADD)
            else -> false
        }
        val multiply = when {
            (numbers[index] * sum <= testValue) -> process(numbers[index] * sum, index+1, Operator.MULTIPLY)
            else -> false
        }
        val concatVal = "${numbers[index-1]}${numbers[index]}".toLong()
        val sumWithConcat = when(previousOperator) {
            (Operator.START) -> concatVal
            (Operator.MULTIPLY) -> sum - (numbers[index-2] * numbers[index-1]) + (numbers[index-2] * concatVal)
            (Operator.ADD) -> sum - numbers[index-1] + concatVal
            else -> -1 // assume we can't have concurrent concats. 1 || 2 || 3 != 123.
        }

        val concat = when {
            (sumWithConcat < 0) -> false
            (sumWithConcat <= testValue) -> process(sumWithConcat, index+1, Operator.CONCAT)
            else -> false
        }

        return plus || multiply || concat
    }

    enum class Operator {
        MULTIPLY, ADD, CONCAT, START
    }
}
