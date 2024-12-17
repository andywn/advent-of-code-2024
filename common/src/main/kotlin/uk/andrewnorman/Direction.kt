package uk.andrewnorman


enum class Direction(val character: Char, val move: (coords: Coords) -> Coords) {
    UP('^', { coords: Coords -> Coords(coords.x, coords.y-1) }),
    RIGHT('>', { coords: Coords -> Coords(coords.x+1, coords.y) }),
    DOWN('v', { coords: Coords -> Coords(coords.x, coords.y+1) }),
    LEFT('<', { coords: Coords -> Coords(coords.x-1, coords.y) });

    fun turnRight(): Direction {
        return when(this) {
            (UP) -> RIGHT
            (RIGHT) -> DOWN
            (DOWN) -> LEFT
            else -> UP
        }
    }

    fun turnLeft(): Direction {
        return when(this) {
            (UP) -> LEFT
            (RIGHT) -> UP
            (DOWN) -> RIGHT
            else -> DOWN
        }
    }

    companion object {
        fun findDirection(dir: Char): Direction {
            return entries.first { it.character == dir}
        }
    }
}