package uk.andrewnorman

import kotlin.math.abs

class PlaneRange: ArrayList<Coords> {
    constructor(lengthX: Int, lengthY: Int): super(lengthX * lengthY) {
        this.addAll(
            IntRange(0, lengthX - 1).flatMap { x -> IntRange(0, lengthY - 1).map { y -> Coords(x, y) } }.toList()
        )
    }

    fun centreAndFilterDistance(distance: Int): List<Coords> {
        return this.map { Coords(it.x - distance, it.y - distance) }
            .filter { abs(it.x) + abs(it.y) <= distance }
    }

}