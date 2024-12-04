package year24.day04

import DayTemplate
import io.kotest.matchers.shouldBe

class Day04 : DayTemplate() {

    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 18
            secondPart(testInputPart2) shouldBe 9
        }

        "Should solve the proper input" {
            println(firstPart(input))
            println(secondPart(input))
        }
    }

    private fun firstPart(input: List<String>): Int {
        val matrix = Matrix(input)
        var counter = 0
        (0..matrix.rows - 1).forEach { row ->
            (0..matrix.columns - 1).forEach { column ->
                counter += matrix.checkCellFirstPart(row, column, "XMAS")
            }
        }

        return counter
    }

    private fun secondPart(input: List<String>): Int {
        val matrix = Matrix(input)
        var counter = 0
        (1..matrix.rows - 2).forEach { row ->
            (1..matrix.columns - 2).forEach { column ->
                if (matrix.checkCellSecondPart(row, column, "MAS"))
                    counter++
            }
        }

        return counter
    }

    class Matrix(
        input: List<String>
    ) {
        val matrix = input.map { it.toCharArray().map { it.toString() } }
        val rows: Int
            get() = matrix.size
        val columns: Int
            get() = matrix[0].size

        fun checkCellFirstPart(row: Int, column: Int, word: String): Int {
            var counter = 0

            // check horizontal
            if (column <= columns - word.length) {
                if (matrix[row][column] + matrix[row][column + 1] + matrix[row][column + 2] + matrix[row][column + 3] == word)
                    counter++
                if (matrix[row][column + 3] + matrix[row][column + 2] + matrix[row][column + 1] + matrix[row][column] == word)
                    counter++
            }

            // check vertical
            if (row <= rows - word.length) {
                if (matrix[row][column] + matrix[row + 1][column] + matrix[row + 2][column] + matrix[row + 3][column] == word)
                    counter++
                if (matrix[row + 3][column] + matrix[row + 2][column] + matrix[row + 1][column] + matrix[row][column] == word)
                    counter++
            }

            // check diagonal
            if (row <= rows - word.length && column <= columns - word.length) {
                if (matrix[row][column] + matrix[row + 1][column + 1] + matrix[row + 2][column + 2] + matrix[row + 3][column + 3] == word)
                    counter++
                if (matrix[row + 3][column + 3] + matrix[row + 2][column + 2] + matrix[row + 1][column + 1] + matrix[row][column] == word)
                    counter++
            }

            // check other diagonal
            if (row >= 3 && column <= columns - word.length) {
                if (matrix[row][column] + matrix[row - 1][column + 1] + matrix[row - 2][column + 2] + matrix[row - 3][column + 3] == word)
                    counter++
                if (matrix[row - 3][column + 3] + matrix[row - 2][column + 2] + matrix[row - 1][column + 1] + matrix[row][column] == word)
                    counter++
            }

            return counter
        }

        fun checkCellSecondPart(row: Int, column: Int, word: String): Boolean =
            ((
                    matrix[row - 1][column - 1] + matrix[row][column] + matrix[row + 1][column + 1] == word ||
                    matrix[row - 1][column - 1] + matrix[row][column] + matrix[row + 1][column + 1] == word.reversed()
            )
            &&
            (
                    matrix[row - 1][column + 1] + matrix[row][column] + matrix[row + 1][column - 1] == word ||
                    matrix[row - 1][column + 1] + matrix[row][column] + matrix[row + 1][column - 1] == word.reversed()
            ))

    }
}