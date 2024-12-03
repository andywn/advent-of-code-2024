package uk.andrewnorman

import java.io.File
import kotlin.streams.asStream

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    val regex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")
    val lines = File("src/main/resources/input.txt")?.bufferedReader()?.readLines()
    //val lines = listOf("mul(1,2)mul(3,4)mu[4,5]")

    val sum = lines!!.stream()
        .flatMap{ line -> regex.findAll(line).asStream() }
        .map { matchResult -> matchResult.groupValues[1].toString().toInt() * matchResult.groupValues[2].toString().toInt()}
        .toList().sum()

    println(sum)
}