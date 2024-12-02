package year22.day05

import year22.readInputAsLines
import java.util.*

const val dayNumber = "05"

fun main() {

    data class Move(
        val number: Int,
        val from: Int,
        val to: Int
    )

    fun part1(stacks: List<Deque<String>>, moves: List<Move>): String {
        for (move in moves) {
            repeat(move.number) {
                stacks[move.to].push(stacks[move.from].pop())
            }
        }
        return stacks.joinToString { it.pop() }
            .replace("[", "")
            .replace("]", "")
            .replace(" ", "")
            .replace(",", "")
    }

    fun part2(stacks: List<Deque<String>>, moves: List<Move>): String {
        for (move in moves) {
            (1..move.number).map { stacks[move.from].pop() }
                .reversed()
                .forEach { stacks[move.to].push(it) }

        }
        return stacks.joinToString { it.pop() }
            .replace("[", "")
            .replace("]", "")
            .replace(" ", "")
            .replace(",", "")
    }

    fun toStacks(input: List<List<IndexedValue<String>>>): List<Deque<String>> {
        val stacks: List<Deque<String>> = (1..input.maxOf { it.size }).map { ArrayDeque() }
        input.flatten().forEach { (index, element) -> if (element.trim().isNotBlank()) stacks[index].push(element) }
        return stacks.map { ArrayDeque(it.reversed()) }
    }

    fun getInput(stacksFile: String, movesFile: String): Pair<List<Deque<String>>, List<Move>> {
        val stacks = toStacks(readInputAsLines(stacksFile)
            .map { it.chunked(4) }
            .map { it.withIndex().toList() })
        val moves = readInputAsLines(movesFile)
            .map { "move (\\d+) from (\\d+) to (\\d+)".toRegex().find(it)!!.destructured }
            .map { Move(it.component1().toInt(), it.component2().toInt() - 1, it.component3().toInt() - 1) }
        return Pair(stacks, moves)
    }

    val (testStacks, testMoves) = getInput("Day${dayNumber}_test_a", "Day${dayNumber}_test_b")

    check(part1(testStacks, testMoves) == "CMZ")

    val (testStacksPartTwo, testMovesPartTwo) = getInput("Day${dayNumber}_test_a", "Day${dayNumber}_test_b")
    check(part2(testStacksPartTwo, testMovesPartTwo) == "MCD")

    val (stacks, moves) = getInput("Day${dayNumber}_stacks", "Day${dayNumber}_moves")
    println(part1(stacks, moves))

    val (stacksPartTwo, movesPartTwo) = getInput("Day${dayNumber}_stacks", "Day${dayNumber}_moves")
    println(part2(stacksPartTwo, movesPartTwo))
}