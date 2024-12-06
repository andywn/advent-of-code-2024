package uk.andrewnorman

import java.io.File

fun main() {
    val lines = File("src/main/resources/input.txt")!!.bufferedReader()!!.readLines()
    val rules = Rules(lines)
    val regex = Regex("^(\\d+,)+(\\d+)")
    println(lines.stream()
        .filter { regex.matches(it) }
        .map{ PrintOrder(it) }
        .map{ it.checkRules(rules) }
        .toList().sum())
}

class Rules(input: List<String>) {

    val rules: Map<Int, List<Int>>

    init {
        val regex = Regex("^(\\d+)\\|(\\d+)")
        rules = input.stream()
            .filter{ it -> regex.matches(it) }
            .map{ it ->
                val groups = regex.find(it)!!.groups
                Pair(groups[1]!!.value.toInt(),
                    groups[2]!!.value.toInt())
            }
            .toList()
            .groupBy({ it.first }, {it.second})
    }
}

class PrintOrder(printOrder: String) {

    val order: List<Int>
    val indexes: Map<Int, Int>

    init {
        order = printOrder.split(",").stream()
            .map{ it.toInt() }
            .toList()

        indexes = IntRange(0, order.size-1).
                associateBy { order.get(it) }
    }

    fun checkRules(rules: Rules): Int {
        for(pageOrder in 0..order.size-1) {
            val rulesMatch = rules.rules[order[pageOrder]]?.stream()?.map { it -> checkSingleRule(it, pageOrder)}?.allMatch{ it } != false
            if (!rulesMatch) {
                return 0
            }
        }
        return order.get((order.size-1)/2)
    }


    fun checkSingleRule(value: Int, after: Int): Boolean {
        if (indexes.contains(value)) {
            if (indexes.get(value)!! < after) {
                return false
            }
        }
        return true
    }
}