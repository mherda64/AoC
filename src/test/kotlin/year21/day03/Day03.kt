package year21.day03

import year21.readInput

fun main() {

    fun part1(input: List<String>): Int {
        val epsilonRate = buildString {
            for (column in input[0].indices) {
                val (zeroes, ones) = input.countBitsInColumn(column)
                append(if (zeroes > ones) '0' else '1')
            }
        }
        val gammaRate = epsilonRate.invertBinaryString()
        return epsilonRate.toInt(2) * gammaRate.toInt(2)
    }

    fun part2(input: List<String>): Int {
        fun rating(type: RatingType): Int {
            val columns = input[0].indices
            var candidates = input
            for (column in columns) {
                val (zeroes, ones) = candidates.countBitsInColumn(column)
                val mostCommon = if (zeroes > ones) '0' else '1'
                candidates = candidates.filter {
                    when (type) {
                        RatingType.OXYGEN -> it[column] == mostCommon
                        RatingType.CO2 -> it[column] != mostCommon
                    }
                }
                if (candidates.size == 1) break
            }
            return candidates.single().toInt(2)
        }

        return rating(RatingType.OXYGEN) * rating(RatingType.CO2)
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

private enum class RatingType {
    OXYGEN,
    CO2
}

private fun String.invertBinaryString(): String =
    this
        .asIterable()
        .joinToString("") { if (it == '0') "1" else "0" }


private fun List<String>.countBitsInColumn(column: Int): BitCount {
    var zeroes = 0
    var ones = 0
    for (line in this) {
        if (line[column] == '0') zeroes++ else ones++
    }
    return BitCount(zeroes, ones)
}

private data class BitCount(val zeroes: Int, val ones: Int)