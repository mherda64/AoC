package year24.day07

import DayTemplate
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicLong

class Day07 : DayTemplate() {

    init {
        "Should solve the test input" {
            val (firstPart, secondPart) = solve(testInput)
            firstPart shouldBe 3749
            secondPart shouldBe 11387
        }

        "Should solve the proper input" {
            val (firstPart, secondPart) = solve(input)
            println(firstPart)
            println(secondPart)
        }
    }

    private fun solve(input: List<String>): Pair<Long, Long> {
        val parsed = parseInput(input)
        var firstSum = AtomicLong(0)
        var secondSum = AtomicLong(0)

        runBlocking(Dispatchers.Default) {
            for (numbers in parsed) {
                launch {
                    if (isPossible(numbers, false)) {
                        firstSum.addAndGet(numbers.first)
                    }
                }
                launch {
                    if (isPossible(numbers, true)) {
                        secondSum.addAndGet(numbers.first)
                    }
                }
            }
        }

        return Pair(firstSum.get(), secondSum.get())
    }

    fun isPossible(input: Pair<Long, List<Long>>, concatenate: Boolean): Boolean {
        val toCheck = ArrayDeque(listOf(input))

        while (toCheck.isNotEmpty()) {
            val (desiredValue, elements) = toCheck.last()
            when {
                elements.size == 1 -> {
                    if (elements[0] == desiredValue) {
                        return true
                    } else {
                        toCheck.removeLast()
                        continue
                    }
                }

                else -> {
                    toCheck.removeLast()
                    toCheck.addLast(Pair(desiredValue, listOf(elements[0] + elements[1]) + elements.drop(2)))
                    toCheck.addLast(Pair(desiredValue, listOf(elements[0] * elements[1]) + elements.drop(2)))
                    if (concatenate) {
                        toCheck.addLast(Pair(desiredValue, listOf("${elements[0]}${elements[1]}".toLong()) + elements.drop(2)))
                    }
                }
            }
        }

        return false
    }

    private fun parseInput(input: List<String>) =
        input.map { row ->
            val a = row.split(":")
            val components = a[1].trim().split(" ").map { it.toLong() }
            Pair(a[0].toLong(), components)
        }

}