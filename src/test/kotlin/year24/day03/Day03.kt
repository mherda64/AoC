package year24.day03

import DayTemplate
import io.kotest.matchers.shouldBe

class Day03 : DayTemplate(secondTestPartTheSame = false) {
    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 161
            secondPart(testInputPart2) shouldBe 48
        }

        "Should solve the proper input" {
            println(firstPart(input))
            println(secondPart(input))
        }
    }

    private val firstPattern = "mul\\(([0-9]+),([0-9]+)\\)".toRegex()
    private val secondPattern = "(do\\(\\)|don't\\(\\)|mul\\(([0-9]+),([0-9]+)\\))".toRegex()

    private fun firstPart(input: List<String>): Int =
        input.flatMap {
            firstPattern.findAll(it).toList()
        }.map { Pair(it.groups[1]!!.value.toInt(), it.groups[2]!!.value.toInt()) }
            .sumOf { it.first * it.second }

    private fun secondPart(input: List<String>): Int {
        var enabled = true
        return input.map {
            secondPattern.findAll(it).toList()
        }.map { row ->
            row.filter {
                when (it.value) {
                    "do()" -> {
                        enabled = true
                        false
                    }

                    "don't()" -> {
                        enabled = false
                        false
                    }

                    else -> enabled
                }
            }
        }.flatten().map {
            Pair(it.groups[2]!!.value.toInt(), it.groups[3]!!.value.toInt())
        }.sumOf { it.first * it.second }
    }

}