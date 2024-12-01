package year23.day09

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import year23.readInputAsLines

class Day09 : FreeSpec() {
    val testInput = readInputAsLines("${this::class.simpleName}_test")
    val input = readInputAsLines("${this::class.simpleName}")

    init {
        "Should solve test input" {
            part1(testInput) shouldBe 114
        }

        "Should solve proper input" {
            println(part1(input))
            println(part2(input))
        }
    }

    fun part1(input: List<String>): Int {
        val ints = input.map {
            it.split(" ").map(String::toInt)
        }

        val extrapolated = ints.map(::extrapolate)

        return extrapolated.sum()
    }

    fun part2(input: List<String>): Int {
        val ints = input.map {
            it.split(" ").map(String::toInt).reversed()
        }

        val extrapolated = ints.map(::extrapolate)

        return extrapolated.sum()
    }

    fun extrapolate(ints: List<Int>): Int {
        val history = mutableListOf<MutableList<Int>>()
        do {
            if (history.isEmpty())
                history.add(ints.windowed(2).map { it[1] - it[0] }.toMutableList())
            else
                history.add(history.last().windowed(2).map { it[1] - it[0] }.toMutableList())

        } while (history.last().any { it != 0 })

        val historyReversed = history.reversed()
        val extrapolated = mutableListOf(0)
        historyReversed.drop(1).forEach { list ->
            extrapolated.add(extrapolated.last() + list.last())
        }
        return extrapolated.last() + ints.last()
    }
}