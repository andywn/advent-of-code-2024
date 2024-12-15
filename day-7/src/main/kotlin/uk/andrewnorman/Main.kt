package uk.andrewnorman

import java.io.File


fun main() {
    val lines = File("day-7/src/main/resources/input.txt").bufferedReader().readLines()

    println(lines.parallelStream()
        .map{ line -> Equation(line) }
        .map{ equation -> if (equation.process(0, 0)) { equation.testValue } else { 0 }}
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

    fun process(sum: Long, index: Int): Boolean {
        if (index >= numbers.size) {
            return sum == testValue
        } else if (index == 0) {
            return process(numbers[index], 1)
        }
        // Test plus
        return when {
            (numbers[index] + sum <= testValue) -> process(numbers[index] + sum, index+1)
            else -> false
        } || when {
            (numbers[index] * sum <= testValue) -> process(numbers[index] * sum, index+1)
            else -> false
        } || when {
            ("${sum}${numbers[index]}".toLong() <= testValue) -> process("${sum}${numbers[index]}".toLong(), index+1)
            else -> false
        }
    }
}
