package year24.day01

import DayTemplate
import io.kotest.matchers.shouldBe
import kotlin.math.abs

class Day01 : DayTemplate() {
    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 11
            secondPart(testInputPart2) shouldBe 31
        }

        "Should solve the proper input" {
            println(firstPart(input))
            println(secondPart(input))
        }
    }

    fun firstPart(input: List<String>): Int {
        val parsed = parseInput(input)
        return parsed.first.sorted()
            .zip(parsed.second.sorted())
            .sumOf { abs(it.first - it.second) }
    }

    fun secondPart(input: List<String>): Int {
        val parsed = parseInput(input)
        return parsed.first.sumOf { a -> a * parsed.second.count { it == a } }
    }

    private fun parseInput(input: List<String>): Pair<List<Int>, List<Int>> {
        val ints = input.map { it.split("   ") }
            .map { it.map(String::toInt) }
        val firstList = ints.map { it[0] }
        val secondList = ints.map { it[1] }

        return Pair(firstList, secondList)
    }

}