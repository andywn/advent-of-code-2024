package uk.andrewnorman

class GuardedArea (val input: List<String>) {

    val map: List<List<Boolean>> = input.stream().map{
        it.chars().mapToObj{ it -> it == '#'.code }.toList()
    }.toList()

    fun checkLocation(coords: Pair<Int, Int>, direction: Direction, checkObstructionOption: Boolean = true): LocationCheck {
        if (map.size <= coords.second || map[0].size <= coords.first
            || coords.first < 0 || coords.second < 0) {
            return LocationCheck.OUTSIDE_MAP
        } else if (map[coords.second][coords.first]) {
            return LocationCheck.BLOCKED
        } else if (checkObstructionOption && loopLocation(coords, direction)) {
            return LocationCheck.OBSTRUCTION_OPTION
        } else {
            return LocationCheck.OK
        }
    }

    fun loopLocation(coords: Pair<Int, Int>, direction: Direction): Boolean {
        // Create new map.
        val newMap = IntRange(0,input.size-1)
            .map { y -> IntRange(0, input.get(y).length-1).map { x ->
                    if (x == coords.first && y == coords.second && input[y][x] == '.') {
                        '#'
                    } else {
                        input[y][x]
                    }
                }.toCharArray().joinToString("")
            }
            .toList()

        val newArea = GuardedArea(newMap)
        val maxNumberOfSteps = newMap.size * newMap[0].length * 4 // technically, this times 4 (number of directions)
        // Go back one, turn right, and then try and find a loop.
        val testGuard = Guard(newArea, newMap) // Start from start.
        var nextStep = testGuard.move(false)
        var i = 0
        val history = HashSet<GuardLocation>()
        while (!nextStep.outsideOfMap && i < maxNumberOfSteps) {
            if (history.contains(nextStep)) {
                return true
            }
            history.add(nextStep)
            nextStep = testGuard.move(false)
            i++
        }
        return false
    }

}

enum class LocationCheck {
    OK,
    BLOCKED,
    OBSTRUCTION_OPTION,
    OUTSIDE_MAP
}