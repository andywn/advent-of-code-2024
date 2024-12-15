package uk.andrewnorman

import java.io.File

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

    val visitedCoords = mutableSetOf<Coords>()

    fun process(): Long {
        var price = 0L
        for (y in 0..lines.size-1) {
            for (x in 0..lines[0].length-1) {
                if (!visitedCoords.contains(Coords(x, y))) {
                    val fencing = visitPlant(Coords(x, y), lines[y][x])
                    price += (fencing.perimeter * fencing.area)
                }
            }
        }
        return price
    }

    fun visitPlant(coords: Coords, plant: Char): Fencing {
        if (visitedCoords.contains(coords)) {
            return Fencing(0, 0)
        }
        visitedCoords.add(coords)
        var perimeter = 4
        var area = 1
        for (dir in Direction.entries) {
            if (checkPlant(dir.move(coords), plant)) {
                val fence = visitPlant(dir.move(coords), plant)
                perimeter--
                perimeter += fence.perimeter
                area += fence.area
            }
        }
        return Fencing(area, perimeter)
    }

    // Does plant match character 'plant'?
    fun checkPlant(coords: Coords, plant: Char): Boolean {
        return !(coords.x < 0 || coords.y < 0 || coords.y >= lines.size || coords.x >= lines[0].length)
                && lines[coords.y][coords.x] == plant
    }

}

data class Coords(val x: Int, val y: Int)

data class Fencing(val area: Int, val perimeter: Int)

enum class Direction(val move: (coords: Coords) -> Coords) {
    UP({ coords: Coords -> Coords(coords.x, coords.y-1) }),
    RIGHT({ coords: Coords -> Coords(coords.x+1, coords.y) }),
    DOWN({ coords: Coords -> Coords(coords.x, coords.y+1) }),
    LEFT({ coords: Coords -> Coords(coords.x-1, coords.y) }),
}