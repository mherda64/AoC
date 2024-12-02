package year22.day04

import year22.readInputAsLines

const val dayNumber = "04"

fun main() {

    fun part1(input: List<Pair<IntRange, IntRange>>): Int =
        input.count { (first: IntRange, second: IntRange) ->
            first.toSet().containsAll(second.toSet()) || second.toSet().containsAll(first.toSet())
        }

    fun part2(input: List<Pair<IntRange, IntRange>>): Int =
        input.count { (first: IntRange, second: IntRange) ->
            first.toSet().intersect(second.toSet()).isNotEmpty()
        }


    fun getInput(filename: String) = readInputAsLines(filename)
        .map {
            val (aMin, aMax, bMin, bMax) = "(\\d+)-(\\d+),(\\d+)-(\\d+)".toRegex().find(it)!!.destructured
            Pair(aMin.toInt()..aMax.toInt(), bMin.toInt()..bMax.toInt())
        }

    val testInput = getInput("Day${dayNumber}_test")

    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = getInput("Day${dayNumber}")

    println(part1(input))
    println(part2(input))
}