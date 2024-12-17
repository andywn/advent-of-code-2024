package uk.andrewnorman

import java.io.File

fun main() {
    val lines = File("day-14/src/main/resources/input.txt").bufferedReader().readLines()

//    val lines = listOf("p=0,4 v=3,-3",
//            "p=6,3 v=-1,-3",
//            "p=10,3 v=-1,2",
//            "p=2,0 v=2,-1",
//            "p=0,0 v=1,3",
//            "p=3,0 v=-2,-2",
//            "p=7,6 v=-1,-3",
//            "p=3,0 v=-1,-2",
//            "p=9,3 v=2,3",
//            "p=7,3 v=-1,2",
//            "p=2,4 v=2,-3",
//            "p=9,5 v=-3,-3")

    //val lines = listOf("p=2,4 v=2,-3")

    var q1 = 0
    var q2 = 0
    var q3 = 0
    var q4 = 0

    lines.map { it -> Robot(it, 100, 102).process(100) }
        .forEach{ it -> when(it) {
            (1) -> q1++
            (2) -> q2++
            (3) -> q3++
            (4) -> q4++
        } }
    println("$q1 $q2 $q3 $q4")
    println(q1 * q2 * q3 * q4)
}

class Robot(val line: String, val maxX: Int, val maxY: Int) {

    val regex = Regex("p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)")

    fun process(times: Int): Int {
        val groups = regex.find(line)!!.groups
        val startingCoords = Coords(groups[1]!!.value.toInt(), groups[2]!!.value.toInt())
        val velocity = Coords(groups[3]!!.value.toInt(), groups[4]!!.value.toInt())
        return startingCoords.addCoord(velocity.multiply(times)).wrap(maxX, maxY).quadrant(maxX, maxY)
    }

}

