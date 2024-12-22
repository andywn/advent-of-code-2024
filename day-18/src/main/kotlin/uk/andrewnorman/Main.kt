package uk.andrewnorman

import java.io.File

fun main() {
    val lines = File("day-18/src/main/resources/input.txt").bufferedReader().readLines()

//    val lines = listOf("5,4",
//            "4,2",
//            "4,5",
//            "3,0",
//            "2,1",
//            "6,3",
//            "2,4",
//            "1,5",
//            "0,6",
//            "3,3",
//            "2,6",
//            "5,1",
//            "1,2",
//            "5,5",
//            "2,5",
//            "6,5",
//            "1,4",
//            "0,4",
//            "6,4",
//            "1,1",
//            "6,1",
//            "1,0",
//            "0,5",
//            "1,6",
//            "2,0")

    val coords = Regex("(\\d+),(\\d+)")
    val obstacles = IntRange(0, 1023)
        .map { lines.get(it) }
        .map {
            val groups = coords.find(it)!!.groups
            Coords(groups[1]!!.value.toInt(), groups[2]!!.value.toInt())
        }.toSet()


    val result = RouteProblemMap(Coords(70, 70), obstacles, 71, 71, 1, 0).findShortestPath(Coords(0, 0), Direction.DOWN, false)
    //val result = RouteProblemMap(Coords(6, 6), obstacles, 7, 7, 1, 0).findShortestPath(Coords(0, 0), Direction.DOWN)

    println(result.shortestCost)
}

