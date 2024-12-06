package uk.andrewnorman


class Guard(val area: GuardedArea, val map: List<String>) {

    var guardLocation: GuardLocation

    val positionsVisited: HashSet<String> = HashSet()

    init {
        val coords = IntRange(0, map.size-1).map{
            Pair(map[it].toString().indexOfAny("^<>v".toCharArray()), it)
        }.filter { it.first >= 0 }.get(0)
        val direction = Direction.findDirection(map[coords.second][coords.first])

        this.guardLocation = GuardLocation(coords.first, coords.second, direction, false)
        addToSet(guardLocation)
    }

    fun move(): GuardLocation {
        var direction:Direction = guardLocation.direction
        for(i:Int in 1..4) {
            val nextSpot = direction.move(Pair(guardLocation.x, guardLocation.y))
            val nextspotResult = area.checkLocation(nextSpot)
            when (nextspotResult) {
                (LocationCheck.OK) -> {
                    this.guardLocation = GuardLocation(nextSpot.first, nextSpot.second, direction, false)
                    addToSet(guardLocation)
                    return guardLocation
                }
                (LocationCheck.OUTSIDE_MAP) -> {
                    this.guardLocation = GuardLocation(nextSpot.first, nextSpot.second, direction, true)
                    return guardLocation
                }
                else -> direction = direction.turnRight()
            }
        }
        // We're blocked, there's crates all around. Assume this isn't possible, and just say we're outside the map...
        return guardLocation
    }

    fun addToSet(guardLocation: GuardLocation) {
        val x = guardLocation.x
        val y = guardLocation.y
        positionsVisited.add("$x:$y")
    }

    fun getCount():Int {
        return positionsVisited.size
    }
}

data class GuardLocation(val x: Int, val y: Int, val direction: Direction, val outsideOfMap: Boolean)

enum class Direction(val character: Char, val move: (coords: Pair<Int, Int>) -> Pair<Int, Int>) {
    UP('^', {coords: Pair<Int, Int> -> Pair(coords.first, coords.second-1)}),
    RIGHT('>', {coords: Pair<Int, Int> -> Pair(coords.first+1, coords.second)}),
    DOWN('v', {coords: Pair<Int, Int> -> Pair(coords.first, coords.second+1)}),
    LEFT('<', {coords: Pair<Int, Int> -> Pair(coords.first-1, coords.second)});

    fun turnRight(): Direction {
        return when(this) {
            (UP) -> RIGHT
            (RIGHT) -> DOWN
            (DOWN) -> LEFT
            else -> UP
        }
    }

    companion object {
        fun findDirection(dir: Char): Direction {
            return Direction.values().first { it.character == dir}
        }
    }

}