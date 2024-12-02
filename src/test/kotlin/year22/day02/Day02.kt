package year22.day02

import year22.readInputAsLines

fun main() {

    val firstPartMapping = mapOf(
        "X" to "A",
        "Y" to "B",
        "Z" to "C"
    )

    val winningPairs = mapOf(
        "A" to "B",
        "B" to "C",
        "C" to "A"
    )

    fun countScore(round: Pair<String, String>): Int {
        val selectedShapeScore = when (round.second) {
            "A" -> 1
            "B" -> 2
            "C" -> 3
            else -> throw IllegalStateException("Nah")
        }
        val outcomeScore = when {
            winningPairs.map { Pair(it.key, it.value) }.any{it == round} -> 6
            round.first == round.second -> 3
            else -> 0
        }
        return selectedShapeScore + outcomeScore
    }

    fun getChosenSymbol(round: Pair<String, String>): String {
        val chosenSymbol = when (round.second) {
            "Y" -> round.first
            "Z" -> winningPairs[round.first]
            "X" -> winningPairs.entries.first { it.value == round.first }.key
            else -> throw IllegalStateException("Nah")
        }
        return chosenSymbol!!
    }

    fun part1(input: List<Pair<String, String>>): Int {
        return input.map { Pair(it.first, firstPartMapping[it.second]!!) }.sumOf { countScore(it) }
    }

    fun part2(input: List<Pair<String, String>>): Int {
        return input.map { Pair(it.first, getChosenSymbol(it)) }.sumOf { countScore(it) }
    }

    val testInput = readInputAsLines("Day02_test").map {
        it.split(" ")
            .let { Pair(it[0], it[1]) }
    }
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInputAsLines("Day02").map {
        it.split(" ")
            .let { Pair(it[0], it[1]) }
    }
    println(part1(input))
    println(part2(input))
}
