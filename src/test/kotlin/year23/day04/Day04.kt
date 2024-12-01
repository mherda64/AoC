package year23.day04

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import year23.readInputAsLines
import kotlin.math.pow

class Day04 : FreeSpec() {
    val dayNo = "04"
    val testInput = readInputAsLines("Day${dayNo}_test")
    val input = readInputAsLines("Day${dayNo}")
    val spaceRegex = " +".toRegex()

    init {
        "Should solve test input" {
            part1(testInput) shouldBe 13
            part2(testInput) shouldBe 30
        }

        "Should solve proper input" {
            println(part1(input))
            println(part2(input))
        }
    }

    fun part1(input: List<String>) =
        input.map { it.split(":")[1].trim().split("|") }
            .map {
                Pair(
                    it[0].trim().split(spaceRegex).map { it.toInt() }.toSet(),
                    it[1].trim().split(spaceRegex).map { it.toInt() }.toSet()
                )
            }.mapIndexed { index, value -> ScratchCard(index + 1, value.first, value.second).calculateScore() }
            .sum()

    fun part2(input: List<String>): Int {
        val initialScratchCards = input.map { it.split(":")[1].trim().split("|") }
            .map {
                Pair(
                    it[0].trim().split(spaceRegex).map { it.toInt() }.toSet(),
                    it[1].trim().split(spaceRegex).map { it.toInt() }.toSet()
                )
            }.mapIndexed {index, value -> ScratchCard(index + 1, value.first, value.second) }
        return countScratchcards(initialScratchCards, 0)
    }

    private tailrec fun countScratchcards(scratchCards: List<ScratchCard>, cards: Int): Int {
        if (scratchCards.isEmpty())
            return cards
        val current = scratchCards.first()
        val duplicated = (current.index + 1 .. current.index + current.wonCount())
            .mapNotNull { index -> scratchCards.find { index == it.index } }

        return countScratchcards(duplicated + scratchCards.drop(1), cards + 1)
    }

    data class ScratchCard(
        val index: Int,
        val winning: Set<Int>,
        val found: Set<Int>
    ) {
        fun wonCount(): Int =
            winning.intersect(found).size

        fun calculateScore(): Int {
            val wonCount = wonCount()
            if (wonCount == 0)
                return 0
            else
                return 2f.pow(wonCount - 1).toInt()
        }

    }
}