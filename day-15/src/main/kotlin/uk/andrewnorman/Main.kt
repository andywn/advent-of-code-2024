package uk.andrewnorman

import java.io.File
import uk.andrewnorman.WarehouseItem.*

fun main() {
    val lines = File("day-15/src/main/resources/input.txt").bufferedReader().readLines()

//    val lines = listOf("##########",
//            "#..O..O.O#",
//            "#......O.#",
//            "#.OO..O.O#",
//            "#..O@..O.#",
//            "#O#..O...#",
//            "#O..O..O.#",
//            "#.OO.O.OO#",
//            "#....O...#",
//            "##########",
//            "",
//            "<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^",
//            "vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v",
//            "><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<",
//            "<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^",
//            "^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><",
//            "^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^",
//            ">^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^",
//            "<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>",
//            "^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>",
//            "v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^")

    val wh = Warehouse(lines)
    wh.move(lines)
    println(wh.countGPS())
}

class Warehouse {

    val warehouse: MutableList<MutableList<WarehouseItem>>
    var guard: Coords

    constructor(lines: List<String>) {
        val whRegex = Regex("[#.O@]+")
        warehouse = lines.filter { whRegex.matches(it) }
            .map {
                it.toCharArray().map {
                    when(it) {
                        ('#') -> WALL
                        ('O') -> BOX
                        ('@') -> GUARD
                        else -> EMPTY
                    }
                }.toMutableList()
            }.toMutableList()

        guard = IntRange(0, lines.size-1)
            .filter { lines.get(it).contains("@") }
            .map { Coords(lines.get(it).indexOf("@"), it) }
            .first()
    }

    fun move(lines: List<String>) {
        val moveRegex = Regex("[\\^<>v]+")
        lines.filter { moveRegex.matches(it) }
            .flatMap { it.toList() }
            .map{ Direction.findDirection(it) }
            .forEach {
                if (move(EMPTY, guard, it)) {
                    guard = it.move(guard)
                }
            }
    }

    // Item is the item moving into this location.
    fun move(item: WarehouseItem, location: Coords, direction: Direction): Boolean {
        if (warehouse[location.y][location.x] == WALL) {
            return false
        } else if (warehouse[location.y][location.x] == EMPTY) {
            warehouse[location.y][location.x] = item
            return true
        } else { // it's a box or a guard.
            var result = move(warehouse[location.y][location.x], direction.move(location), direction)
            if (result) {
                warehouse[location.y][location.x] = item
            }
            return result
        }
    }

    fun countGPS(): Long {
        return IntRange(0, warehouse.size-1).flatMap {
            y -> IntRange(0, warehouse[y].size-1).map { x -> Coords(x, y) }
        }.filter { warehouse[it.y][it.x] == BOX }
            .map{ (100L * it.y) + it.x }
            .sum()
    }

}

enum class WarehouseItem {
    WALL,
    BOX,
    GUARD,
    EMPTY
}