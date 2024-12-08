package year24.day08

import DayTemplate
import io.kotest.matchers.shouldBe
import kotlin.math.abs

class Day08 : DayTemplate() {

    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 14
            secondPart(testInputPart2) shouldBe 34
        }

        "Should solve the proper input" {
            println(firstPart(input))
            println(secondPart(input))
        }
    }

    private fun firstPart(input: List<String>): Int {
        val matrix = Matrix(input)
        val antinodes = matrix.calculateAntinodes()
        return antinodes.count()
    }

    private fun secondPart(input: List<String>): Int {
        val matrix = Matrix(input)
        val antinodes = matrix.calculateAntinodesPart2()
        return antinodes.count()
    }

    // Forgive me, Father, for I have sinned.
    class Matrix(
        input: List<String>
    ) {
        val matrix = input.map { it.toCharArray().map { it.toString() }.toMutableList() }
        var letters: Map<String, List<Pair<Int, Int>>> = matrix.mapIndexed { rowIndex, row ->
            row.mapIndexedNotNull { columnIndex, letter ->
                if (letter != ".") {
                    Pair(letter, Pair(rowIndex, columnIndex))
                } else null
            }
        }.flatten()
            .groupBy { it.first }
            .mapValues { it.value.map { it.second } }

        fun calculateAntinodesPart2(): List<Pair<Int, Int>> {
            val letterCombinations = combineLetters()
            val antinodes = letterCombinations.map { combinations ->
                combinations.value.flatten().distinct()
                    .map {
                        val points = it.toList()
                        val a = points[0]
                        val b = points[1]
                        val distance = Pair(abs(b.first - a.first), abs(b.second - a.second))
                        when {
                            a.first > b.first && a.second > b.second ->
                                (-50..50).map {
                                    listOf(
                                        Pair(a.first + it * distance.first, a.second + it * distance.second),
                                        Pair(b.first - it * distance.first, b.second - it * distance.second)
                                    )
                                }.flatten()
                                    .filter { it.first in 0..<matrix.size && it.second in 0..<matrix[0].size }

                            a.first < b.first && a.second < b.second ->
                                (-50..50).map {
                                    listOf(
                                        Pair(a.first - it * distance.first, a.second - it * distance.second),
                                        Pair(b.first + it * distance.first, b.second + it * distance.second)
                                    )
                                }.flatten()
                                    .filter { it.first in 0..<matrix.size && it.second in 0..<matrix[0].size }

                            a.first > b.first && a.second < b.second ->
                                (-50..50).map {
                                    listOf(
                                        Pair(a.first + it * distance.first, a.second - it * distance.second),
                                        Pair(b.first - it * distance.first, b.second + it * distance.second)
                                    )
                                }.flatten()
                                    .filter { it.first in 0..<matrix.size && it.second in 0..<matrix[0].size }

                            a.first < b.first && a.second > b.second ->
                                (-50..50).map {
                                    listOf(
                                        Pair(a.first - it * distance.first, a.second + it * distance.second),
                                        Pair(b.first + it * distance.first, b.second - it * distance.second)
                                    )
                                }.flatten()
                                    .filter { it.first in 0..<matrix.size && it.second in 0..<matrix[0].size }

                            else -> throw IllegalStateException()
                        }
                    }
            }
            val filtered =
                antinodes.flatten().flatten().filter { it.first in 0..<matrix.size && it.second in 0..<matrix[0].size }
            return filtered.distinct()
        }

        fun calculateAntinodes(): List<Pair<Int, Int>> {
            val letterCombinations = combineLetters()
            val antinodes = letterCombinations.map { combinations ->
                combinations.value.flatten().distinct()
                    .map {
                        val points = it.toList()
                        val a = points[0]
                        val b = points[1]
                        val distance = Pair(abs(b.first - a.first), abs(b.second - a.second))
                        when {
                            a.first > b.first && a.second > b.second ->
                                listOf(
                                    Pair(a.first + distance.first, a.second + distance.second),
                                    Pair(b.first - distance.first, b.second - distance.second)
                                )

                            a.first < b.first && a.second < b.second ->
                                listOf(
                                    Pair(a.first - distance.first, a.second - distance.second),
                                    Pair(b.first + distance.first, b.second + distance.second)
                                )

                            a.first > b.first && a.second < b.second ->
                                listOf(
                                    Pair(a.first + distance.first, a.second - distance.second),
                                    Pair(b.first - distance.first, b.second + distance.second)
                                )

                            a.first < b.first && a.second > b.second ->
                                listOf(
                                    Pair(a.first - distance.first, a.second + distance.second),
                                    Pair(b.first + distance.first, b.second - distance.second)
                                )

                            else -> throw IllegalStateException()
                        }
                    }
            }
            val filtered =
                antinodes.flatten().flatten().filter { it.first in 0..<matrix.size && it.second in 0..<matrix[0].size }
            return filtered.distinct()
        }

        private fun combineLetters(): Map<String, List<List<Set<Pair<Int, Int>>>>> {
            val letterCombinations = letters.mapValues { letterPositions ->
                letterPositions.value.map { a ->
                    letterPositions.value.mapNotNull { b ->
                        if (a != b) {
                            setOf(a, b)
                        } else null
                    }
                }
            }
            return letterCombinations
        }
    }
}