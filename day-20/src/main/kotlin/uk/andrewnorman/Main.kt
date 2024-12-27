package uk.andrewnorman

import java.io.File
import kotlin.io.bufferedReader
import kotlin.io.readLines
import kotlin.math.abs

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

    val result = Maze(lines).challenge1()
    val result2 = Maze(lines).challenge2()
    println(result.filter{ it >= 100 }.count())
    println(result2.filter{ it >= 100 }.count())

}

class Maze {

    val route: List<Coords>
    val routeMap: Map<Coords, Int>

    constructor(lines: List<String>) {
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
        route = map.findSingleShortestPathDjikstra(start).pathOrder.reversed()

        routeMap = IntRange(0, route.size-1)
            .associateBy { it -> route[it] }
    }

    fun challenge1(): List<Int> {
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

    fun challenge2(): List<Int> {
        return IntRange(0, route.size-1).flatMap { i ->
            PlaneRange(41, 41)
                .centreAndFilterDistance(20)
                .map { it.addCoord(route[i]) }
                .filter { routeMap.contains(it) && routeMap.get(it)!! > i + 50 }
                .map { routeMap.get(it)!! - i -  it.distanceFrom(route[i])}
                .toList()
        }.toList()
    }

}