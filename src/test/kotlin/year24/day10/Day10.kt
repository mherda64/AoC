package year24.day10

import DayTemplate
import io.kotest.matchers.shouldBe

class Day10 : DayTemplate() {

    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 36
            secondPart(testInputPart2) shouldBe 81
        }

        "Should solve the proper input" {
            println(firstPart(input))
            println(secondPart(input))
        }
    }

    private fun firstPart(input: List<String>): Int {
        val matrix = Matrix(input)
        matrix.nines.forEach { startingPos ->
            matrix.addTrailheadScore(startingPos, startingPos)
        }

        return matrix.trailheadScores.distinct().groupBy { it.first }.values.sumOf { it.count() }
    }

    private fun secondPart(input: List<String>): Int {
        val matrix = Matrix(input)
        matrix.nines.forEach { startingPos ->
            matrix.addTrailheadScore(startingPos, startingPos)
        }

        return matrix.trailheadRatings.values.sum()
    }

    class Matrix(
        input: List<String>
    ) {
        val matrix = input.map { it.map { if (it == '.') 99 else it.digitToInt() } }
        val numbers: List<Pair<Int, Pair<Int, Int>>> = matrix.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, number ->
                Pair(number, Pair(rowIndex, columnIndex))
            }
        }.flatten()
        val nines = numbers.filter { it.first == 9 }
            .map { it.second }
        val trailheadScores = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
        val trailheadRatings = mutableMapOf<Pair<Int, Int>, Int>()

        fun addTrailheadScore(endPoint: Pair<Int, Int>, point: Pair<Int, Int>) {
            val pointValue = matrix[point.first][point.second]
            if (pointValue == 0) {
                trailheadScores.add(Pair(point, endPoint))
                trailheadRatings[point] = trailheadRatings.getOrDefault(point, 0) + 1
                return
            }

            val neighbours = getNeighbours(point)
            neighbours.filter { matrix[it.first][it.second] == pointValue - 1 }
                .forEach { addTrailheadScore(endPoint, it) }

        }

        fun getNeighbours(point: Pair<Int, Int>): List<Pair<Int, Int>> {
            val neighbours = mutableListOf<Pair<Int, Int>>()

            for ((x, y) in listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))) {
                try {
                    if (matrix[point.first + y][point.second + x] != null)
                        neighbours.add(Pair(point.first + y, point.second + x))
                } catch (e: Exception) {

                }
            }

            return neighbours.filter { it != point }
        }
    }

}