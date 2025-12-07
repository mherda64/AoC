package year25.day07

import DayTemplate
import io.kotest.matchers.shouldBe

class Day07 : DayTemplate(year = "year25") {
    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 21
            secondPart(testInputPart2) shouldBe 40
        }

        "Should solve the proper input" {
            println(firstPart(input))
            println(secondPart(input))
        }
    }

    fun parseInput(input: List<String>): List<Layer> {
        return input.map { line ->
            val beam = line.indexOf('S')
            val splitters = line.withIndex().filter { it.value == '^' }.map { it.index }.toSet()
            Layer(if (beam > 0) setOf(beam) else emptySet(), splitters)
        }
    }

    fun firstPart(input: List<String>): Int {
        val beamLayers = parseInput(input)

        val processed = beamLayers.drop(1).fold(beamLayers[0]) { prevLayer, layer ->
            val newBeams = mutableSetOf<Int>()
            var splitCount = 0
            prevLayer.beams.forEach { prevLayerBeam ->
                if (layer.splitters.contains(prevLayerBeam)) {
                    newBeams.add(prevLayerBeam - 1)
                    newBeams.add(prevLayerBeam + 1)
                    splitCount += 1
                } else {
                    newBeams.add(prevLayerBeam)
                }
            }

            Layer(newBeams, emptySet(), prevLayer.splitCount + splitCount)
        }

        return processed.splitCount
    }

    fun parseInput2(input: List<String>): List<Layer2> {
        return input
            .filter { !it.toCharArray().all { it == '.' } }
            .map { line ->
                val beamIdx = line.indexOf('S')
                val splitters = line.withIndex().filter { it.value == '^' }.map { it.index }.toSet()
                Layer2(if (beamIdx > 0) listOf(Beam(beamIdx, 1)) else emptyList(), splitters)
            }
    }

    fun secondPart(input: List<String>): Long {
        val beamLayers = parseInput2(input)
        println(beamLayers)

        val processed = beamLayers.drop(1).foldIndexed(beamLayers[0]) { idx, prevLayer, layer ->
            val newBeams = mutableMapOf<Int, Long>()
            var splitCount = 0L
            prevLayer.beams.forEach { prevLayerBeam ->
                if (layer.splitters.contains(prevLayerBeam.position)) {
                    newBeams[prevLayerBeam.position - 1] =
                        newBeams.getOrDefault(prevLayerBeam.position - 1, 0) + maxOf(1, prevLayerBeam.count)
                    newBeams[prevLayerBeam.position + 1] =
                        newBeams.getOrDefault(prevLayerBeam.position + 1, 0) + maxOf(1, prevLayerBeam.count)
                    splitCount += prevLayerBeam.count
                } else {
                    newBeams[prevLayerBeam.position] =
                        newBeams.getOrDefault(prevLayerBeam.position, 0) + maxOf(1, prevLayerBeam.count)
                }

            }

            println("idx: $idx, newBeamsSize: ${newBeams.size}")
            val newBeamsGrouped = newBeams.map { Beam(it.key, it.value) }
            Layer2(newBeamsGrouped, emptySet(), prevLayer.splitCount + splitCount)
        }

        println(processed)
        return processed.beams.sumOf { it.count }
    }

    data class Layer(
        val beams: Set<Int>,
        val splitters: Set<Int>,
        val splitCount: Int = 0
    )

    data class Layer2(
        val beams: List<Beam>,
        val splitters: Set<Int>,
        val splitCount: Long = 0
    )

    data class Beam(
        val position: Int,
        val count: Long
    )
}

