package uk.andrewnorman

import java.io.File

fun main() {

    val lines = File("src/main/resources/input.txt").bufferedReader().readLines()

    //val lines = listOf("2333133121414131402")

    println(FileBlocks(lines.get(0)).process())

}

class FileBlocks {

    val files: MutableList<Int> = mutableListOf<Int>()
    val space: MutableList<Int> = mutableListOf<Int>()

    constructor(line: String) {
        IntRange(0, line.length-1)
            .forEach { index ->
                val count = line.get(index) - '0'
                if (index % 2 == 0) {
                    files.add(count)
                } else {
                    space.add(count)
                }
            }
    }

    fun process(): Long {
        var backCounter = files.size-1
        var backFileCounter = files.get(backCounter)
        var forwardCounter = 0
        var indexCounter = 0
        var sum: Long = 0

        while (forwardCounter < backCounter) {
            for (i in 0..files.get(forwardCounter)-1) {
                sum += indexCounter * forwardCounter
                indexCounter++
            }
            for (i in 0..space.get(forwardCounter)-1) {
                if (backFileCounter == 0) {
                    backCounter--
                    backFileCounter = files.get(backCounter)
                    if (backCounter == forwardCounter) {
                        // We're done.
                        return sum
                    }
                }
                sum += indexCounter * backCounter
                indexCounter++
                backFileCounter--
            }
            forwardCounter++
        }
        for (i in 0..backFileCounter-1) {
            sum += indexCounter * backCounter
            indexCounter++
        }
        return sum
    }

}

