package uk.andrewnorman

import java.io.File


fun main() {

    val lines = File("src/main/resources/input.txt").bufferedReader().readLines()

//    val lines = listOf("89010123",
//            "78121874",
//            "87430965",
//            "96549874",
//            "45678903",
//            "32019012",
//            "01329801",
//            "10456732")

    println(TopographicalMap(lines).score())
    println(TopographicalMap(lines).rating())

}

class TopographicalMap(val lines: List<String>) {

    fun score(): Long {
        var sum: Long = 0
        for(y in 0..lines.size-1) {
            for(x in 0..lines.get(0).length-1) {
                if (lines[y][x] == '0') {
                    sum += countTrailheads(0, Coords(x, y)).size
                }
            }
        }
        return sum
    }

    fun rating(): Long {
        var sum: Long = 0
        for(y in 0..lines.size-1) {
            for(x in 0..lines.get(0).length-1) {
                if (lines[y][x] == '0') {
                    sum += countRatings(0, Coords(x, y))
                }
            }
        }
        return sum
    }

    fun countTrailheads(count: Int, coord: Coords): Set<Coords> {
        if (coord.x < 0 || coord.y < 0 || coord.x >= lines.get(0).length || coord.y >= lines.size) {
            return emptySet()
        }
        if ((lines[coord.y][coord.x] - '0') != count) {
            return emptySet() // Not the right number.
        }
        if (count == 9) {
            return mutableSetOf(coord)
        }
        val nines = mutableSetOf<Coords>()
        Direction.values().forEach {
           nines.addAll(countTrailheads(count+1, it.move(coord)))
        }
        return nines
    }

    fun countRatings(count: Int, coord: Coords): Int {
        if (coord.x < 0 || coord.y < 0 || coord.x >= lines.get(0).length || coord.y >= lines.size) {
            return 0
        }
        if ((lines[coord.y][coord.x] - '0') != count) {
            return 0 // Not the right number.
        }
        if (count == 9) {
            return 1
        }
        var trails = 0
        Direction.values().forEach {
            trails += (countRatings(count+1, it.move(coord)))
        }
        return trails
    }


}

enum class Direction(val move: (coords: Coords) -> Coords) {
    UP({ coords: Coords -> Coords(coords.x, coords.y-1) }),
    RIGHT({ coords: Coords -> Coords(coords.x+1, coords.y) }),
    DOWN({ coords: Coords -> Coords(coords.x, coords.y+1) }),
    LEFT({ coords: Coords -> Coords(coords.x-1, coords.y) }),
}

data class Coords(val x: Int, val y: Int)