package year23.day01

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import year23.readInputAsLines

class Day01 : FreeSpec() {
    val dayNo = "01"
    val testInputPart1 = readInputAsLines("Day${dayNo}_test_part1")
    val testInputPart2 = readInputAsLines("Day${dayNo}_test_part2")
    val input = readInputAsLines("Day${dayNo}")
    val regexp = "(\\d|one|two|three|four|five|six|seven|eight|nine)".toRegex()
    val reversedRegexp = "(enin|thgie|neves|xis|evif|ruof|eerht|owt|eno|\\d)".toRegex()

    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 142
            secondPart(testInputPart2) shouldBe 281
        }

        "Should solve the proper input" {
            println(
                firstPart(input)
            )
            println(
                secondPart(input)
            )
        }

        "Should solve edge case" {
            val testInput = listOf("5flpvgjsmbbxgsgh2threesqjnoneightdt")
            secondPart(testInput) shouldBe 58
        }
    }

    fun firstPart(input: List<String>): Int =
        input.map { it.filter { it.isDigit() } }
            .map { it.first().toString() + it.last() }
            .sumOf { it.toInt() }

    fun secondPart(input: List<String>) =
        firstPart(
            input.map { line ->
                val firstDigit = regexp.find(line)!!.value
                val lastDigit = reversedRegexp.find(line.reversed())!!.value.reversed()

                assertDigit(firstDigit) + assertDigit(lastDigit)
            }
        )


    private fun assertDigit(value: String) = if (value.length > 1) mapWordToDigit(value) else value


    private fun mapWordToDigit(input: String): String =
        when (input) {
            "one" -> "1"
            "two" -> "2"
            "three" -> "3"
            "four" -> "4"
            "five" -> "5"
            "six" -> "6"
            "seven" -> "7"
            "eight" -> "8"
            "nine" -> "9"
            "zero" -> "0"
            else -> throw IllegalStateException()
        }
}