package uk.andrewnorman

import java.io.File

fun main() {
    val lines = File("day-13/src/main/resources/input.txt").bufferedReader().readLines()

//    val lines = listOf("Button A: X+94, Y+34",
//            "Button B: X+22, Y+67",
//            "Prize: X=8400, Y=5400",
//            "",
//            "Button A: X+26, Y+66",
//            "Button B: X+67, Y+21",
//            "Prize: X=12748, Y=12176",
//            "",
//            "Button A: X+17, Y+86",
//            "Button B: X+84, Y+37",
//            "Prize: X=7870, Y=6450",
//            "",
//            "Button A: X+69, Y+23",
//            "Button B: X+27, Y+71",
//            "Prize: X=18641, Y=10279")

    var line = 0
    var sumTokens = 0L
    while (line < lines.size) {
        sumTokens += ClawGame(lines[line], lines[line+1], lines[line+2]).winGame()
        line += 4
    }
    println(sumTokens)
}

class ClawGame(val buttonAStr: String, val buttonBStr: String, val prizeStr: String) {

    fun winGame(): Long {
        val buttonA = Button(buttonAStr)
        val buttonB = Button(buttonBStr)
        val prize = Prize(prizeStr)

        /**
         * So we know:
         * ax * a + bx * b = px
         * ay * a + by * b = py
         * We can solve these two equations.
         * a = (px - bx*b)/ax
         * b = (py - ay*a)/by
         * b*by = py - ay* ((px-bx*b)/ax)
         *      = py - (ay*px)/ax + (ay*bx*b)/ax
         * b(ax*by - ay*bx) = ax*py - ay*px
         * therefore...
         * b = (ax*py - ay*px) / (ax*py - ay*bx)
         * If we can solve for a and b, and both divide without a remainder, then we have the min solution.
         */

        //Algebra Solution.
        val b_dividend = (buttonA.x*prize.y - buttonA.y*prize.x)
        val b_divisor = (buttonA.x*buttonB.y - buttonA.y*buttonB.x)
        if (b_dividend % b_divisor != 0L) {
            return 0
        }
        val b = b_dividend/b_divisor
        val a_dividend = (prize.x - (buttonB.x * b))
        val a_divisor = buttonA.x
        if (a_dividend % a_divisor != 0L) {
            return 0
        }
        val a = a_dividend/a_divisor

        return (a*3 + b)
    }

}

class Button {
    val x: Long
    val y: Long
    val buttonRegex = Regex("Button [A|B]: X\\+(\\d+), Y\\+(\\d+)")

    constructor(button: String) {
        var group = buttonRegex.find(button)!!.groups
        x = group[1]!!.value.toLong()
        y = group[2]!!.value.toLong()
    }
}

class Prize {
    val x: Long
    val y: Long
    val prizeRegex = Regex("Prize: X=(\\d+), Y=(\\d+)")

    constructor(button: String) {
        var group = prizeRegex.find(button)!!.groups
        x = group[1]!!.value.toLong() + 10000000000000L
        y = group[2]!!.value.toLong() + 10000000000000L
    }

}