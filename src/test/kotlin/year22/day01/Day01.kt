package year22.day01

import year22.readInput

fun main() {

    fun part1(input: List<List<Int>>): Int {
        return input.map { it.sum() }.maxOf { it }
    }

    fun part2(input: List<List<Int>>): Int {
        return input.map { it.sum() }.sortedDescending().take(3).sum()
    }

    val testInput = readInput("Day01_test").readText().split("\n\n").map { it.split("\n").map { it.toInt() } }
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01").readText().split("\n\n").map { it.split("\n").map { it.toInt() } }
    println(part1(input))
    println(part2(input))
}
