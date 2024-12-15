package uk.andrewnorman.uk.andrewnorman

import java.io.File
import java.util.stream.IntStream
import java.util.stream.Stream
import kotlin.io.bufferedReader
import kotlin.io.readLines
import kotlin.math.abs


fun main() {
    val idLists = IdListPair()
    println(idLists.getDiff())
    println(idLists.getSimilarityScore())
}

class IdListPair() {

    val intPairParser = Regex("(\\d+)\\s+(\\d+)")
    val firstColumn: List<Int>
    val secondColumn: List<Int>
    val secondColumnValueCount: Map<Int, Int>

    init {
        val lines = File("day-1/src/main/resources/input.txt")?.bufferedReader()?.readLines()

        firstColumn = lines!!.stream()
            .map{ it -> intPairParser.matchAt(it, 0)!!.groups[1]!!.value.toInt()}
            .sorted()
            .toList()

        secondColumn = lines!!.stream()
            .map{ it -> intPairParser.matchAt(it, 0)!!.groups[2]!!.value.toInt()}
            .sorted()
            .toList()

        secondColumnValueCount = secondColumn.groupingBy { it }.eachCount()
    }

    fun getDiff(): Int {
        return IntRange(0, firstColumn.size-1)
            .map { it -> abs(firstColumn.get(it) - secondColumn.get(it)) }
            .sum()
    }

    fun getSimilarityScore(): Int {
        return IntRange(0, firstColumn.size-1)
            .map{ it -> firstColumn.get(it)}
            .distinct()
            .filter{ it -> secondColumnValueCount.contains(it)}
            .map{ it -> it * secondColumnValueCount.get(it)!!.toInt() }
            .sum()
    }
}