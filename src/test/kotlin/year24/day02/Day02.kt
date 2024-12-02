package year24.day02

import DayTemplate
import dropIndex
import io.kotest.matchers.shouldBe
import kotlin.math.abs

class Day02 : DayTemplate("02") {
    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 2
            secondPart(testInputPart2) shouldBe 4
        }

        "Should solve the proper input" {
            println(firstPart(input))
            println(secondPart(input))
        }
    }

    private fun secondPart(input: List<String>) =
        parseInput(input).map { row ->
            isSafe(row) || isSafeWithoutSingleLevel(row)
        }.count { it }

    private fun isSafeWithoutSingleLevel(row: List<Int>): Boolean {
        for (i in row.indices) {
            if (isSafe(row.dropIndex(i)))
                return true
        }
        return false
    }

    private fun firstPart(input: List<String>): Int =
        parseInput(input).map { row -> isSafe(row) }.count { it }

    private fun isSafe(input: List<Int>) =
        (isIncreasing(input) || isDecreasing(input)) && isDifferSafe(input)

    private fun isIncreasing(input: List<Int>) =
        input.windowed(2)
            .all { it[0] > it[1] }

    private fun isDecreasing(input: List<Int>) =
        input.windowed(2)
            .all { it[0] < it[1] }

    private fun isDifferSafe(input: List<Int>) =
        input.windowed(2)
            .all { abs(it[0] - it[1]) in 1..3 }

    private fun parseInput(input: List<String>) =
        input.map { it.split(" ") }
            .map { it.map(String::toInt) }

}