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

    println(Maze(lines).findShortestPath())
}

class Maze(val mazeMap: List<String>) {

    val costChecker = { add: Int, value: Int -> if (value < 0) { -1 } else { value + add }}

    val costs = mutableMapOf<String, Int>()

    fun findShortestPath(): Pair<Int, Int> {
        val start = Coords(
            mazeMap.find{ it.contains("S") }!!.indexOf('S'),
            mazeMap.indexOfFirst{ it.contains("S") })
        val path = findNextStep(start, RIGHT, 0)
        return Pair(path.first, path.second.size)
    }

    fun findNextStep(location: Coords, direction: Direction, costSoFar: Int): Pair<Int, Set<String>> {
        if (mazeMap[location.y][location.x] == '#') {
            return Pair(-1, emptySet())
        } else if (mazeMap[location.y][location.x] == 'E') {
            return Pair(0, setOf(location.toString()))
        } else if (costs.contains(location.toString()) && costs.get(location.toString())!! < (costSoFar - 1000)) {
            // Hacky "-1000" here, because there;s a chance you might get to this location, but be in the "right"
            // direction.
            return Pair(-1, emptySet())
        }
        if (!costs.contains(location.toString()) || costs.get(location.toString())!! > costSoFar) {
            costs.put(location.toString(), costSoFar)
        }
        // Go cheapest way first
        val sameDirection = findNextStep(direction.move(location), direction, costSoFar + 1)
        if (sameDirection.first >= 0 && sameDirection.first < 1001) {
            val sameDirSet = sameDirection.second.toMutableSet()
            sameDirSet.add(location.toString())
            return Pair(sameDirection.first + 1, sameDirSet)
        }
        val left = findNextStep(direction.turnLeft().move(location), direction.turnLeft(), costSoFar + 1001)
        val right = findNextStep(direction.turnRight().move(location), direction.turnRight(), costSoFar + 1001)
        val costs = mapOf(
            Pair(UP, costChecker.invoke(1, sameDirection.first)),
            Pair(LEFT, costChecker.invoke(1001, left.first)),
            Pair(RIGHT, costChecker.invoke(1001, right.first))
        )
        if (costs.any { it.value > 0 }) {
            val lowestCost = costs.values.filter { it > 0 }.sorted().first()
            val allCoords = costs.filter { it.value == lowestCost }
                .flatMap {
                    when(it.key) {
                        (UP) -> sameDirection.second
                        (LEFT) -> left.second
                        else -> right.second
                    }
                }.toMutableSet()
            allCoords.add(location.toString())
            return Pair(lowestCost, allCoords)
        } else {
            return Pair(-1, emptySet())
        }
    }
}