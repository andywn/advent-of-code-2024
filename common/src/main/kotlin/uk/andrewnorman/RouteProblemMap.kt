package uk.andrewnorman

import uk.andrewnorman.Direction.LEFT
import uk.andrewnorman.Direction.RIGHT
import uk.andrewnorman.Direction.UP
import java.util.PriorityQueue
import kotlin.collections.contains
import kotlin.collections.mutableMapOf

class RouteProblemMap(val end: Coords, val obstacles: Set<Coords>, val lengthX: Int, val lengthY: Int, val stepCost: Int, val turnCost: Int) {

    var pathCosts = mutableMapOf<Coords, Int>()
    val visited = mutableSetOf<Coords>()
    val queue: PriorityQueue<Node> = PriorityQueue()
    val costChecker = { add: Int, value: Int -> if (value < 0) { -1 } else { value + add }}

    /**
     * Does not find all paths.
     * Ignores direction costs.
     */
    fun findSingleShortestPathDjikstra(start: Coords): ShortestPathResult {
        addAllNextNodes(Node(start, 0, null))
        var endNode: Node? = null
        while (!queue.isEmpty()) {
            val nextNode = queue.remove()
            if (nextNode.location == end) {
                endNode = nextNode
                break
            }
            addAllNextNodes(nextNode)
        }
        if (endNode != null) {
            val route = mutableListOf<Coords>()
            var node = endNode
            while(node!!.previous != null) {
                route.add(node.location)
                node = node.previous
            }
            route.add(node.location)
            return ShortestPathResult(endNode.stepCount, route)
        }
        return ShortestPathResult(-1, emptyList())
    }

    fun addAllNextNodes(previous: Node) {
        for (dir in Direction.entries) {
            val moved = dir.move(previous.location)
            if (visited.contains(moved)) {
                continue
            } else if (!obstacles.contains(moved) && moved.x >= 0 && moved.x < lengthX && moved.y >= 0 && moved.y < lengthY) {
                visited.add(moved)
                queue.add(Node(moved, previous.stepCount + stepCost, previous))
            }
        }
    }


    fun findShortestPath(start: Coords, direction: Direction, allRoutes: Boolean): ShortestPathResult {
        pathCosts = mutableMapOf<Coords, Int>()
        return findNextStep(start, direction, 0, allRoutes)
    }

    fun findNextStep(location: Coords, direction: Direction, costSoFar: Int, allRoutes: Boolean): ShortestPathResult {
        if (obstacles.contains(location)) {
            return ShortestPathResult(-1, emptyList())
        } else if (location.x >= lengthX || location.y >= lengthY || location.x < 0 || location.y < 0) {
            return ShortestPathResult(-1, emptyList())
        } else if (location == end) {
            return ShortestPathResult(0, listOf(location))
        } else if (pathCosts.contains(location) && pathCosts.get(location)!! < (costSoFar - turnCost)) {
            return ShortestPathResult(-1, emptyList())
        } else if (!allRoutes && pathCosts.contains(location) && pathCosts.get(location)!! == (costSoFar - turnCost)) {
            return ShortestPathResult(-1, emptyList())
        }
        if (!pathCosts.contains(location) || pathCosts.get(location)!! > costSoFar) {
            pathCosts.put(location, costSoFar)
        }

        val sameDirection = findNextStep(direction.move(location), direction, costSoFar + 1, allRoutes)
        if (turnCost > stepCost && sameDirection.shortestCost >= 0 && sameDirection.shortestCost < turnCost + stepCost) {
            // Go cheapest way first
            val sameDirList = sameDirection.pathOrder.toMutableList()
            sameDirList.add(location)
            return ShortestPathResult(sameDirection.shortestCost + stepCost, sameDirList)
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
                        (UP) -> sameDirection.pathOrder
                        (LEFT) -> left.pathOrder
                        else -> right.pathOrder
                    }
                }.toMutableList()
            allCoords.add(location)
            return ShortestPathResult(lowestCost, allCoords)
        } else {
            return ShortestPathResult(-1, emptyList())
        }
    }
}

data class ShortestPathResult(val shortestCost: Int, val pathOrder: List<Coords>)

class Node(val location: Coords, val stepCount: Int, val previous: Node?): Comparable<Node> {
    override fun compareTo(other: Node): Int {
        return this.stepCount.compareTo(other.stepCount)
    }

    override fun toString(): String {
        return "<${location.x}, ${location.y}>, $stepCount, || $previous"
    }
}