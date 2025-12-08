package year25.day08

import DayTemplate
import io.kotest.matchers.shouldBe
import kotlin.math.abs
import kotlin.math.sqrt

class Day08 : DayTemplate(year = "year25") {
    init {
        "Should solve the test input" {
            firstPart(testInputPart1, 10) shouldBe 40
            secondPart(testInputPart2) shouldBe 25272
        }

        "Should solve the proper input" {
            println(firstPart(input, 1000))
            println(secondPart(input))
        }
    }

    fun parseInput(input: List<String>): List<JunctionBox> {
        return input.map {
            val (x, y, z) = it.split(",").map(String::toLong)
            JunctionBox(x, y, z)
        }
    }

    fun calcDistance(a: JunctionBox, b: JunctionBox): Double {
        return sqrt(
            (abs(a.x - b.x) * abs(a.x - b.x) +
                    abs(a.y - b.y) * abs(a.y - b.y) +
                    abs(a.z - b.z) * abs(a.z - b.z)).toDouble()
        )
    }

    fun getDistanceMatrix(junctionBoxes: List<JunctionBox>): Array<DoubleArray> {
        val size = junctionBoxes.size
        val distanceMatrix = Array(size) { DoubleArray(size) }
        for (i in 0 until size) {
            for (j in 0 until size) {
                distanceMatrix[i][j] = calcDistance(junctionBoxes[i], junctionBoxes[j])
            }
        }
        return distanceMatrix
    }

    fun findPairWithSmallestDistance(distanceMatrix: Array<DoubleArray>): Pair<Int, Int> {
        var minDistance = Double.MAX_VALUE
        var pair = Pair(-1, -1)
        for (i in distanceMatrix.indices) {
            for (j in distanceMatrix[i].indices) {
                if (i != j && distanceMatrix[i][j] < minDistance) {
                    minDistance = distanceMatrix[i][j]
                    pair = Pair(i, j)
                }
            }
        }
        return pair
    }

    fun firstPart(input: List<String>, iterations: Int): Int {
        val junctionBoxes = parseInput(input)
        val distanceMatrix = getDistanceMatrix(junctionBoxes)
        var connections = listOf<Set<JunctionBox>>()
        for (i in 0..<iterations) {
            val pairWithSmallestDistance = findPairWithSmallestDistance(distanceMatrix)
            val first = junctionBoxes[pairWithSmallestDistance.first]
            val second = junctionBoxes[pairWithSmallestDistance.second]
            connections = addAndMerge(connections, first, second)
            distanceMatrix[pairWithSmallestDistance.first][pairWithSmallestDistance.second] = Double.MAX_VALUE
            distanceMatrix[pairWithSmallestDistance.second][pairWithSmallestDistance.first] = Double.MAX_VALUE
        }

        return connections.map { it.size }.sortedDescending().take(3).reduce { a, b -> a * b}
    }

    fun addAndMerge(
        connections: List<Set<JunctionBox>>,
        first: JunctionBox,
        second: JunctionBox,
    ): List<Set<JunctionBox>> {
        val existingConnection = connections.firstOrNull {
            it.contains(first) || it.contains(second)
        }

        return if (existingConnection != null) {
            if (existingConnection.contains(first) && existingConnection.contains(second)) {
                connections
            } else {
                val firstPossible = connections.firstOrNull { it.contains(first) }
                val secondPossible = connections.firstOrNull { it.contains(second) }
                if (firstPossible != null && secondPossible != null && firstPossible != secondPossible) {
                        connections.filter { it != firstPossible && it != secondPossible } +
                                listOf(firstPossible + secondPossible)

                } else {
                    connections.filter { it != existingConnection } + listOf(existingConnection + setOf(first, second))
                }
            }
        } else {
            connections + listOf(setOf(first, second))
        }
    }

    fun secondPart(input: List<String>): Long {
        val junctionBoxes = parseInput(input)
        val distanceMatrix = getDistanceMatrix(junctionBoxes)
        var connections = listOf<Set<JunctionBox>>()
        while (true) {
            val pairWithSmallestDistance = findPairWithSmallestDistance(distanceMatrix)
            val first = junctionBoxes[pairWithSmallestDistance.first]
            val second = junctionBoxes[pairWithSmallestDistance.second]
            connections = addAndMerge(connections, first, second)
            distanceMatrix[pairWithSmallestDistance.first][pairWithSmallestDistance.second] = Double.MAX_VALUE
            distanceMatrix[pairWithSmallestDistance.second][pairWithSmallestDistance.first] = Double.MAX_VALUE

            if (connections[0].containsAll(junctionBoxes)) {
                return first.x * second.x
            }
        }
    }

    data class JunctionBox(val x: Long, val y: Long, val z: Long)

}

