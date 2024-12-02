package day05

import year21.readInput

fun main() {

    val pattern = Regex("(\\d*),(\\d*) -> (\\d*),(\\d*)")

    fun processInput(input: List<String>): List<Pair<Point, Point>> {
        return input.map {
            val (x1, y1, x2, y2) = pattern.find(it)!!.destructured
            Pair(Point(x1.toInt(), y1.toInt()), Point(x2.toInt(), y2.toInt()))
        }
    }

    fun part1(input: List<Pair<Point, Point>>): Int {
        val max = input.maxOf { maxOf(maxOf(it.first.x, it.first.y), maxOf(it.second.x, it.second.y)) }
        val map = Map(max + 1)
        map.mark(input.filter { (first, second) -> first.x == second.x || first.y == second.y })
        return map.countOverlapping()
    }

    fun part2(input: List<Pair<Point, Point>>): Int {
        val max = input.maxOf { maxOf(maxOf(it.first.x, it.first.y), maxOf(it.second.x, it.second.y)) }
        val map = Map(max + 1)
        map.mark(input)
        return map.countOverlapping()
    }

    val testInput = readInput("Day05_test")
    check(part1(processInput(testInput)) == 5)
    check(part2(processInput(testInput)) == 12)

    val input = readInput("Day05")
    println(part1(processInput(input)))
    println(part2(processInput(input)))
}

class Map(val size: Int) {

    val map: Array<Array<Int>> = Array(size) { Array(size) { 0 } }

    fun mark(pairs: List<Pair<Point, Point>>) {
        pairs.map { (first, second) ->
            when {
                first.x == second.x ->
                    (if (first.y > second.y) first.y downTo second.y else first.y..second.y)
                        .map { map[first.x][it] += 1 }
                first.y == second.y ->
                    (if (first.x > second.x) first.x downTo second.x else first.x..second.x)
                        .map { map[it][first.y] += 1 }
                else -> {
                    val xRange = if (first.x > second.x) first.x downTo second.x else first.x..second.x
                    val yRange = if (first.y > second.y) first.y downTo second.y else first.y..second.y
                    xRange.zip(yRange).map { map[it.first][it.second] += 1 }
                }
            }
        }
    }

    fun countOverlapping(): Int {
        var counter = 0
        for ((rowIndex, row) in map.withIndex()) {
            for (column in row.indices) {
                counter += if (map[rowIndex][column] > 1) 1 else 0
            }
        }
        return counter
    }
}

data class Point(val x: Int, val y: Int)
