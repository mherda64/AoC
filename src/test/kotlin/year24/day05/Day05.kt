package year24.day05

import DayTemplate
import io.kotest.matchers.shouldBe

class Day05 : DayTemplate() {

    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 143
            secondPart(testInputPart2) shouldBe 123
        }

        "Should solve the proper input" {
            println(firstPart(input))
            println(secondPart(input))
        }
    }

    private fun firstPart(input: List<String>): Int {
        val (rules, updates) = parseInput(input)
        var sum = 0
        for (update in updates) {
            val reordered = reorderInvalid(rules, update)
            if (reordered == update) {
                sum += update[update.size / 2]
            }
        }

        return sum
    }

    private fun secondPart(input: List<String>): Int {
        val (rules, updates) = parseInput(input)
        var sum = 0
        for (update in updates) {
            val reordered = reorderInvalid(rules, update)
            if (reordered != update) {
                sum += reordered[reordered.size / 2]
            }
        }

        return sum
    }

    private fun reorderInvalid(rules: List<Pair<Int, Int>>, update: List<Int>): List<Int> {
        val filteredRules = rules.filter { update.contains(it.first) && update.contains(it.second) }
        val assumption =
            filteredRules.map { it.first }.groupBy { it }.entries.sortedByDescending { it.value.size }.map { it.key }

        return assumption + update.find { !assumption.contains(it) }!!
    }

    private fun parseInput(input: List<String>): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
        val rules = mutableListOf<Pair<Int, Int>>()
        val updates = mutableListOf<List<Int>>()
        for (row in input) {
            when {
                row.contains("|") -> {
                    rules.add(row.split("|").let { Pair(it[0].toInt(), it[1].toInt()) })
                }

                row.contains(",") -> {
                    updates.add(row.split(",").map { it.toInt() })
                }

                else -> Unit
            }
        }

        return Pair(rules.toList(), updates.toList())
    }
}