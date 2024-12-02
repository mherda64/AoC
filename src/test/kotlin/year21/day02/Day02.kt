package year21.day02

import year21.readInput


fun main() {
    data class Operation(val direction: String, val amount: Int)

    fun part1(input: List<String>): Int {
        var position = 0
        var depth = 0
        input.map { it.split(" ") }
            .map { Operation(it[0], it[1].toInt()) }
            .forEach { (direction, amount) ->
                when (direction) {
                    "forward" -> position += amount
                    "down" -> depth += amount
                    "up" -> depth -= amount
                }
            }
        return position * depth
    }

    fun part2(input: List<String>): Int {
        var position = 0
        var depth = 0
        var aim = 0
        input.map { it.split(" ") }
            .map { Operation(it[0], it[1].toInt()) }
            .forEach { (direction, amount) ->
                when (direction) {
                    "forward" -> {
                        position += amount
                        depth += amount * aim
                    }
                    "down" -> aim += amount
                    "up" -> aim -= amount
                }
            }
        return position * depth
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
