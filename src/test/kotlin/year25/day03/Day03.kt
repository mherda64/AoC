package year25.day03

import DayTemplate
import io.kotest.matchers.shouldBe

class Day03 : DayTemplate(year = "year25") {
    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 357
            secondPart(testInputPart2) shouldBe 3121910778619L
        }

        "Should solve the proper input" {
            println(firstPart(input))
            println(secondPart(input))
        }
    }

    fun firstPart(input: List<String>): Int {
        val parsed = parseInput(input)
        val largestDigits = parsed.map {
            findLargestPairOfDigits(it)
        }
        return largestDigits.sumOf { it }
    }

    private fun findLargestPairOfDigits(digits: List<Int>): Int {
        val firstLargestIndex = digits.indexOf(digits.maxOrNull()!!)
        val leftSublist = digits.subList(0, firstLargestIndex)
        val rightSublist = digits.subList(firstLargestIndex + 1, digits.size)
        val leftLargest = leftSublist.maxOrNull() ?: -1
        val rightLargest = rightSublist.maxOrNull() ?: -1
        val leftPossible = "${leftLargest}${digits[firstLargestIndex]}".toIntOrNull() ?: -1
        val rightPossible = "${digits[firstLargestIndex]}${rightLargest}".toIntOrNull() ?: -1
        return maxOf(leftPossible, rightPossible)
    }

    private fun parseInput(input: List<String>): List<List<Int>> =
        input.map {
            it.toCharArray().map { char -> char.digitToInt() }
        }

    fun secondPart(input: List<String>): Long {
        val parsed = parseInput(input)
        val largestDigits = parsed.map {
            findLargestTwelveDigits(it)
        }
        println(largestDigits)
        return largestDigits.sumOf { it }
    }

    private fun findLargestTwelveDigits(input: List<Int>): Long {
        val digits = mutableListOf<Int>()
        var currentIndex = 0
        var windowSize = input.size - 11
        while (currentIndex + windowSize <= input.size) {
            val window = input.subList(currentIndex, currentIndex + windowSize)
            val largestDigit = window.maxOrNull()!!
            digits.add(largestDigit)
            currentIndex += window.indexOf(largestDigit) + 1
            windowSize = input.size - currentIndex - (11 - digits.size)
        }
        return digits.joinToString("") { it.toString() }.toLong()
    }
}