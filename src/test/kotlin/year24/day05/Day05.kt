package year24.day05

import DayTemplate
import io.kotest.matchers.shouldBe

class Day05 : DayTemplate() {

    init {
        "Should solve the test input" {
            val (firstPart, secondPart) = solution(testInputPart1)
            firstPart shouldBe 143
            secondPart shouldBe 123
        }

        "Should solve the proper input" {
            val (firstPart, secondPart) = solution(testInputPart1)
            println(firstPart)
            println(secondPart)
        }
    }

    private fun solution(input: List<String>): Pair<Int, Int> {
        val (rules, updates) = parseInput(input)
        var firstPartSum = 0
        var secondPartSum = 0
        for (update in updates) {
            val reordered = reorderInvalid(rules, update)
            if (reordered == update) {
                firstPartSum += update[update.size / 2]
            } else {
                secondPartSum += reordered[reordered.size / 2]
            }
        }

        return Pair(firstPartSum, secondPartSum)
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