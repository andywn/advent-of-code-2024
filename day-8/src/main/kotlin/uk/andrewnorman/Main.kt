package uk.andrewnorman

import java.io.File
import kotlin.math.abs
import kotlin.math.min

fun main() {

    val lines = File("src/main/resources/input.txt").bufferedReader().readLines()

//    val lines = listOf(
//        "............",
//        "........0...",
//        ".....0......",
//        ".......0....",
//        "....0.......",
//        "......A.....",
//        "............",
//        "............",
//        "........A...",
//        ".........A..",
//        "............",
//        "............"
//    )

    val antennaMap = AntennaMap(lines)

    println(antennaMap.countAntinodes())
    println(antennaMap.antinodes)

}

class AntennaMap {

    val locations: Map<String, List<Pair<Int, Int>>>
    val maxX: Int
    val maxY: Int
    val antinodes = HashSet<String>()

    constructor(lines: List<String>) {
        locations = IntRange(0, lines.size-1)
            .flatMap { y -> IntRange(0, lines.get(y).length-1)
                .map { x -> Pair(x, y)}
            }
            .filter { lines[it.second][it.first] != '.' }
            .toList()
            .groupBy({ lines[it.second][it.first].toString() }, {it})
        maxY = lines.size-1
        maxX = lines.get(0).length-1
    }

    fun countAntinodes(): Int {
       locations.keys.stream()
            .flatMap{ key ->
                IntRange(0, locations.get(key)!!.size-1)
                    .flatMap {
                        coord1 -> IntRange(coord1 + 1, locations.get(key)!!.size-1).
                            map{ coord2 -> Pair(
                                locations.get(key)!!.get(coord1),
                                locations.get(key)!!.get(coord2)
                            )}
                    }.stream()
            }.forEach {
                antinodeExists(it.first, it.second)
                antinodeExists(it.second, it.first)
            }
        return antinodes.count()
    }

    fun antinodeExists(a1: Pair<Int, Int>, a2: Pair<Int, Int>) {

        var gcd = 1
        var xdif = a1.first - a2.first
        var ydif = a1.second - a2.second
        for (i in 1..min(abs(xdif),abs(ydif))) {
            if (abs(xdif) % i == 0 && abs(ydif) % i == 0) {
                gcd = i
            }
        }
        xdif = xdif / gcd
        ydif = ydif / gcd

        var x = a1.first
        var y = a1.second

        while (x >= 0 && x <= maxX && y >= 0 && y <= maxY) {
            antinodes.add("$x:$y")
            x = x + xdif
            y = y + ydif
        }
    }

}