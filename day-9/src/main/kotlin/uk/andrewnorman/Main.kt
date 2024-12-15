package uk.andrewnorman

import java.io.File
import java.util.stream.IntStream

fun main() {

    val lines = File("day-9/src/main/resources/input.txt").bufferedReader().readLines()

   // val lines = listOf("2333133121414131402")

    //println(FileBlocks(lines.get(0)).challengeOneProcess())
    println(FileBlocks(lines.get(0)).challengeTwoProcess())

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

    fun challengeTwoProcess(): Long {
        val fileMoves = mutableMapOf<Int, MutableList<FileMove>>()
        for (i in files.size-1 downTo 0) {
            val size = files.get(i)
            spaceloop@ for (j in 0..i-1) {
                if (size <= space.get(j)) {
                    if (fileMoves.get(j) == null) {
                        fileMoves.put(j, mutableListOf())
                    }
                    fileMoves.get(j)!!.add(FileMove(i, size))
                    val spaceSize = space!!.get(j)
                    files.set(i, -size)
                    space.set(j, (spaceSize - size))
                    @spaceloop break
                }
            }
        }

        println(fileMoves)
        var counter = 0
        return IntRange(0, files.size-1)
            .map{ index ->
                var sum: Long = 0
                var fileCount = files.get(index)
                while (fileCount > 0) {
                    sum += index * counter
                    fileCount--
                    counter++
                }
                // got to count the empty spaces the files moved away from.
                while (fileCount < 0) {
                    counter++
                    fileCount++
                }
                fileMoves.get(index)?.forEach { file ->
                    for (i in 0..file.size-1) {
                        sum += file.fileId * counter
                        counter++
                    }
                }
                if (space.size > index) {
                    for (i in 0..(space[index] - 1)) {
                        counter++
                    }
                }
                sum
            }.sum()
    }

    fun challengeOneProcess(): Long {
        var backCounter = files.size-1
        var forwardCounter = 0
        var indexCounter = 0
        var sum: Long = 0

        while (forwardCounter < backCounter) {
            for (i in 0..files.get(forwardCounter)-1) {
                sum += indexCounter * forwardCounter
                println("sum1: $sum = $indexCounter * $forwardCounter")
                indexCounter++
            }

            if (files.get(backCounter) > space.get(forwardCounter)) {
                indexCounter += space.get(forwardCounter)
            } else {
                var backFileCounter = files.get(backCounter)
                var fileId = backCounter
                for (i in 0..space.get(forwardCounter) - 1) {
                    if (backFileCounter == 0 && (space.get(forwardCounter) - i >= files.get(backCounter-1))) {
                        backCounter--
                        backFileCounter = files.get(backCounter)
                        fileId = backCounter
                        if (backCounter == forwardCounter) {
                            // We're done.
                            return sum
                        }
                    } else if (backFileCounter == 0) {
                        backCounter--
                        fileId = 0
                    }
                    sum += indexCounter * fileId
                    println("sum2: $sum = $indexCounter * $fileId")
                    indexCounter++
                    backFileCounter--
                }
            }
            forwardCounter++
        }
        return sum
    }
}

data class FileMove(val fileId: Int, val size: Int)

