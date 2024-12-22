package uk.andrewnorman

import java.util.Objects
import kotlin.math.floor
import kotlin.math.max

class Coords(val x: Int, val y: Int) {

    fun addCoord(coords: Coords): Coords {
        return Coords(this.x + coords.x, this.y + coords.y)
    }

    fun multiply(multiplier: Int): Coords {
        return Coords(this.x * multiplier, this.y * multiplier)
    }

    fun remainder(xModulus: Int, yModulus: Int): Coords {
        return Coords(this.x % (xModulus+1), this.y % (yModulus+1))
    }

    fun wrap(xMax: Int, yMax: Int): Coords {
        val x = if (this.x < 0) {
            (this.x % (xMax+1)) + (xMax+1)
        } else {
            (this.x % (xMax+1))
        }
        val y = if (this.y < 0) {
            (this.y % (yMax+1)) + (yMax+1)
        } else {
            (this.y % (yMax+1))
        }
        // Do it one last time...
        return Coords(x, y).remainder(xMax, yMax)
    }

    // Indexed from 0.
    fun quadrant(maxX: Int, maxY: Int): Int {
        val qx = (maxX + 1) / 2
        val qy = (maxY + 1) / 2

        val xquadrant = when {
            (this.x < qx) -> 1
            (this.x > (maxX-qx)) -> 2
            else -> -10
        }
        val yquadrant = when {
            (this.y < qy) -> 0
            (this.y > (maxY-qy)) -> 2
            else -> -10
        }
        return max(xquadrant+yquadrant, 0)
    }

    override fun equals(other: Any?): Boolean {
        return (other is Coords && other.x == this.x && other.y == this.y)
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y)
    }

    override fun toString(): String = "<${this.x}, ${this.y}>"
}