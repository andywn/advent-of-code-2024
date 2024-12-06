package uk.andrewnorman

class GuardedArea (val input: List<String>) {

    val map: List<List<Boolean>> = input.stream().map{
        it.chars().mapToObj{ it -> it == '#'.code }.toList()
    }.toList()

    fun checkLocation(coords: Pair<Int, Int>): LocationCheck {
        if (map.size <= coords.second || map[0].size <= coords.first
            || coords.first < 0 || coords.second < 0) {
            return LocationCheck.OUTSIDE_MAP
        } else if (map[coords.second][coords.first]) {
            return LocationCheck.BLOCKED
        } else {
            return LocationCheck.OK
        }
    }

}

enum class LocationCheck {
    OK,
    BLOCKED,
    OUTSIDE_MAP
}