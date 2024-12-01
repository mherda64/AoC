package year23.day08

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import year23.readInputAsLines

class Day08 : FreeSpec() {
    val testInputPart1 = readInputAsLines("${this::class.simpleName}_test_part1")
    val testInputPart2 = readInputAsLines("${this::class.simpleName}_test_part2")
    val input = readInputAsLines("${this::class.simpleName}")
    val nodeRegex = "([A-Z\\d]{3}) = \\(([A-Z\\d]{3}), ([A-Z\\d]{3})\\)".toRegex()

    init {
        "Should solve test input" {
            part1(testInputPart1) shouldBe 6
            part2(testInputPart2) shouldBe 6
        }

        "Should solve proper input" {
//            println(part1(input))
            println(part2(input))
        }
    }

    fun part1(input: List<String>): Int {
        val rightLeftIterator = RightLeftIterator(input[0])
        val nodes = input.drop(2).map {
            val values = nodeRegex.findAll(it).toList()[0].groupValues
            values[1] to Pair(values[2], values[3])
        }.associate { it.first to it.second }

        return countSteps("AAA", rightLeftIterator, nodes, 0)
    }

    fun part2(input: List<String>): Long {
        val nodes = input.drop(2).map {
            val values = nodeRegex.findAll(it).toList()[0].groupValues
            values[1] to Pair(values[2], values[3])
        }.associate { it.first to it.second }
        return nodes.keys.filter { it.endsWith("A") }
            .map {
                countStepsPart2(it, RightLeftIterator(input[0]), nodes, 0)
            }.reduce(::lcm)
    }

    fun lcm(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    tailrec fun countSteps(
        currentNode: String,
        directionIterator: RightLeftIterator,
        nodes: Map<String, Pair<String, String>>,
        steps: Int
    ): Int {
        if (currentNode == "ZZZ") {
            return steps
        }
        val nextDirection = directionIterator.next()
        val nextNode = when (nextDirection) {
            Direction.L -> nodes[currentNode]!!.first
            Direction.R -> nodes[currentNode]!!.second
        }
        return countSteps(nextNode, directionIterator, nodes, steps + 1)
    }

    tailrec fun countStepsPart2(
        currentNode: String,
        directionIterator: RightLeftIterator,
        nodes: Map<String, Pair<String, String>>,
        steps: Int
    ): Long {
        if (currentNode.endsWith("Z")) {
            return steps.toLong()
        }
        val nextDirection = directionIterator.next()
        val nextNode = when (nextDirection) {
            Direction.L -> nodes[currentNode]!!.first
            Direction.R -> nodes[currentNode]!!.second
        }
        return countStepsPart2(nextNode, directionIterator, nodes, steps + 1)
    }

    class RightLeftIterator(sequence: String) : Iterator<Direction> {

        var currentIndex = 0
        val parsedSequence = sequence.toCharArray().map { Direction.valueOf(it.toString()) }
        override fun hasNext(): Boolean = true

        override fun next(): Direction {
            val direction = parsedSequence[currentIndex++]
            if (currentIndex == parsedSequence.size) {
                currentIndex = 0
            }
            return direction
        }

    }

    enum class Direction {
        R, L
    }
}