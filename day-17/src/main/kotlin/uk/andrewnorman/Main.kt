package uk.andrewnorman

import java.io.File
import kotlin.math.roundToInt

fun main() {
    val lines = File("day-17/src/main/resources/input.txt").bufferedReader().readLines()

//    val lines = listOf("Register A: 729",
//            "Register B: 0",
//            "Register C: 0",
//            "",
//            "Program: 0,1,5,4,3,0")

    println(Computer(lines).run())
    println(Computer(lines).test(30000000000021))

    println("Found it! ${Computer(lines).challengeTwo()}")
}

class Computer {

    val registers: Registers
    val instructions: List<Int>

    constructor(lines: List<String>) {
        val number = Regex("(\\d+)")
        val program = Regex("Program: ([\\d,]+)")
        registers = Registers(
            number.find(lines.get(0))!!.groups[1]!!.value.toLong(),
            number.find(lines.get(1))!!.groups[1]!!.value.toLong(),
            number.find(lines.get(2))!!.groups[1]!!.value.toLong(),
            0
        )
        instructions = program.find(lines.get(4))!!.groups[1]!!.value.split(",").map{ it.toInt() }.toList()
    }

    fun challengeTwo(): Long {

        // This long has 12 matches.
        var i = 30045532529563
        var maxMatchId = 0


        iloop@while (i < 300000000000000) {
            var matchId = 0
            registers.instructionPointer = 0
            registers.registerB = 0
            registers.registerC = 0
            registers.registerA = i
            while (registers.instructionPointer < instructions.size) {
                val inst = Instruction.findInst(instructions.get(registers.instructionPointer))
                var operand = instructions.get(registers.instructionPointer + 1).toLong()
                if (inst.comboOperand) {
                    operand = ComboOperand.findCombo(operand.toInt()).comboOperand(registers)
                }
                val output = inst.instruction(operand, registers)?.toInt()
                if (output?.equals(instructions.get(matchId)) == false) {
                    // Output is always mod 8. Playing around, can see that roughly, we need to find the right
                    // power of 8 that matches the offset on the instructions list.
                    i += Math.pow(8.0, 12.0).toLong()
                    continue@iloop
                } else if (output?.equals(instructions.get(matchId)) == true) {
                    matchId++
                    if (matchId > maxMatchId) {
                        maxMatchId = matchId
                        println("New max match Id: $maxMatchId at index $i")
                    }
                }
            }
            return i
        }
        return 0
    }

    fun test(regA: Long): String {
        registers.registerA = regA
        return run()
    }

    fun run(): String {
        var resultString = ""
        while (registers.instructionPointer < instructions.size) {
            val inst = Instruction.findInst(instructions.get(registers.instructionPointer))
            var operand =  instructions.get(registers.instructionPointer + 1).toLong()
            if (inst.comboOperand) {
                operand = ComboOperand.findCombo(operand.toInt()).comboOperand(registers)
            }
            resultString = resultString.plus(inst.instruction(operand, registers)?.toString()?.plus(",").orEmpty())
        }
        return resultString
    }
}

enum class Instruction(val opcode: Int, val comboOperand: Boolean, val instruction: (operand: Long, registers: Registers) -> Int?) {
    ADV(0, true, { literalOperand: Long, registers: Registers ->
        val result = (registers.registerA.toDouble() / Math.pow(2.0, literalOperand.toDouble())).toLong()
        registers.registerA = result
        registers.instructionPointer += 2
        null
    }),
    BXL(1, false, { literalOperand: Long, registers: Registers ->
        registers.registerB = registers.registerB.toLong().xor(literalOperand.toLong()).toLong()
        registers.instructionPointer += 2
        null
    }),
    BST(2, true, { literalOperand: Long, registers: Registers ->
        registers.registerB = literalOperand % 8
        registers.instructionPointer += 2
        null
    }),
    JNZ(3, false, { literalOperand: Long, registers: uk.andrewnorman.Registers ->
        if (registers.registerA > 0) {
            registers.instructionPointer = literalOperand.toInt()
        } else {
            registers.instructionPointer += 2
        }
        null
    }),
    BXC(4, false, { literalOperand: Long, registers: Registers ->
        registers.registerB = registers.registerB.toLong().xor(registers.registerC.toLong()).toLong()
        registers.instructionPointer += 2
        null
    }),
    OUT(5, true, { literalOperand: Long, registers: Registers ->
        registers.instructionPointer += 2
        (literalOperand % 8).toInt()
    }),
    BDV(6, true, { literalOperand: Long, registers: Registers ->
        val result = (registers.registerA.toDouble() / Math.pow(2.0, literalOperand.toDouble())).toLong()
        registers.registerB = result
        registers.instructionPointer += 2
        null
    }),
    CDV(7, true, { literalOperand: Long, registers: Registers ->
        val result = (registers.registerA.toDouble() / Math.pow(2.0, literalOperand.toDouble())).toLong()
        registers.registerC = result
        registers.instructionPointer += 2
        null
    });

    companion object {
        fun findInst(code: Int): Instruction {
            return Instruction.entries.first { it.opcode == code}
        }
    }
}

enum class ComboOperand(val comboCode: Int, val comboOperand: (registers: Registers) -> Long) {
    ZERO(0, { registers: Registers -> 0L }),
    ONE(1, { registers: Registers -> 1L }),
    TWO(2, { registers: Registers -> 2L }),
    THREE(3, { registers: Registers -> 3L }),
    FOUR(4, { registers: Registers -> registers.registerA }),
    FIVE(5, { registers: Registers -> registers.registerB }),
    SIX(6, { registers: Registers -> registers.registerC }),
    SEVEN(7, { registers: Registers -> println("TROUBLE"); 0L});

    companion object {
        fun findCombo(code: Int): ComboOperand {
            return ComboOperand.entries.first { it.comboCode == code}
        }
    }
}

data class Registers(var registerA: Long, var registerB: Long, var registerC: Long, var instructionPointer: Int)