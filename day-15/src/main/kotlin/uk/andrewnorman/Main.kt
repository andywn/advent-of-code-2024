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
                it.toCharArray().flatMap {
                    when(it) {
                        ('#') -> listOf(WALL, WALL)
                        ('O') -> listOf(BOX_LEFT, BOX_RIGHT)
                        ('@') -> listOf(GUARD, EMPTY)
                        else -> listOf(EMPTY, EMPTY)
                    }
                }.toMutableList()
            }.toMutableList()

        guard = IntRange(0, warehouse.size-1)
            .filter { warehouse.get(it).contains(GUARD) }
            .map { Coords(warehouse.get(it).indexOf(GUARD), it) }
            .first()
    }

    fun move(lines: List<String>) {
        val moveRegex = Regex("[\\^<>v]+")
        lines.filter { moveRegex.matches(it) }
            .flatMap { it.toList() }
            .map{ Direction.findDirection(it) }
            .forEach {
                if (checkMove(GUARD, guard, it)) {
                    move(EMPTY, GUARD, guard, it)
                    guard = it.move(guard)
                }
            }
    }

    fun checkMove(thisItem: WarehouseItem, location: Coords, direction: Direction): Boolean {
        if (thisItem == WALL) {
            return false
        } else if (thisItem == EMPTY) {
            return true
        } else { // it's a box or a guard.
            val nextLocation = direction.move(location)
            var nextItem = warehouse[nextLocation.y][nextLocation.x]
            var result = checkMove(nextItem, nextLocation, direction)
            if (direction == Direction.DOWN || direction == Direction.UP) {
                if (nextItem == BOX_LEFT) {
                    result = result && checkMove(BOX_RIGHT, Direction.RIGHT.move(nextLocation), direction)
                } else if (nextItem == BOX_RIGHT) {
                    result = result && checkMove(BOX_LEFT, Direction.LEFT.move(nextLocation), direction)
                }
            }
            return result
        }
    }

    // Item is the item moving into this location.
    fun move(replacementItem: WarehouseItem, thisItem: WarehouseItem, location: Coords, direction: Direction) {
        if (thisItem == WALL) {
            println("TROUBLE!!!")
        }
        else if (thisItem == EMPTY) {
            warehouse[location.y][location.x] = replacementItem
        } else { // it's a box or a guard.
            val nextLocation = direction.move(location)
            val nextItem = warehouse[nextLocation.y][nextLocation.x]
            move(thisItem, nextItem, nextLocation, direction)
            if (replacementItem != EMPTY && (direction == Direction.DOWN || direction == Direction.UP)) {
                if (thisItem == BOX_LEFT) {
                    move(EMPTY, BOX_RIGHT, Direction.RIGHT.move(location), direction)
                } else if (thisItem == BOX_RIGHT) {
                    move(EMPTY, BOX_LEFT, Direction.LEFT.move(location), direction)
                }
            }
            warehouse[location.y][location.x] = replacementItem
        }
    }

    fun countGPS(): Long {
        return PlaneRange(warehouse[0].size, warehouse.size)
            .filter { warehouse[it.y][it.x] == BOX_LEFT }
            .map{ (100L * it.y) + it.x }
            .sum()
    }

}

enum class WarehouseItem(val c: Char) {
    WALL('#'),
    BOX('O'),
    BOX_LEFT('['),
    BOX_RIGHT(']'),
    GUARD('@'),
    EMPTY('.');

    override fun toString(): String {
        return "${this.c}"
    }
}