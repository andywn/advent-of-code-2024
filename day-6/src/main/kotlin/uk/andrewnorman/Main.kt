package uk.andrewnorman.uk.andrewnorman

import uk.andrewnorman.Guard
import uk.andrewnorman.GuardedArea
import java.io.File

fun main() {

    val lines = File("src/main/resources/input.txt").bufferedReader().readLines()
    val area = GuardedArea(lines)
    val guard = Guard(area, lines)

    while (!guard.move().outsideOfMap) { }
    println(guard.getCount())
    println(guard.getObstructions())
}




