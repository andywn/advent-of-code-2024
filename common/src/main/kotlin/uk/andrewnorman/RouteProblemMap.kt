package uk.andrewnorman

import uk.andrewnorman.Direction.LEFT
import uk.andrewnorman.Direction.RIGHT
import uk.andrewnorman.Direction.UP
import kotlin.collections.contains
import kotlin.collections.mutableMapOf

class RouteProblemMap(val end: Coords, val obstacles: Set<Coords>, val lengthX: Int, val lengthY: Int, val stepCost: Int, val turnCost: Int) {

    var pathCosts = mutableMapOf<Coords, Int>()

    val costChecker = { add: Int, value: Int -> if (value < 0) { -1 } else { value + add }}

    fun findShortestPath(start: Coords, direction: Direction, allRoutes: Boolean): ShortestPathResult {
        pathCosts = mutableMapOf<Coords, Int>()
        return findNextStep(start, direction, 0, allRoutes)
    }

    fun findNextStep(location: Coords, direction: Direction, costSoFar: Int, allRoutes: Boolean): ShortestPathResult {
        if (obstacles.contains(location)) {
            return ShortestPathResult(-1, emptySet())
        } else if (location.x >= lengthX || location.y >= lengthY || location.x < 0 || location.y < 0) {
            return ShortestPathResult(-1, emptySet())
        } else if (location == end) {
            return ShortestPathResult(0, setOf(location))
        } else if (pathCosts.contains(location) && pathCosts.get(location)!! < (costSoFar - turnCost)) {
            return ShortestPathResult(-1, emptySet())
        } else if (!allRoutes && pathCosts.contains(location) && pathCosts.get(location)!! == (costSoFar - turnCost)) {
            return ShortestPathResult(-1, emptySet())
        }
        if (!pathCosts.contains(location) || pathCosts.get(location)!! > costSoFar) {
            pathCosts.put(location, costSoFar)
        }

        val sameDirection = findNextStep(direction.move(location), direction, costSoFar + 1, allRoutes)
        if (turnCost > stepCost && sameDirection.shortestCost >= 0 && sameDirection.shortestCost < turnCost + stepCost) {
            // Go cheapest way first
            val sameDirSet = sameDirection.allShortestPathSteps.toMutableSet()
            sameDirSet.add(location)
            return ShortestPathResult(sameDirection.shortestCost + stepCost, sameDirSet)
        }
        val left = findNextStep(direction.turnLeft().move(location), direction.turnLeft(), costSoFar + turnCost + stepCost, allRoutes)
        val right = findNextStep(direction.turnRight().move(location), direction.turnRight(), costSoFar + turnCost + stepCost, allRoutes)
        val costs = mapOf(
            Pair(UP, costChecker.invoke(stepCost, sameDirection.shortestCost)),
            Pair(LEFT, costChecker.invoke(turnCost + stepCost, left.shortestCost)),
            Pair(RIGHT, costChecker.invoke(turnCost + stepCost, right.shortestCost))
        )
        if (costs.any { it.value > 0 }) {
            val lowestCost = costs.values.filter { it > 0 }.sorted().first()
            val allCoords = costs.filter { it.value == lowestCost }
                .flatMap {
                    when(it.key) {
                        (UP) -> sameDirection.allShortestPathSteps
                        (LEFT) -> left.allShortestPathSteps
                        else -> right.allShortestPathSteps
                    }
                }.toMutableSet()
            allCoords.add(location)
            return ShortestPathResult(lowestCost, allCoords)
        } else {
            return ShortestPathResult(-1, emptySet())
        }
    }
}

data class ShortestPathResult(val shortestCost: Int, val allShortestPathSteps: Set<Coords>)