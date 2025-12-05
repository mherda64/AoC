package year25.day05

import DayTemplate
import io.kotest.matchers.shouldBe

class Day05 : DayTemplate(year = "year25") {
    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 3
            secondPart(testInputPart2) shouldBe 14
        }

        "Should solve the proper input" {
            println(firstPart(input))
            println(secondPart(input))
        }
    }

    fun parseInput(input: List<String>): Database {
        val emptyLineIdx = input.indexOf("")
        val rawRanges = input.subList(0, emptyLineIdx)
        val parsedRanges = rawRanges.map {
            val (start, end) = it.split("-").map { numStr -> numStr.toLong() }
            start..end
        }
        val ids = input.subList(emptyLineIdx + 1, input.size).map { it.toLong() }
        return Database(parsedRanges, ids)
    }

    fun firstPart(input: List<String>): Int {
        val parsed = parseInput(input)
        println(parsed)

        val freshCount = parsed.ids.count { id ->
            parsed.ranges.any { range -> id in range }
        }
        return freshCount
    }

    fun secondPart(input: List<String>): Long {
        val parsed = parseInput(input)

        val rangesWithoutOverlaps = parsed.ranges.sortedBy { it.first }.fold(mutableListOf<LongRange>()) { acc, range ->
            if (acc.isEmpty() || acc.last().last < range.first - 1) {
                acc.add(range)
            } else {
                val lastRange = acc.removeAt(acc.size - 1)
                acc.add(lastRange.first..maxOf(lastRange.last, range.last))
            }
            acc
        }

        return rangesWithoutOverlaps.sumOf {
            it.last() - it.first() + 1
        }
    }
}

data class Database(
    val ranges: List<LongRange>,
    val ids: List<Long>
)