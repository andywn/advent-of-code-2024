package uk.andrewnorman

import java.io.File
import kotlin.math.abs

fun main() {

    val lines = File("day-2/src/main/resources/input.txt")?.bufferedReader()?.readLines()

    println(lines!!.stream()
        .filter { it -> Report(it).getResult(true, -1) != ReportResult.UNSAFE }
        .count())

}

enum class ReportResult {
    INCREASING, DECREASING, UNSAFE
}

class Report(report: String) {

    val regex = Regex("\\s*\\d+")

    var reportList: List<Int>

    init {
        reportList = regex.findAll(report, 0).toList().stream()
            .map { it -> it.value.trim().toInt() }
            .toList()
    }

    fun getResult(allowOneError: Boolean, skipIndex: Int): ReportResult {

        var rl = IntRange(0, reportList.size-1)
            .filter{ it -> allowOneError && it != skipIndex }
            .map { reportList.get(it) }
            .toList()


        // Could have done this nicer with fold. Maybe redo later?
        var values = IntRange(0, rl.size - 2)
            .map { index ->
                val first = rl.get(index)
                val second = rl.get(index + 1)
                val threeDiffRule = abs(first - second) <= 3
                // Based just on these two values, which direction as we going?
                val direction = when {
                    (first < second) -> ReportResult.INCREASING
                    (first > second) -> ReportResult.DECREASING
                    else -> ReportResult.UNSAFE
                }
                if (threeDiffRule) direction else ReportResult.UNSAFE
            }
            .toList().groupingBy { it }.eachCount()

        var allResults = rl.size - 1
        return when {
            (values.containsKey(ReportResult.INCREASING) &&
                    values.get(ReportResult.INCREASING)!! >= allResults)
                -> ReportResult.INCREASING

            (values.containsKey(ReportResult.DECREASING) &&
                    values.get(ReportResult.DECREASING)!! >= allResults)
                -> ReportResult.DECREASING
            // recursive call back to check with missing index.
            (allowOneError && skipIndex < reportList.size-1) ->  getResult(allowOneError, skipIndex+1)

            else -> ReportResult.UNSAFE
        }
    }
}