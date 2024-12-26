package uk.andrewnorman

import java.io.File
import kotlin.io.bufferedReader
import kotlin.io.readLines

fun main() {
    val lines = File("day-20/src/main/resources/input.txt").bufferedReader().readLines()

//    val lines = listOf("###############",
//            "#...#...#.....#",
//            "#.#.#.#.#.###.#",
//            "#S#...#.#.#...#",
//            "#######.#.#.###",
//            "#######.#.#...#",
//            "#######.#.###.#",
//            "###..E#...#...#",
//            "###.#######.###",
//            "#...###...#...#",
//            "#.#####.#.###.#",
//            "#.#...#.#.#...#",
//            "#.#.#.#.#.#.###",
//            "#...#...#...###",
//            "###############")

    val result = Maze(lines).process()
    println(result.filter{ it >= 100 }.count())

}

class Maze(val lines: List<String>){

    fun process(): List<Int> {
        val obstacles = PlaneRange(lines.size, lines[0].length)
            .filter { lines[it.y][it.x] == '#' }
            .toSet()

        val start = PlaneRange(lines.size, lines[0].length)
            .filter { lines[it.y][it.x] == 'S' }
            .first()

        val end = PlaneRange(lines.size, lines[0].length)
            .filter { lines[it.y][it.x] == 'E' }
            .first()

        val map = RouteProblemMap(end, obstacles, lines[0].length, lines.size, 1, 0)
        val route = map.findSingleShortestPathDjikstra(start).pathOrder.reversed()

        val routeMap = IntRange(0, route.size-1)
            .associateBy { it -> route[it] }
        return IntRange(0, route.size-1).flatMap { it ->
            val allJumps = mutableListOf<Int>()
            val coords = route[it]
            for (dir in Direction.values()) {
                val x = dir.move(dir.move(coords))
                if (routeMap.contains(x) && routeMap.get(x)!! > it + 2) {
                    allJumps.add(routeMap.get(x)!! - it - 2)
                }
            }
            allJumps
        }.toList()
    }

}