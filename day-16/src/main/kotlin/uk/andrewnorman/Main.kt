package uk.andrewnorman

import java.io.File

import uk.andrewnorman.Direction.*

fun main() {
    val lines = File("day-16/src/main/resources/input.txt").bufferedReader().readLines()

//    val lines = listOf("###############",
//            "#.......#....E#",
//            "#.#.###.#.###.#",
//            "#.....#.#...#.#",
//            "#.###.#####.#.#",
//            "#.#.#.......#.#",
//            "#.#.#####.###.#",
//            "#...........#.#",
//            "###.#.#####.#.#",
//            "#...#.....#.#.#",
//            "#.#.#.###.#.#.#",
//            "#.....#...#.#.#",
//            "#.###.#.#.#.#.#",
//            "#S..#.....#...#",
//            "###############")

    val result = Maze(lines).findShortestPath()
    println("Cost: ${result.shortestCost}, All possible steps count: ${result.pathOrder.toSet().size}")
}

class Maze(val mazeMap: List<String>) {

    fun findShortestPath(): ShortestPathResult {
        val start = Coords(
            mazeMap.find{ it.contains("S") }!!.indexOf('S'),
            mazeMap.indexOfFirst{ it.contains("S") })
        val end = Coords(
            mazeMap.find{ it.contains("E") }!!.indexOf('E'),
            mazeMap.indexOfFirst{ it.contains("E") })

        // Fix this up later...
        val obstacles = PlaneRange(mazeMap[0].length, mazeMap.size)
            .filter { mazeMap[it.y][it.x] == '#' }
            .toSet()

        return RouteProblemMap(end, obstacles, mazeMap[0].length, mazeMap.size, 1, 1000).findShortestPath(start, RIGHT, true)
    }
}