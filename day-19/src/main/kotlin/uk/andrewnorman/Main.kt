package uk.andrewnorman

import java.io.File

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

}

class TowelSorted(val towelStr:String) {

    val towels = towelStr.split(", ").toSet()
    val maxPatternLength = towels.map { it.length }.max()
    val cache = mutableMapOf<String, Boolean>()

    fun matchTowels(lines: List<String>): Long {
        val patterns = Regex("^[wubrg]+$")
        return lines.parallelStream()
            .filter { patterns.matches(it) }
            .filter { findNextTowel(it) }
            .count()

    }

    fun findNextTowel(design: String): Boolean {
        if (design.length == 0) {
            return true
        } else if (cache.contains(design)) {
            return cache.getOrDefault(design, false)
        }
        for (i in Math. min(design.length, maxPatternLength) downTo 1) {
            if (towels.contains(design.subSequence(0, i))) {
                if (findNextTowel(design.substring(i))) {
                    cache.put(design, true)
                    return true
                }
            }
        }
        cache.put(design, false)
        return false
    }



}