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
    var sumTokens = 0
    while (line < lines.size) {
        sumTokens += ClawGame(lines[line], lines[line+1], lines[line+2]).winGame()
        line += 4
    }
    println(sumTokens)
}

class ClawGame(val buttonAStr: String, val buttonBStr: String, val prizeStr: String) {

    fun winGame(): Int {
        val buttonA = Button(buttonAStr)
        val buttonB = Button(buttonBStr)
        val prize = Prize(prizeStr)
        var minCost = 0

        // i == how many A's
        aloop@for (i in 0..100) {
            val diffX = prize.x - (i * buttonA.x)
            val diffY = prize.y - (i * buttonA.y)
            if (diffX < 0 || diffY < 0) {
                break@aloop
            }
            if (diffX % buttonB.x == 0 && diffY % buttonB.y == 0 && (diffX / buttonB.x) == (diffY / buttonB.y)) {
                // Cost
                val cost = 3 * i + (diffY / buttonB.y)
                if (minCost == 0 || minCost > cost) {
                    minCost = cost
                }
            }
        }
        return minCost
    }

}

class Button {
    val x: Int
    val y: Int
    val buttonRegex = Regex("Button [A|B]: X\\+(\\d+), Y\\+(\\d+)")

    constructor(button: String) {
        var group = buttonRegex.find(button)!!.groups
        x = group[1]!!.value.toInt()
        y = group[2]!!.value.toInt()
    }
}

class Prize {
    val x: Int
    val y: Int
    val prizeRegex = Regex("Prize: X=(\\d+), Y=(\\d+)")

    constructor(button: String) {
        var group = prizeRegex.find(button)!!.groups
        x = group[1]!!.value.toInt()
        y = group[2]!!.value.toInt()
    }

}