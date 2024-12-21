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

    Computer(lines).run()
}

class Computer {

    val registers: Registers
    val instructions: List<Int>

    constructor(lines: List<String>) {
        val number = Regex("(\\d+)")
        val program = Regex("Program: ([\\d,]+)")
        registers = Registers(
            number.find(lines.get(0))!!.groups[1]!!.value.toInt(),
            number.find(lines.get(1))!!.groups[1]!!.value.toInt(),
            number.find(lines.get(2))!!.groups[1]!!.value.toInt(),
            0
        )
        instructions = program.find(lines.get(4))!!.groups[1]!!.value.split(",").map{ it.toInt() }.toList()
    }

    fun run() {
        while (registers.instructionPointer < instructions.size) {
            val inst = Instruction.findInst(instructions.get(registers.instructionPointer))
            var operand =  instructions.get(registers.instructionPointer + 1)
            if (inst.comboOperand) {
                operand = ComboOperand.findCombo(operand).comboOperand(registers)
            }
            print(inst.instruction(operand, registers)?.toString()?.plus(",").orEmpty())
        }
    }
}

enum class Instruction(val opcode: Int, val comboOperand: Boolean, val instruction: (operand: Int, registers: Registers) -> Int?) {
    ADV(0, true, { literalOperand: Int, registers: Registers ->
        val result = (registers.registerA.toDouble() / Math.pow(2.0, literalOperand.toDouble())).toInt()
        registers.registerA = result
        registers.instructionPointer += 2
        null
    }),
    BXL(1, false, { literalOperand: Int, registers: Registers ->
        registers.registerB = registers.registerB.toLong().xor(literalOperand.toLong()).toInt()
        registers.instructionPointer += 2
        null
    }),
    BST(2, true, { literalOperand: Int, registers: Registers ->
        registers.registerB = literalOperand % 8
        registers.instructionPointer += 2
        null
    }),
    JNZ(3, false, { literalOperand: Int, registers: uk.andrewnorman.Registers ->
        if (registers.registerA > 0) {
            registers.instructionPointer = literalOperand
        } else {
            registers.instructionPointer += 2
        }
        null
    }),
    BXC(4, false, { literalOperand: Int, registers: Registers ->
        registers.registerB = registers.registerB.toLong().xor(registers.registerC.toLong()).toInt()
        registers.instructionPointer += 2
        null
    }),
    OUT(5, true, { literalOperand: Int, registers: Registers ->
        registers.instructionPointer += 2
        literalOperand % 8
    }),
    BDV(6, true, { literalOperand: Int, registers: Registers ->
        val result = (registers.registerA.toDouble() / Math.pow(2.0, literalOperand.toDouble())).toInt()
        registers.registerB = result
        registers.instructionPointer += 2
        null
    }),
    CDV(7, true, { literalOperand: Int, registers: Registers ->
        val result = (registers.registerA.toDouble() / Math.pow(2.0, literalOperand.toDouble())).toInt()
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

enum class ComboOperand(val comboCode: Int, val comboOperand: (registers: Registers) -> Int) {
    ZERO(0, { registers: Registers -> 0 }),
    ONE(1, { registers: Registers -> 1 }),
    TWO(2, { registers: Registers -> 2 }),
    THREE(3, { registers: Registers -> 3 }),
    FOUR(4, { registers: Registers -> registers.registerA }),
    FIVE(5, { registers: Registers -> registers.registerB }),
    SIX(6, { registers: Registers -> registers.registerC }),
    SEVEN(7, { oregisters: Registers -> println("TROUBLE"); 0});

    companion object {
        fun findCombo(code: Int): ComboOperand {
            return ComboOperand.entries.first { it.comboCode == code}
        }
    }
}

data class Registers(var registerA: Int, var registerB: Int, var registerC: Int, var instructionPointer: Int)