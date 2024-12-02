package year21.day04

import year21.readInput

fun main() {

    fun processInput(input: List<String>): Pair<List<Int>, List<BingoBoard>> {
        val draws = input.first().split(",").map { it.toInt() }
        val boards = input.drop(1).chunked(6).map { board ->
            board.filter { it.isNotBlank() }.map { line ->
                line.trim()
                    .split(Regex("\\W+"))
                    .map { it.toInt() }
            }
        }.map { BingoBoard.fromCollection(it) }
        return Pair(draws, boards)
    }

    fun part1(input: List<String>): Int {
        val (draws, boards) = processInput(input)
        for (draw in draws) {
            for (board in boards) {
                board.markNumber(draw)
                if (board.isWinner()) {
                    return board.unmarkedSum() * draw
                }
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val (draws, boards) = processInput(input)
        var winCounter = 0
        for (draw in draws) {
            for (board in boards) {
                if (!board.isWinner()) {
                    board.markNumber(draw)
                    if (board.isWinner()) {
                        winCounter++
                        if (winCounter == boards.size)
                            return board.unmarkedSum() * draw
                    }
                }
            }
        }
        return 0
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

class BingoBoard(val fields: List<List<Field>>) {

    companion object {
        fun fromCollection(input: Collection<Collection<Int>>): BingoBoard {
            return BingoBoard(input.map { row -> row.map { elem -> Field(elem) }.toMutableList() })
        }
    }

    fun markNumber(number: Int) {
        for (row in fields) {
            row.map {
                if (it.value == number) it.mark()
            }
        }
    }

    fun isWinner(): Boolean {
        return checkRows() || checkColumns()
    }

    fun unmarkedSum(): Int {
        return fields.flatten().filterNot { it.marked }.sumOf { it.value }
    }

    private fun checkRows(): Boolean = fields.any { row -> row.all { it.marked } }

    private fun checkColumns(): Boolean {
        return fields.any { row ->
            List(row.size) { index -> index }.any { column ->
                (0..4).all { fields[it][column].marked }
            }
        }
    }

}

class Field(val value: Int, var marked: Boolean = false) {
    fun mark() {
        marked = true
    }
}