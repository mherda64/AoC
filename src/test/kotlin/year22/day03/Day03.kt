package year22.day03

import year22.readInputAsLines

const val dayNumber = "03"

val priorities = (1..26).associateWith { (it + 96).toChar() }
    .plus((27..52).associateWith { (it + 38).toChar() })
    .entries
    .associateBy({ it.value }) { it.key }

data class Rucksack(
    val first: Set<Char>,
    val second: Set<Char>
) {
    fun getCommonItem(): Char = first.intersect(second).first()

    fun getCommonItemPriority(): Int = priorities[getCommonItem()]!!

    fun getCharSet(): Set<Char> = first.plus(second)
}

fun main() {

    fun part1(input: List<Rucksack>): Int {
        return input.sumOf { it.getCommonItemPriority() }
    }

    fun part2(input: List<List<Rucksack>>): Int {
        return input.sumOf {
            val char = it.fold(it.first().getCharSet()) { acc, rucksack -> acc.intersect(rucksack.getCharSet()) }
                .first()
            priorities[char]!!
        }
    }

    fun getInput(filename: String) = readInputAsLines(filename)
        .map { it.chunked(it.length / 2) }
        .map {
            Rucksack(
                it[0].toCharArray().toSet(),
                it[1].toCharArray().toSet()
            )
        }

    val testInput = getInput("Day${dayNumber}_test")

    check(part1(testInput) == 157)
    check(part2(testInput.chunked(3)) == 70)

    val input = getInput("Day${dayNumber}")

    println(part1(input))
    println(part2(input.chunked(3)))
}