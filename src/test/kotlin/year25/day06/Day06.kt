package year25.day06

import DayTemplate
import io.kotest.matchers.shouldBe
import transpose

class Day06 : DayTemplate(year = "year25") {
    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 4277556
            secondPart(testInputPart2) shouldBe 3263827
        }

        "Should solve the proper input" {
            println(firstPart(input))
            println(secondPart(input))
        }
    }

    fun parseInput(input: List<String>): List<Operation> {
        val numberRegex = "( +)?(\\d+)".toRegex()
        val operatorRegex = "( +)?([+\\-*/])".toRegex()
        val numbers = input.take(input.size - 1).map { numberRegex.findAll(it).map { line -> line.groups[2]!!.value.toLong() }.toList() }
        val operators = operatorRegex.findAll(input.last()).map { it.groups[2]!!.value }.toList()

        return numbers[0].mapIndexed { index, _ ->
            Operation(
                numbers = numbers.map { it[index] },
                operator = operators[index]
            )
        }
    }


    fun firstPart(input: List<String>): Long {
        val operations = parseInput(input)
        val values = executeOperations(operations)

        return values.sum()
    }

    fun parseInput2(input: List<String>): List<Operation> {
        val matrix = input.map {
            it.toCharArray().map { char -> char.toString() }
        }
        val transposed = matrix.transpose()
        val indexesOfAllEmpty = transposed.mapIndexed { index, list ->
            if (list.all { it == " " }) {
                index
            } else {
                null
            }
        }.filterNotNull().toSet()
        val separateRawOperations = splitByIndexes(transposed, indexesOfAllEmpty)
        val operations = separateRawOperations.map { chunk ->
            val rawValues = chunk.map {
                it.filter { value -> value != " " }
            }
            val operator = rawValues[0].last()
            val numbers = rawValues.mapIndexed { idx, value ->
                if (idx == 0) {
                    value.dropLast(1).joinToString("")
                } else {
                    value.joinToString("")
                }
            }.filter { !it.isEmpty() }
            .map { it.toLong() }
            Operation(
                numbers = numbers,
                operator = operator
            )
        }

        return operations
    }

    fun splitByIndexes(
        input: List<List<String>>,
        indexes: Set<Int>
    ): List<List<List<String>>> {
        val result = mutableListOf<List<List<String>>>()
        var currentChunk = mutableListOf<List<String>>()

        input.forEachIndexed { i, row ->
            if (i in indexes) {
                if (currentChunk.isNotEmpty()) {
                    result.add(currentChunk)
                    currentChunk = mutableListOf()
                }
            } else {
                currentChunk.add(row)
            }
        }

        if (currentChunk.isNotEmpty()) {
            result.add(currentChunk)
        }

        return result
    }

    fun secondPart(input: List<String>): Long {
        val parsed = parseInput2(input)
        val values = executeOperations(parsed)

        return values.sum()
    }

    private fun executeOperations(parsed: List<Operation>): List<Long> {
        val values = parsed.map {
            when (it.operator) {
                "+" -> it.numbers.sum()
                "-" -> it.numbers.reduce { acc, n -> acc - n }
                "*" -> it.numbers.reduce { acc, n -> acc * n }
                "/" -> it.numbers.reduce { acc, n -> acc / n }
                else -> error("Unknown operator ${it.operator}")
            }
        }
        return values
    }
}

data class Operation(
    val numbers: List<Long>,
    val operator: String
)
