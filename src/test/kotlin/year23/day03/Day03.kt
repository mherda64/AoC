package year23.day03

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import year23.readInputAsLines

class Day03 : FreeSpec() {
    val dayNo = "03"
    val testInput = readInputAsLines("Day${dayNo}_test")
    val input = readInputAsLines("Day${dayNo}")
    val numberRegexp = "(\\d+)".toRegex()
    val gearsRegexp = "(\\*)".toRegex()

    init {
        "Should solve test input" {
            part1(testInput) shouldBe 4361
            part2(testInput) shouldBe 467835
        }

        "Should solve proper input" {
            println(part1(input))
            println(part2(input))
        }
    }

    fun part1(input: List<String>): Int {
        input.forEachIndexed { lineNumber, line ->
            val foundNumbers = numberRegexp.findAll(line).toList()
            foundNumbers.map { Pair(it, isAdjacent(input, lineNumber, it)) }
        }
        val output = input.mapIndexed { lineNumber, line ->
            val foundNumbers = numberRegexp.findAll(line).toList()
            foundNumbers.map { Pair(it.value, isAdjacent(input, lineNumber, it)) }
        }.flatten()
            .filter { it.second }
        return output.sumOf { it.first.toInt() }
    }

    private fun isAdjacent(lines: List<String>, lineNumber: Int, it: MatchResult): Boolean {
        val maxLength = lines[lineNumber].length
        // top
        if (lineNumber != 0) {
            val diagonalRange = (it.range.first - 1..it.range.last + 1)
            diagonalRange.forEach {
                if (it >= 0 && it <= maxLength - 1) {
                    val value = lines[lineNumber - 1][it]
                    if (isSymbol(value))
                        return true
                }
            }
        }
        // bottom
        if (lineNumber != lines.size - 1) {
            val diagonalRange = (it.range.first - 1..it.range.last + 1)
            diagonalRange.forEach {
                if (it >= 0 && it <= maxLength - 1) {
                    val value = lines[lineNumber + 1][it]
                    if (isSymbol(value))
                        return true
                }
            }
        }
        // left
        if (it.range.first != 0) {
            val value = lines[lineNumber][it.range.first - 1]
            if (isSymbol(value))
                return true
        }
        // right
        if (it.range.last != lines[lineNumber].length - 1) {
            val value = lines[lineNumber][it.range.last + 1]
            if (isSymbol(value))
                return true
        }
        return false
    }

    private fun isSymbol(char: Char) =
        !char.isDigit() && char != '.'

    fun part2(input: List<String>): Int {
        val numbers = input.mapIndexed { lineNumber, line ->
            numberRegexp.findAll(line).toList().map { Number(it.value.toInt(), it.range, lineNumber) }
        }.flatten()
        val maybeGears = input.mapIndexed { lineNumber, line ->
            gearsRegexp.findAll(line).toList().map { Gear(it.range.first, lineNumber) }
        }.flatten()
        val gears = maybeGears.map { isGear(it, numbers) }.filter { it.first }
        return gears.sumOf { it.second }
    }

    private fun isGear(gear: Gear, numbers: List<Number>): Pair<Boolean, Int> {
        val adjacentNumbers = numbers.filter {
            it.isAdjacent(gear)
        }
        if (adjacentNumbers.count() >= 2) {
            return Pair(true, adjacentNumbers.fold(1){acc: Int, number -> acc * number.value})
        }
        return Pair(false, 0)
    }

    data class Gear(
        val xPos: Int,
        val yPos: Int
    )

    data class Number(
        val value: Int,
        val xRange: IntRange,
        val yPos: Int
    ) {
        fun isAdjacent(gear: Gear): Boolean {
            return (xRange.first - 1 .. xRange.last + 1).contains(gear.xPos) && (yPos-1 .. yPos+1).contains(gear.yPos)
        }
    }
}