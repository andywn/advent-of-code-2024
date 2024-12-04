package uk.andrewnorman

import java.io.File

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger

fun main() {
    val lines = File("src/main/resources/input.txt")!!.bufferedReader()!!.readLines()
    println(WordFinder(lines).startSearch())
    println(XMasFinder(lines).startSearch())
}

class XMasFinder(lines: List<String>){

    val board: List<String>

    val getVal = {coords: Pair<Int, Int> ->
        if (coords.second > board.size-1 || coords.second < 0 ||
            coords.first > board.get(0).length-1 || coords.first < 0) {
            "Z"
        } else {
            board[coords.second][coords.first]
        }
    }

    val word = "MAS"

    init {
        this.board = lines
    }

    fun startSearch(): Int {
        val counter = AtomicInteger()
        runBlocking {
            for (i in 0..board.size-1) {
                launch {
                    val sum = IntRange(0, board.get(0).length-1).map { x -> Pair(x, i) }
                        .filter { coords -> getVal.invoke(coords) == word.get(1) }
                        .map { coords -> countFromA(coords) }
                        .sum()
                    counter.addAndGet(sum)
                }
            }
        }
        return counter.toInt()
    }

    fun countFromA(coords: Pair<Int, Int>): Int {
        // Four different X configs to check.
        val xSum = checkDirection(Direction.DOWN_RIGHT, 0, Direction.UP_LEFT.move(coords)) +
            checkDirection(Direction.UP_LEFT, 0, Direction.DOWN_RIGHT.move(coords)) +
            checkDirection(Direction.DOWN_LEFT, 0, Direction.UP_RIGHT.move(coords)) +
            checkDirection(Direction.UP_RIGHT, 0, Direction.DOWN_LEFT.move(coords))

        if (xSum == 2) {
            // We need both parts of the X to return a single match.
            return 1
        }
        return 0
    }

    fun checkDirection(direction: Direction, counter: Int, coords: Pair<Int, Int>): Int {
        if (getVal(coords) == word.get(counter)) {
            if (counter == word.length-1) {
                return 1
            }
            return checkDirection(direction, counter+1, direction.move(coords))
        }
        return 0
    }
}


class WordFinder(lines: List<String>){

    val board: List<String>

    val getVal = {coords: Pair<Int, Int> ->
        if (coords.second > board.size-1 || coords.second < 0 ||
            coords.first > board.get(0).length-1 || coords.first < 0) {
            "Z"
        } else {
            board[coords.second][coords.first]
        }
    }

    val word = "XMAS"

    init {
        this.board = lines
    }

    fun startSearch(): Int {
        val counter = AtomicInteger()
        runBlocking {
            for (i in 0..board.size-1) {
                launch {
                    val sum = IntRange(0, board.get(0).length-1).map { x -> Pair(x, i) }
                        .filter { coords -> getVal.invoke(coords) == word.get(0) }
                        .map { coords -> countFromX(coords) }
                        .sum()
                    counter.addAndGet(sum)
                }
            }
        }
        return counter.toInt()
    }

    fun countFromX(coords: Pair<Int, Int>): Int {
        var success = 0
        for (direction in Direction.entries) {
            success += checkDirection(direction, 1, coords)
        }
        return success
    }

    fun checkDirection(direction: Direction, counter: Int, coords: Pair<Int, Int>): Int {
        if (getVal(direction.move(coords)) == word.get(counter)) {
            if (counter == word.length-1) {
                return 1
            }
            return checkDirection(direction, counter+1, direction.move(coords))
        }
        return 0
    }

}

enum class Direction(val move: (coords: Pair<Int, Int>) -> Pair<Int, Int>) {

    UP({coords: Pair<Int, Int> -> Pair(coords.first, coords.second-1)}),
    DOWN({coords: Pair<Int, Int> -> Pair(coords.first, coords.second+1)}),
    LEFT({coords: Pair<Int, Int> -> Pair(coords.first-1, coords.second)}),
    RIGHT({coords: Pair<Int, Int> -> Pair(coords.first+1, coords.second)}),
    UP_LEFT({coords: Pair<Int, Int> -> LEFT.move(UP.move(coords))}),
    UP_RIGHT({coords: Pair<Int, Int> -> RIGHT.move(UP.move(coords))}),
    DOWN_LEFT({coords: Pair<Int, Int> -> LEFT.move(DOWN.move(coords))}),
    DOWN_RIGHT({coords: Pair<Int, Int> -> RIGHT.move(DOWN.move(coords))})
}