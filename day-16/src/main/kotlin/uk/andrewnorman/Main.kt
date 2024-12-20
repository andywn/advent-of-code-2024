package uk.andrewnorman

import java.io.File

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

    println(Maze(lines).findShortestPath())
}

class Maze(val mazeMap: List<String>) {

    val costChecker = { add: Int, value: Int -> if (value < 0) { -1 } else { value + add }}

    val costs = mutableMapOf<String, Int>()

    fun findShortestPath(): Int {
        val start = Coords(
            mazeMap.find{ it.contains("S") }!!.indexOf('S'),
            mazeMap.indexOfFirst{ it.contains("S") })
        return findNextStep(start, Direction.RIGHT, 0)
    }

    fun findNextStep(location: Coords, direction: Direction, costSoFar: Int): Int {
        if (mazeMap[location.y][location.x] == '#') {
            return -1
        } else if (mazeMap[location.y][location.x] == 'E') {
            return 0
        } else if (costs.contains(location.toString()) && costs.get(location.toString())!! < (costSoFar - 1000)) {
            // Hacky "-1000" here, because there;s a chance you might get to this location, but be in the "right"
            // direction.
            return -1
        }
        if (!costs.contains(location.toString()) || costs.get(location.toString())!! > costSoFar) {
            costs.put(location.toString(), costSoFar)
        }
        // Go cheapest way first
        val sameDirection = findNextStep(direction.move(location), direction, costSoFar + 1)
        if (sameDirection >= 0 && sameDirection < 1001) {
            return sameDirection + 1
        }
        val costs = listOf(
            costChecker.invoke(1, sameDirection),
            costChecker.invoke(1001, findNextStep(direction.turnLeft().move(location), direction.turnLeft(), costSoFar + 1001)),
            costChecker.invoke(1001, findNextStep(direction.turnRight().move(location), direction.turnRight(), costSoFar + 1001))
        )
        if (costs.any { it > 0 }) {
            return costs.filter { it > 0 }.sorted().first()
        } else {
            return -1
        }
    }
}