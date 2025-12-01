package year25.day01

import DayTemplate
import io.kotest.matchers.shouldBe

class Day01 : DayTemplate(year="year25") {
    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 3
            secondPart(testInputPart2) shouldBe 6
        }

        "Should solve the proper input" {
            println(firstPart(input))
            println(secondPart(input))
        }
    }

    fun firstPart(input: List<String>): Int {
        val parsed = parseInput(input)
        val result = parsed.fold(Pair(50, 0)) {
            (acc, zeroes), (operation, value) ->
            val acc1 = when (operation) {
                "L" -> (acc - value) % 100
                "R" -> (acc + value) % 100
                else -> error("Unknown operation $operation")
            }
            if (acc1 == 0) {
                Pair(acc1, zeroes + 1)
            } else {
                Pair(acc1, zeroes)
            }
        }
        return result.second
    }

    private fun parseInput(input: List<String>): List<Pair<String, Int>> =
        input.map {
            it[0].toString() to it.substring(1).toInt()
        }

    fun secondPart(input: List<String>): Int {
        val parsed = parseInput(input)
        var pos = 50
        var zeroes = 0
        for ((direction, value) in parsed) {
            for (i in 1..value) {
                pos = when (direction) {
                    "L" -> (pos - 1 + 100) % 100
                    "R" -> (pos + 1) % 100
                    else -> error("Unknown operation $direction")
                }
                if (pos == 0)
                    zeroes += 1
            }
        }
        return zeroes
    }
}