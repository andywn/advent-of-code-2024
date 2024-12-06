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

    println(lines.stream()
        .filter { regex.matches(it) }
        .map{ PrintOrder(it) }
        .map{ it.reorderAndCheckRules(rules) }
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

    // Could do this with LinkedHashMap, but I think that's overkill for this.
    val order: List<Int> = printOrder.split(",").stream()
        .map{ it.toInt() }
        .toList()

    fun checkRules(rules: Rules, order: List<Int> = this.order) : Int {
        for(pageOrder in 0..order.size-1) {
            val rulesMatch = rules.rules[order[pageOrder]]?.stream()?.map { it -> checkSingleRule(it, pageOrder, order)}?.allMatch{ it } != false
            if (!rulesMatch) {
                return 0
            }
        }
        return order.get((order.size-1)/2)
    }

    /**
     * Basic method of tackling this.
     * Loop through until we find the first rule that breaks. Fix that one rule (i.e. move that value as
     * short a distance as possible). And then start from the start again and check all rules.
     *
     * Alternative solution: we could have:
     * 1. found all rules that are broken.
     * 2. work out all values that need to change.
     * 3. work out the LARGEST they need to move by (i.e. which rule pushes them the furthest).
     * 4. then re-check.
     */
    fun reorderAndCheckRules(rules: Rules): Int {
        if (checkRules(rules) == 0) {
            val reorderedList = order.toMutableList()
            // There's got to be a cleaner way of doing it than this...
            attempts@for (i in 0..100000) {
                var reOrdered = false
                index@for (pageOrder in 0..reorderedList.size - 1) {

                    if (rules.rules[reorderedList.get(pageOrder)]?.stream()?.anyMatch { it ->
                            if (!checkSingleRule(it, pageOrder, reorderedList)) {
                                reorderedList.remove(it)
                                reorderedList.add(pageOrder, it)
                                true
                            } else {
                                false
                            }
                        } == true) {
                        reOrdered = true
                        break@index
                    }
                }
                if (!reOrdered) {
                    break@attempts
                }
            }
            return checkRules(rules, reorderedList)
        } else {
            return 0
        }
    }


    fun checkSingleRule(value: Int, after: Int, orderedList: List<Int> = this.order): Boolean {
        if (orderedList.contains(value)) {
            if (orderedList.indexOf(value)!! < after) {
                return false
            }
        }
        return true
    }
}