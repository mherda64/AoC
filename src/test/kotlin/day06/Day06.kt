package day06

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import readInputAsLines

class Day06 : FreeSpec() {
    val dayNo = "06"
    val testInput = readInputAsLines("Day${dayNo}_test")
    val input = readInputAsLines("Day${dayNo}")

    init {
        "Should solve test input" {
            part1(testInput) shouldBe 288
            part2(testInput) shouldBe 71503
        }

        "Should solve proper input" {
            println(part1(input))
            println(part2(input))
        }
    }

    fun part1(input: List<String>): Int {
        val timeDistanceList = input.map { it.split(":")[1].trim().split(" +".toRegex()).map { it.trim().toLong() } }
        val timeDistance = timeDistanceList[0].zip(timeDistanceList[1])

        val counts = timeDistance.map(::countWins)

        return counts.reduce {acc, next -> acc * next }
    }

    fun part2(input: List<String>): Int {
        val timeDistanceList = input.map { it.split(":")[1].trim().replace(" ", "").toLong() }
        val timeDistance = Pair(timeDistanceList[0], timeDistanceList[1])

        return countWins(timeDistance)
    }

    fun countWins(timeDistance: Pair<Long, Long>): Int {
        val (time, distance) = timeDistance
        return (0..time).count {
            distance < it * (time - it)
        }
    }
}