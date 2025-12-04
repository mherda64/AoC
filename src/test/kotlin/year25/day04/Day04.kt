package year25.day04

import DayTemplate
import Map
import io.kotest.matchers.shouldBe

class Day04 : DayTemplate(year = "year25") {
    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 13
            secondPart(testInputPart2) shouldBe 43
        }

        "Should solve the proper input" {
            println(firstPart(input))
            println(secondPart(input))
        }
    }

    fun parseInput(unparsedInput: List<String>): Map<Point> {
        val preParsed = unparsedInput.map {
            it.toCharArray().map { char -> Point(char.toString()) }
        }
        return Map(preParsed)
    }

    fun firstPart(input: List<String>): Int {
        val map = parseInput(input)
        val (width, height) = map.size()
        var accessibleRolls = 0
        for (y in 0..height) {
            for (x in 0..width) {
                if (map.get(x, y)?.value == "@" && map.isRollAccessible(x, y)) {
                    accessibleRolls += 1
                }
            }
        }

        map.printMarked()

        return accessibleRolls
    }

    private fun Map<Point>.isRollAccessible(x: Int, y: Int): Boolean {
        val adjacentDeltas = listOf(
            Pair(-1, -1),
            Pair(0, -1),
            Pair(1, -1),
            Pair(-1, 0),
            Pair(1, 0),
            Pair(-1, 1),
            Pair(0, 1),
            Pair(1, 1),
        )
        val adjacentRolls = adjacentDeltas
            .mapNotNull { (dx, dy) -> get(x +dx, y + dy) }
            .filter { it.value == "@" }
        val adjacentRollsCount = adjacentRolls
            .count()

        val accessible = adjacentRollsCount < 4
        if (accessible) {
            get(x, y)?.marked = true
        }
        return accessible
    }

    fun secondPart(input: List<String>): Int {
        var sumOfRemovedRolls = 0
        var map = parseInput(input)
        do {
            val (width, height) = map.size()
            var accessibleRolls = 0
            for (y in 0..height) {
                for (x in 0..width) {
                    if (map.get(x, y)?.value == "@" && map.isRollAccessible(x, y)) {
                        accessibleRolls += 1
                    }
                }
            }
            map.printMarked()
            map = map.withoutMarked()
            sumOfRemovedRolls += accessibleRolls
        } while (accessibleRolls > 0)

        return sumOfRemovedRolls
    }

    fun Map<Point>.printMarked() {
        grid.forEach { row ->
            row.forEach { point ->
                if (point.marked) {
                    print("x")
                } else {
                    print(point.value)
                }
            }
            println()
        }
    }

    fun Map<Point>.withoutMarked(): Map<Point> {
        val newGrid = grid.map { row ->
            row.map { point ->
                if (point.marked) {
                    Point(".")
                } else {
                    point
                }
            }
        }
        return Map(newGrid)
    }

}

data class Point(
    val value: String,
    var marked: Boolean = false
)