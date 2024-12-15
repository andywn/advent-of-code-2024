package uk.andrewnorman

import java.io.File
import kotlin.streams.asStream

fun main() {

    val regex = Regex("(mul\\((\\d{1,3}),(\\d{1,3})\\))|(do\\(\\))|(don't\\(\\))")
    val lines = File("day-3/src/main/resources/input.txt")?.bufferedReader()?.readLines()

    val instructions = lines!!.stream()
        .flatMap{ line -> regex.findAll(line).asStream() }
        .map { matchResult ->
            when {
                matchResult.value.contains("mul") ->
                    InstructionAndValue(
                        Instruction.MUL,
                        matchResult.groupValues[2].toString().toInt() * matchResult.groupValues[3].toString().toInt()
                    )
                matchResult.value.contains("don't") ->
                    InstructionAndValue(Instruction.DONT, 0)
                else -> InstructionAndValue(Instruction.DO, 0)
            }
        }
        .toList()

    val pair = instructions.fold(Pair(Instruction.DO, 0)) { pair, instr ->
        when(instr.instruction) {
            (Instruction.DO) -> Pair(Instruction.DO, pair.second)
            (Instruction.DONT) -> Pair(Instruction.DONT, pair.second)
            else -> if (pair.first == Instruction.DO) { Pair(Instruction.DO, pair.second + instr.value) } else { pair }
        }
    }

    println(pair.second)
}

enum class Instruction {
    MUL, DO, DONT
}

data class InstructionAndValue(val instruction: Instruction, val value: Int)