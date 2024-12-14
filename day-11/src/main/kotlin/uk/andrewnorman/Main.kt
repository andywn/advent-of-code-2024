package uk.andrewnorman

import java.io.File


fun main() {
    val lines = File("src/main/resources/input.txt").bufferedReader().readLines()

    //val lines = listOf("125 17")

    println(StoneStateMachine(lines).countStones(25))
}

class StoneStateMachine {

    var stoneState:List<Long>

    constructor(lines: List<String>) {
        this.stoneState = lines.get(0).split(" ").map { it -> it.toLong() }.toMutableList()
    }

    fun countStones(states: Int): Int {
        for (i in 1..states) {
            val newStoneState = mutableListOf<Long>()
            for (j in 0..stoneState.size-1) {
                val str = stoneState.get(j).toString()
                if (stoneState[j] == 0L) {
                    newStoneState.add(1)
                } else if (str.length % 2 == 0) {
                    str.chunked(str.length/2)
                        .forEach{ newStoneState.add(it.toLong()) }
                } else {
                    newStoneState.add(stoneState.get(j) * 2024)
                }
            }
            stoneState = newStoneState
        }
        return stoneState.size
    }

}