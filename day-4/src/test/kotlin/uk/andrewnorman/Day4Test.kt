package uk.andrewnorman

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day4Test {


    @Test
    fun testWordSearch() {
        val result = WordFinder(listOf("MMMSXXMASM",
                "MSAMXMSMSA",
                "AMXSXMAAMM",
                "MSAMASMSMX",
                "XMASAMXAMM",
                "XXAMMXXAMA",
                "SMSMSASXSS",
                "SAXAMASAAA",
                "MAMMMXMMMM",
                "MXMXAXMASX")).startSearch()

        assertEquals(18, result)
    }

    @Test
    fun testDirectionCheck() {
        val result = WordFinder(listOf("MMMSXXMASM",
            "MSAMXMSMSA",
            "AMXSXMAAMM",
            "MSAMASMSMX",
            "XMASAMXAMM",
            "XXAMMXXAMA",
            "SMSMSASXSS",
            "SAXAMASAAA",
            "MAMMMXMMMM",
            "MXMXAXMASX")).checkDirection(WordFinder.Direction.RIGHT, 1, Pair(5, 9))

        assertEquals(1, result)
    }

    @Test
    fun testDirectionCheckFails() {
        val result = WordFinder(listOf("MMMSXXMASM",
            "MSAMXMSMSA",
            "AMXSXMAAMM",
            "MSAMASMSMX",
            "XMASAMXAMM",
            "XXAMMXXAMA",
            "SMSMSASXSS",
            "SAXAMASAAA",
            "MAMMMXMMMM",
            "MXMXAXMASX")).checkDirection(WordFinder.Direction.LEFT, 1, Pair(5, 9))

        assertEquals(0, result)
    }

    @Test
    fun testAllDirectionCheck() {
        val result = WordFinder(listOf("MMMSXXMASM",
            "MSAMXMSMSA",
            "AMXSXMAAMM",
            "MSAMASMSMX",
            "XMASAMXAMM",
            "XXAMMXXAMA",
            "SMSMSASXSS",
            "SAXAMASAAA",
            "MAMMMXMMMM",
            "MXMXAXMASX")).countFromX(Pair(5, 9))

        assertEquals(3, result)
    }

}