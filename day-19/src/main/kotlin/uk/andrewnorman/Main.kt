package uk.andrewnorman

import java.io.File
import java.util.stream.Collectors
import kotlin.concurrent.getOrSet

fun main() {
    val lines = File("day-19/src/main/resources/input.txt").bufferedReader().readLines()

//    val lines = listOf("r, wr, b, g, bwu, rb, gb, br",
//            "",
//            "brwrr",
//            "bggr",
//            "gbbr",
//            "rrbgbr",
//            "ubwu",
//            "bwurrg",
//            "brgr",
//            "bbrgwb")


    println(TowelSorted(lines.get(0)).matchTowels(lines))
    println(TowelSorted(lines.get(0)).matchAllTowels(lines))

}

class TowelSorted(towelStr:String) {

    val towels = towelStr.split(", ").toSet()
    val maxPatternLength = towels.map { it.length }.max()
    val cache = ThreadLocal<MutableMap<String, Long>>()

    fun matchAllTowels(lines: List<String>): Long {
        val patterns = Regex("^[wubrg]+$")
        return lines.parallelStream()
            .filter { patterns.matches(it) }
            .collect(Collectors.summingLong( { findNextTowel(it).toLong()} ))
    }

    fun matchTowels(lines: List<String>): Long {
        val patterns = Regex("^[wubrg]+$")
        return lines.parallelStream()
            .filter { patterns.matches(it) }
            .filter{ findNextTowel(it) > 0 }
            .count()
    }

    fun findNextTowel(design: String): Long {
        val c = cache.getOrSet { mutableMapOf<String, Long>() }
        if (design.length == 0) {
            return 1
        } else if (c.contains(design)) {
            return c.getOrDefault(design, 0)
        }
        var sum = 0L
        for (i in Math. min(design.length, maxPatternLength) downTo 1) {
            if (towels.contains(design.subSequence(0, i))) {
                sum += findNextTowel(design.substring(i))
            }
        }
        c.putIfAbsent(design, sum)
        return sum
    }
}