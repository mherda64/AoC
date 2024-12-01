package year23.day10

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import year23.readInputAsLines

class Day10 : FreeSpec() {
    val testInput1 = readInputAsLines("${this::class.simpleName}_test1")
    val testInput2 = readInputAsLines("${this::class.simpleName}_test2")
    val testInput3 = readInputAsLines("${this::class.simpleName}_test3")
    val input = readInputAsLines("${this::class.simpleName}")

    init {
        "Should solve test input" {
            part1(testInput1) shouldBe 4
            part1(testInput2) shouldBe 8
            part2(testInput3) shouldBe 8
        }

        "Should solve proper input" {
            println(part1(input))
            println(part2(input))
        }
    }

    fun part1(input: List<String>): Int {
        val pipesMap = input.map {
            it.toCharArray().map(Pipe::valueOf).toTypedArray()
        }.toTypedArray()
        val startPos = getStartPos(input)
        val startDirection = getStartDirection(startPos, pipesMap)
        val steps = walkTheMap(startPos, startDirection, pipesMap, startPos, 0)

        return steps / 2
    }

    fun part2(input: List<String>): Int {
        val pipesMap = input.map {
            it.toCharArray().map(Pipe::valueOf).toTypedArray()
        }.toTypedArray()
        val startPos = getStartPos(input)
        val startDirection = getStartDirection(startPos, pipesMap)
        walkTheMap(startPos, startDirection, pipesMap, startPos, 0)

        return input.mapIndexed { y, line ->
            var inside = false
            var insideCount = 0
            line.forEachIndexed { x, char ->
                val maybePipe = pipesMap[y][x]
                if (maybePipe?.isLoop == true && (maybePipe.a == Direction.N || maybePipe.b == Direction.N))
                    inside = !inside
                if (inside && (pipesMap[y][x] == null || pipesMap[y][x]?.isLoop == false) )
                    insideCount++

            }
            insideCount
        }.sum()
    }

    tailrec fun walkTheMap(
        position: Pair<Int, Int>,
        direction: Direction,
        map: Array<Array<Pipe?>>,
        startPos: Pair<Int, Int>,
        steps: Int
    ): Int {
        map[position.second][position.first]?.distance = steps
        map[position.second][position.first]?.isLoop = true
        val nextPosition = when (direction) {
            Direction.N -> Pair(position.first, position.second - 1)
            Direction.S -> Pair(position.first, position.second + 1)
            Direction.W -> Pair(position.first - 1, position.second)
            Direction.E -> Pair(position.first + 1, position.second)
        }
        if (nextPosition == startPos)
            return steps + 1
        val nextPipe = map[nextPosition.second][nextPosition.first]
        requireNotNull(nextPipe)
        val nextDirection = if (nextPipe.a == direction.getOpposite()) nextPipe.b else nextPipe.a
        return walkTheMap(nextPosition, nextDirection, map, startPos, steps + 1)
    }

    fun getStartDirection(startPos: Pair<Int, Int>, map: Array<Array<Pipe?>>): Direction =
        when {
            startPos.second > 0 && map[startPos.second - 1][startPos.first] != null -> Direction.N
            startPos.first < map[startPos.second].size - 1 && map[startPos.second][startPos.first + 1] != null -> Direction.E
            startPos.second < map.size - 1 && map[startPos.second + 1][startPos.first] != null -> Direction.S
            startPos.first > 0 && map[startPos.second][startPos.first - 1] != null -> Direction.W
            else -> throw IllegalStateException()
        }

    fun getStartPos(input: List<String>): Pair<Int, Int> {
        val startY = input.indexOfFirst {
            it.contains("S")
        }
        val startX = input[startY].indexOfFirst { it == 'S' }
        return Pair(startX, startY)
    }

    enum class Direction {
        N, S, W, E;

        fun getOpposite(): Direction =
            when(this) {
                N -> S
                S -> N
                W -> E
                E -> W
            }
    }

    data class Pipe(val a: Direction, val b: Direction, var distance: Int = 0, var isLoop: Boolean = false) {
        companion object {
            fun valueOf(c: Char): Pipe? =
                when (c) {
                    '|' -> Pipe(Direction.N, Direction.S)
                    '-' -> Pipe(Direction.W, Direction.E)
                    'L' -> Pipe(Direction.N, Direction.E)
                    'J' -> Pipe(Direction.N, Direction.W)
                    '7' -> Pipe(Direction.S, Direction.W)
                    'F' -> Pipe(Direction.S, Direction.E)
                    else -> null
                }
        }
    }

}