package year25.day02

import DayTemplate
import io.kotest.matchers.shouldBe

class Day02 : DayTemplate(year="year25") {
    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 1227775554L
            secondPart(testInputPart2) shouldBe 4174379265L
        }

        "Should solve the proper input" {
            println(firstPart(input))
            println(secondPart(input))
        }
    }

    fun firstPart(input: List<String>): Long {
        val ranges = parseInput(input)
        println(ranges)

        var invalidIdsSum = 0L

        for (range in ranges) {
            for (i in range.first..range.second) {
                val stringVal = i.toString()
                if (stringVal.length % 2 != 0) {
                    continue
                }
                val halfs = stringVal.windowed(stringVal.length / 2, stringVal.length / 2)
                if (halfs[0] == halfs[1]) {
                    invalidIdsSum += i
                }
            }
        }

        return invalidIdsSum
    }

    private fun parseInput(input: List<String>): List<Pair<Long, Long>> =
        input.map { it.split(",") }.flatten().map { it.split("-") }.map { Pair(it[0].toLong(), it[1].toLong()) }

    fun secondPart(input: List<String>): Long {
        val ranges = parseInput(input)
        println(ranges)

        var invalidIdsSum = 0L

        for (range in ranges) {
            for (i in range.first..range.second) {
                val stringVal = i.toString()

                for (r in 1..stringVal.length / 2) {
                    if (stringVal.length % r != 0) {
                        continue
                    }
                    val split = stringVal.windowed(r,r).toSet()

                    if (split.size == 1) {
                        invalidIdsSum += i
                        break
                    }
                }
            }
        }

        return invalidIdsSum
    }
}