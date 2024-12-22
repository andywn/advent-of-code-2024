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
    println("Cost: ${result.shortestCost}, All possible steps count: ${result.allShortestPathSteps.size}")
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
        val obstacles = IntRange(0, mazeMap.size-1)
            .flatMap { y -> IntRange(0, mazeMap[0].length-1).map { x -> Coords(x, y) } }
            .filter { mazeMap[it.y][it.x] == '#' }
            .toSet()

        return RouteProblemMap(end, obstacles, mazeMap[0].length, mazeMap.size, 1, 1000).findShortestPath(start, RIGHT, true)
    }
}