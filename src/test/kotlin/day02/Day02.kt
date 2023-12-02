package day02

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import readInputAsLines

class Day02 : FreeSpec() {
    val dayNo = "02"
    val testInput = readInputAsLines("Day${dayNo}_test")

    val input = readInputAsLines("Day${dayNo}")
    val regexp = "(\\d+ (blue|red|green))".toRegex()

    init {
        "Should work on test data" {
            part1(testInput) shouldBe 8
            part2(testInput) shouldBe 2286
        }

        "Should work on proper data" {
            println(part1(input))
            println(part2(input))
        }
    }

    fun part1(input: List<String>): Int =
        input.map { isPossible(it) }
            .filter { it.second }
            .sumOf { it.first }

    fun isPossible(input: String, r: Int = 12, g: Int = 13, b: Int = 14): Pair<Int, Boolean> {
        val gameId = input.split(":")[0].split(" ")[1].toInt()
        val all = regexp.findAll(input).toList().map { it.value }
        val redNotPossible = all.filter { it.contains("red") }.map { it.split(" ")[0].toInt() }.any { it > r }
        val greenNotPossible = all.filter { it.contains("green") }.map { it.split(" ")[0].toInt() }.any { it > g }
        val blueNotPossible = all.filter { it.contains("blue") }.map { it.split(" ")[0].toInt() }.any { it > b }
        return Pair(gameId, !(redNotPossible || greenNotPossible || blueNotPossible))
    }

    fun part2(input: List<String>): Int =
        input.map(::getRequiredCubes)
            .map { it.first * it.second * it.third }
            .sum()

    fun getRequiredCubes(input: String): Triple<Int, Int, Int> {
        val all = regexp.findAll(input).toList().map { it.value }
        val redMax = all.filter { it.contains("red") }.map { it.split(" ")[0].toInt() }.max()
        val greenMax = all.filter { it.contains("green") }.map { it.split(" ")[0].toInt() }.max()
        val blueMax = all.filter { it.contains("blue") }.map { it.split(" ")[0].toInt() }.max()
        return Triple(redMax, greenMax, blueMax)
    }

}