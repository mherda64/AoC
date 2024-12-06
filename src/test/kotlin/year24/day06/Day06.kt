package year24.day06

import DayTemplate
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger

class Day06 : DayTemplate() {

    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 41
            secondPart(testInputPart2) shouldBe 6
        }

        "Should solve the proper input" {
            println(firstPart(input))
            println(secondPart(input))
        }
    }

    private fun firstPart(input: List<String>): Int {
        val matrix = Matrix(input)
        while (matrix.isGuardInsideMap()) {
            matrix.iterateGuard()
        }

        return matrix.getVisitedCells()
    }

    // turbo ugly bruteforce
    private fun secondPart(input: List<String>): Int {
        val sizeMatrix = Matrix(input)
        while (sizeMatrix.isGuardInsideMap()) {
            sizeMatrix.iterateGuard()
        }
        val initPath = sizeMatrix.positionLog.map { it.position  }.distinct()
        var loopCounter = AtomicInteger(0)
        runBlocking(Dispatchers.Default) {
            for ((x, y) in initPath) {
                launch {
                    val matrix = Matrix(input)
                    if (matrix.matrix[x][y] != "^" && matrix.matrix[x][y] != "#") {
                        matrix.matrix[x][y] = "#"
                        while (matrix.isGuardInsideMap()) {
                            if (matrix.isLoop()) {
                                loopCounter.incrementAndGet()
                                break
                            }
                            matrix.iterateGuard()
                        }
                    }
                }
            }
        }
        return loopCounter.get()
    }

    class Matrix(
        input: List<String>
    ) {
        val matrix = input.map { it.toCharArray().map { it.toString() }.toMutableList() }
        val visited = List(rows) { MutableList<Boolean>(columns) { false } }
        val guard = Guard(findGuard())
        val positionLog = mutableListOf<Log>()

        val rows: Int
            get() = matrix.size
        val columns: Int
            get() = matrix[0].size

        fun isGuardInsideMap(): Boolean =
            guard.position.first in 0..<rows && guard.position.second in 0..<columns

        fun isPositionInsideMap(position: Pair<Int, Int>) =
            position.first in 0..<rows && position.second in 0..<columns

        fun isLoop() =
            positionLog.contains(Log(guard.position, guard.direction))

        fun iterateGuard() {
            visited[guard.position.first][guard.position.second] = true
            positionLog.add(Log(guard.position, guard.direction))
            val nextPosition = getNextPosition()
            if (!isPositionInsideMap(nextPosition)) {
                guard.position = nextPosition
                return
            }
            if (matrix[nextPosition.first][nextPosition.second] == "#") {
                guard.direction = when (guard.direction) {
                    Direction.N -> Direction.E
                    Direction.S -> Direction.W
                    Direction.W -> Direction.N
                    Direction.E -> Direction.S
                }
            } else {
                guard.position = nextPosition
            }
        }

        fun getNextPosition() =
            when (guard.direction) {
                Direction.N -> Pair(guard.position.first - 1, guard.position.second)
                Direction.S -> Pair(guard.position.first + 1, guard.position.second)
                Direction.W -> Pair(guard.position.first, guard.position.second - 1)
                Direction.E -> Pair(guard.position.first, guard.position.second + 1)
            }

        fun getVisitedCells() =
            visited.flatten().count { it }

        private fun findGuard(): Pair<Int, Int> {
            matrix.mapIndexed { rowIndex, row ->
                row.mapIndexed { columnIndex, cell ->
                    if (cell == "^") {
                        return Pair(rowIndex, columnIndex)
                    }
                }
            }

            throw IllegalStateException()
        }
    }

    class Guard(
        var position: Pair<Int, Int>,
        var direction: Direction = Direction.N
    )

    data class Log(
        val position: Pair<Int, Int>,
        val direction: Direction
    )

    enum class Direction {
        N, S, W, E
    }
}