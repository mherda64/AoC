package year23.day05

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import year23.readInput

class Day05 : FreeSpec() {
    val dayNo = "05"
    val testInput = readInput("Day${dayNo}_test").readText()
    val input = readInput("Day${dayNo}").readText()
    val inputPartsRegex = "\n\n".toRegex()

    init {
        "Should solve test input" {
            part1(testInput) shouldBe 35
            part2(testInput) shouldBe 46
        }

        "Should solve proper input" {
//            println(part1(input))
            println(part2(input))
        }
    }

    fun part1(input: String): Long {
        val inputParts = input.split(inputPartsRegex)
        val seeds = parseSeedsPart1(inputParts[0])
        val maps = parseMaps(inputParts)
        return seeds.map {
            maps.fold(it) { index, ranges ->
                ranges.firstNotNullOfOrNull { range -> range.get(index) } ?: index
            }
        }.min()
    }

    fun part2(input: String): Long {
        val inputParts = input.split(inputPartsRegex)
        val seedIterators = parseSeedsPart2(inputParts[0])
        val maps = parseMaps(inputParts)
        var min = getDestination(seedIterators[0].first(), maps)
        seedIterators.forEach { iterator ->
            iterator.forEach {
                min = minOf(min, getDestination(it, maps))
            }
        }
        return min
    }

    fun getDestination(init: Long, maps: List<List<Range>>) =
        maps.fold(init) { index, ranges ->
            ranges.firstNotNullOfOrNull { range -> range.get(index) } ?: index
        }

    fun parseSeedsPart1(seedsPart: String): List<Long> =
        seedsPart.split(":")[1].trim().split(" ").map { it.toLong() }

    fun parseSeedsPart2(seedsPart: String): List<LongRange> =
        seedsPart.split(":")[1].trim().split(" ").map { it.toLong() }
            .windowed(2, 2).map { it[0]..<it[0]+it[1] }

    fun parseMaps(inputParts: List<String>): List<List<Range>> {
        val parsedRanges = inputParts.drop(1)
            .map {
                it.split("\n")
                    .drop(1)
                    .map {
                        it.split(" ")
                            .map { it.toLong() }
                    }
            }
        val seedsToSoil = prepareRange(parsedRanges[0])
        val soilToFertilizer = prepareRange(parsedRanges[1])
        val fertilizerToWater = prepareRange(parsedRanges[2])
        val waterToLight = prepareRange(parsedRanges[3])
        val lightToTemperature = prepareRange(parsedRanges[4])
        val temperatureToHumidity = prepareRange(parsedRanges[5])
        val humidityToLocation = prepareRange(parsedRanges[6])

        return listOf(
            seedsToSoil,
            soilToFertilizer,
            fertilizerToWater,
            waterToLight,
            lightToTemperature,
            temperatureToHumidity,
            humidityToLocation
        )

    }

    fun prepareRange(ranges: List<List<Long>>): List<Range> = ranges.map { Range(it[0], it[1], it[2]) }

    data class Range(
        val destinationRangeStart: Long,
        val sourceRangeStart: Long,
        val length: Long
    ) {
        fun get(index: Long): Long? =
            if (index >= sourceRangeStart && index < sourceRangeStart + length) {
                index - (sourceRangeStart - destinationRangeStart)
            } else
                null

    }
}