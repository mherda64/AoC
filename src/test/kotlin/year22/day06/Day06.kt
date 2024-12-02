package year22.day06

import year22.readInputAsLines

const val dayNumber = "06"

fun main() {

    fun solution(input: String, size: Int): Int =
        size + input.indexOf(
            input.windowed(size)
                .map { it.toCharArray().distinct() }
                .first { it.size == size }
                .joinToString("")
        )


    fun getInput(filename: String): String =
        readInputAsLines(filename).joinToString()

    val testInput = getInput("Day${dayNumber}_test")
    check(solution(testInput, 4) == 7)

    val input = getInput("Day${dayNumber}")
    println(solution(input, 4))
    println(solution(input, 14))
}