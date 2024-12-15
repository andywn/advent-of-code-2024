package uk.andrewnorman

import java.io.File

import uk.andrewnorman.Direction.*

fun main() {
    val lines = File("day-12/src/main/resources/input.txt").bufferedReader().readLines()

//    val lines = listOf("RRRRIICCFF",
//            "RRRRIICCCF",
//            "VVRRRCCFFF",
//            "VVRCCCJFFF",
//            "VVVVCJJCFE",
//            "VVIVCCJJEE",
//            "VVIIICJJEE",
//            "MIIIIIJJEE",
//            "MIIISIJEEE",
//            "MMMISSJEEE")

    println(Garden(lines).process())
}

class Garden(val lines: List<String>) {

    val visitedCoords = mutableMapOf<Coords, Set<Direction>>()

    fun process(): Long {
        var price = 0L
        for (y in 0..lines.size-1) {
            for (x in 0..lines[0].length-1) {
                if (!visitedCoords.contains(Coords(x, y))) {
                    val fencing = visitPlant(Coords(x, y), lines[y][x])
                    println("${lines[y][x]} gives result $fencing")
                    price += (fencing.perimeter * fencing.area)
                }
            }
        }
        return price
    }

    fun visitPlant(coords: Coords, plant: Char): Fencing {
        if (visitedCoords.contains(coords)) {
            return Fencing(0, 0, visitedCoords.get(coords)!!)
        }
        val fenceSet = mutableSetOf<Direction>(UP, DOWN, LEFT, RIGHT)
        for (dir in Direction.entries) {
            if (checkPlant(dir.move(coords), plant)) {
                fenceSet.remove(dir)
            }
        }
        visitedCoords.put(coords, fenceSet)

        var perimeter = 0
        var area = 1
        var fenceMap = mutableMapOf<Direction, Set<Direction>>()
        for (dir in Direction.entries) {
            if (checkPlant(dir.move(coords), plant)) {
                val fence = visitPlant(dir.move(coords), plant)
                perimeter += fence.perimeter
                area += fence.area
                fenceMap.put(dir, fence.lastFenceSet)
            }
        }
        // Calculate perimeters for this single coord. Only counting a fence if you're the first
        // coord with that side.
        for (dir in fenceSet) {
            perimeter += when(dir) {
                (UP) -> if (fenceMap.getOrDefault(LEFT, emptySet()).contains(UP)) { 0 } else { 1 }
                (RIGHT) -> if (fenceMap.getOrDefault(UP, emptySet()).contains(RIGHT)) { 0 } else { 1 }
                (DOWN) -> if (fenceMap.getOrDefault(RIGHT, emptySet()).contains(DOWN)) { 0 } else { 1 }
                (LEFT) -> if (fenceMap.getOrDefault(DOWN, emptySet()).contains(LEFT)) { 0 } else { 1 }
            }
        }

        return Fencing(area, perimeter, fenceSet)
    }

    // Does plant match character 'plant'?
    fun checkPlant(coords: Coords, plant: Char): Boolean {
        return !(coords.x < 0 || coords.y < 0 || coords.y >= lines.size || coords.x >= lines[0].length)
                && lines[coords.y][coords.x] == plant
    }

}

data class Coords(val x: Int, val y: Int)

data class Fencing(val area: Int, val perimeter: Int, val lastFenceSet: Set<Direction>)

enum class Direction(val move: (coords: Coords) -> Coords) {
    UP({ coords: Coords -> Coords(coords.x, coords.y-1) }),
    RIGHT({ coords: Coords -> Coords(coords.x+1, coords.y) }),
    DOWN({ coords: Coords -> Coords(coords.x, coords.y+1) }),
    LEFT({ coords: Coords -> Coords(coords.x-1, coords.y) }),
}