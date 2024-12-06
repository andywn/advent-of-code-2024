package uk.andrewnorman

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day5Test {

    @Test
    fun test() {
        var input = listOf(
            "47|53",
            "97|13",
            "97|61",
            "97|47",
            "75|29",
            "61|13",
            "75|53",
            "29|13",
            "97|29",
            "53|29",
            "61|53",
            "97|53",
            "61|29",
            "47|13",
            "75|47",
            "97|75",
            "47|61",
            "75|61",
            "47|29",
            "75|13",
            "53|13",
            "",
            "75,47,61,53,29",
            "97,61,53,29,13",
            "75,29,13",
            "75,97,47,61,53",
            "61,13,29",
            "97,13,75,29,47"
        )

        val rules = Rules(input)
        assertEquals(61,
            PrintOrder("75,47,61,53,29").checkRules(rules))

        assertEquals(53,
            PrintOrder("97,61,53,29,13").checkRules(rules))

        assertEquals(29,
            PrintOrder("75,29,13").checkRules(rules))

        assertEquals(0,
            PrintOrder("61,13,29").checkRules(rules))

        assertEquals(0,
            PrintOrder("97,13,75,29,47").checkRules(rules))
    }

    @Test
    fun testSingleRule() {
        assertEquals(true,
            PrintOrder("75,47,61,53,29").checkSingleRule(53, 1))

        assertEquals(true,
            PrintOrder("75,47,61,53,29").checkSingleRule(29, 3))

        assertEquals(false,
            PrintOrder("75,47,61,53,29").checkSingleRule(47, 3))

        assertEquals(true,
            PrintOrder("75,47,61,53,29").checkSingleRule(99, 3))

    }

    @Test
    fun testReorderedList() {
        var input = listOf(
            "47|53",
            "97|13",
            "97|61",
            "97|47",
            "75|29",
            "61|13",
            "75|53",
            "29|13",
            "97|29",
            "53|29",
            "61|53",
            "97|53",
            "61|29",
            "47|13",
            "75|47",
            "97|75",
            "47|61",
            "75|61",
            "47|29",
            "75|13",
            "53|13",
            "",
            "75,47,61,53,29",
            "97,61,53,29,13",
            "75,29,13",
            "75,97,47,61,53",
            "61,13,29",
            "97,13,75,29,47"
        )

        val rules = Rules(input)
        assertEquals(47,
            PrintOrder("75,97,47,61,53").reorderAndCheckRules(rules))

        assertEquals(29,
            PrintOrder("61,13,29").reorderAndCheckRules(rules))

        assertEquals(47,
            PrintOrder("97,13,75,29,47").reorderAndCheckRules(rules))
    }
}