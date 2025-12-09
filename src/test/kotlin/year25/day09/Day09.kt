package year25.day09

import DayTemplate
import io.kotest.matchers.shouldBe
import kotlin.math.abs

class Day09 : DayTemplate(year = "year25") {
    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 50
            secondPart(testInputPart2) shouldBe 24
        }

        "Should solve the proper input" {
            println(firstPart(input))
            println(secondPart(input))
        }
    }

    fun calcArea(first: Point, second: Point): Long =
        (abs(first.x - second.x) + 1) * (abs(first.y - second.y) + 1)

    fun parseInput(input: List<String>): List<Point> {
        return input.map {
            val points = it.split(",").map(String::toLong)
            Point(points[0], points[1])
        }
    }

    fun firstPart(input: List<String>): Long {
        val points = parseInput(input)
        var maxArea = Long.MIN_VALUE
        for (i in points) {
            for (j in points) {
                if (i != j) {
                    val area = calcArea(i, j)
                    if (area > maxArea) {
                        maxArea = area
                    }
                }
            }
        }

        return maxArea
    }

    fun secondPart(input: List<String>): Long {
        val points = parseInput(input)
        val lines = points.windowed(2).map { Pair(it[0], it[1]) } + Pair(points.last(), points.first())
        val allPointsInLines = lines.flatMap {
            if (it.first.x == it.second.x) {
                // vertical line
                (minOf(it.first.y, it.second.y)..maxOf(it.first.y, it.second.y)).map { y -> Point(it.first.x, y) }
            } else {
                // horizontal line
                (minOf(it.first.x, it.second.x)..maxOf(it.first.x, it.second.x)).map { x -> Point(x, it.first.y) }
            }
        }
        var maxArea = Long.MIN_VALUE
        for (i in points) {
            for (j in points) {
                if (i != j) {
                    val area = calcArea(i, j)
                    if (area > maxArea && noLinesIntersectWithRectangle(i, j, allPointsInLines)) {
                        maxArea = area
                    }
                }
            }
        }

        return maxArea
    }

    fun noLinesIntersectWithRectangle(first: Point, second: Point, allPointsInLines: List<Point>): Boolean {
        val minX = minOf(first.x, second.x)
        val maxX = maxOf(first.x, second.x)
        val minY = minOf(first.y, second.y)
        val maxY = maxOf(first.y, second.y)

        for ((px, py) in allPointsInLines) {
            if (px in ((minX+1)..<maxX) && py in ((minY+1)..<maxY)) {
                return false
            }
        }

        return true
    }

    data class Point(val x: Long, val y: Long)

}

