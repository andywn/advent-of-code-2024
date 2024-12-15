package uk.andrewnorman

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong


fun main() {
    val lines = File("day-11/src/main/resources/input.txt").bufferedReader().readLines().get(0).split(" ")

    //val lines = listOf("125 17").get(0).split(" ")

    println(StoneStateMachine(lines).countStonesParallel(75))
}

class StoneStateMachine(val stoneState: List<String>) {

    val lru = ConcurrentHashMap<String, Long>()

    fun countStonesParallel(states: Int): Long {
        val counter = AtomicLong()
        runBlocking {
            coroutineScope {
                repeat(stoneState.size) { it ->
                    async(Dispatchers.Default) {
                        counter.addAndGet(countStones(states, stoneState.get(it)))
                    }
                }
            }
        }
        return counter.toLong()
    }

    fun countStones(countToGo: Int, stoneVal: String): Long {
        var result = getFromLru(countToGo, stoneVal)
        if (result == -1L) {
            if (countToGo == 0) {
                return 1L
            } else if (stoneVal == "0") {
                result = countStones(countToGo-1, "1")
            } else if (stoneVal.length % 2 == 0) {
                result = stoneVal.chunked(stoneVal.length/2)
                    .map{ countStones(countToGo-1, it.toLong().toString()) }.sum()
            } else {
                result = countStones(countToGo - 1, (stoneVal.toLong() * 2024).toString())
            }
            addToLru(countToGo, stoneVal, result)
        }
        return result
    }

    fun getFromLru(countToGo: Int, stoneVal: String):Long {
        val key = "$countToGo:$stoneVal"
        return lru.getOrDefault(key, -1)
    }

    fun addToLru(countToGo: Int, stoneVal: String, result: Long) {
        lru.putIfAbsent("$countToGo:$stoneVal", result)
    }

}