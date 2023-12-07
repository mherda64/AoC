package day07

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import readInputAsLines

class Day07 : FreeSpec() {
    val testInput = readInputAsLines("${this::class.simpleName}_test")
    val input = readInputAsLines("${this::class.simpleName}")

    init {
        "Should solve test input" {
            part1(testInput) shouldBe 6440
            part2(testInput) shouldBe 5905
        }

        "Should solve proper input" {
//            part1(input) shouldBe 253205868
            part2(input) shouldBe 253907829
        }
    }

    fun part1(input: List<String>): Int {
        val cardSets = input.map {
            val splitInput = it.split(" ")
            val bid = splitInput[1].toInt()
            val cardTypes = splitInput[0].toCharArray().map(CardType::valueOf)
            val cards = splitInput[0].toCharArray()
                .mapIndexed { index, c -> Card(CardType.valueOf(c), index) }
            Triple(bid, cards, cardTypes.associateWith { card -> cardTypes.count { it == card } })
        }.map { CardSet(it.first, it.second, it.third, CardSet.getSetType(it.third.values)) }

        val sorted = cardSets.asSequence().sorted()
        return sorted
            .mapIndexed { index, cardSet -> Pair(index, cardSet) }
            .associate { it.second to it.first + 1 }
            .map { it.key.bid * it.value }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val cardSets = input.map {
            val splitInput = it.split(" ")
            val bid = splitInput[1].toInt()
            val withoutJacks = replaceJacks(splitInput[0]).toCharArray().map(CardType::valueOf)
            val cards = splitInput[0].toCharArray()
                .mapIndexed { index, c -> Card(CardType.valueOf(c), index) }
            Triple(bid, cards, withoutJacks.associateWith { card -> withoutJacks.count { it == card } })
        }.map { CardSet(it.first, it.second, it.third, CardSet.getSetType(it.third.values)) }

        val sorted = cardSets.sorted()
        return sorted
            .mapIndexed { index, cardSet -> Pair(index, cardSet) }
            .associate { it.second to it.first + 1 }
            .map { it.key.bid * it.value }
            .sum()
    }

    fun replaceJacks(cards: String): String {
        if (cards == "JJJJJ")
            return "AAAAA"
        val mostCommon = cards.filter { it != 'J' }.groupBy { it }.maxBy { it.value.size }.key
        return cards.replace('J', mostCommon)
    }

    data class CardSet(val bid: Int, val cards: List<Card>, val cardMap: Map<CardType, Int>, val type: SetType): Comparable<CardSet> {

        companion object {
            fun getSetType(counts: Collection<Int>): SetType {
                require(counts.size <= 5)
                return when {
                    counts.any { it == 5 } -> SetType.FIVE_KIND
                    counts.any { it == 4 } -> SetType.FOUR_KIND
                    counts.any { it == 3 } && counts.size == 2 -> SetType.FULL_HOUSE
                    counts.any { it == 3 } -> SetType.THREE_KIND
                    counts.count { it == 2 } == 2 -> SetType.TWO_PAIR
                    counts.count { it == 2 } == 1 -> SetType.ONE_PAIR
                    else -> SetType.HIGH_CARD
                }
            }
        }

        override fun compareTo(other: CardSet): Int {
            return if (type.rank != other.type.rank)
                type.rank compareTo other.type.rank
            else {
                val comparable = cards.map { it.type.value }.zip(other.cards.map { it.type.value })
                    .firstOrNull { it.first != it.second } ?: Pair(0,0)
                comparable.first compareTo comparable.second
            }
        }
    }

    data class Card(val type: CardType, val index: Int)

    enum class SetType(val rank: Int) {
        FIVE_KIND(7),
        FOUR_KIND(6),
        FULL_HOUSE(5),
        THREE_KIND(4),
        TWO_PAIR(3),
        ONE_PAIR(2),
        HIGH_CARD(1);
    }

    enum class CardType(val value: Int) {
                JACK(1), // Comment for Part 1, uncomment for Part 2
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
//        JACK(11), // Uncomment for Part 1, comment for Part 2
        QUEEN(12),
        KING(13),
        ACE(14);

        companion object {
            fun valueOf(char: Char): CardType =
                when (char) {
                    'A' -> ACE
                    'K' -> KING
                    'Q' -> QUEEN
                    'J' -> JACK
                    'T' -> TEN
                    '9' -> NINE
                    '8' -> EIGHT
                    '7' -> SEVEN
                    '6' -> SIX
                    '5' -> FIVE
                    '4' -> FOUR
                    '3' -> THREE
                    '2' -> TWO
                    else -> throw IllegalStateException()
                }
        }
    }
}