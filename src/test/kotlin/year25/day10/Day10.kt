package year25.day10

import DayTemplate
import com.microsoft.z3.Context
import com.microsoft.z3.IntExpr
import com.microsoft.z3.IntNum
import com.microsoft.z3.Status
import io.kotest.matchers.shouldBe

class Day10 : DayTemplate(year = "year25") {
    init {
        "Should solve the test input" {
            firstPart(testInputPart1) shouldBe 7
            secondPart(testInputPart2) shouldBe 33
        }

        "Should solve the proper input" {
            println(firstPart(input))
            println(secondPart(input))
        }
    }

    fun parseInput(input: List<String>): List<Machine> {
        val lightRegex = "\\[[.#]+]".toRegex()
        val buttonsRegex = "(\\([\\d,]+\\))".toRegex()
        val joltageRegex = "\\{[\\d,]+}".toRegex()

        return input.map { line ->
            val lightMatch = lightRegex.find(line)!!
            val lights = lightMatch.value.drop(1).dropLast(1).map { it == '#' }

            val buttonsMatch = buttonsRegex.findAll(line).toList()
            val buttons = buttonsMatch.map { match ->
                match.value.drop(1).dropLast(1).split(',').map { it.toInt() }.toSet()
            }

            val joltageMatch = joltageRegex.find(line)!!
            val joltageRequirements = joltageMatch.value.drop(1).dropLast(1).split(',').map { it.toInt() }

            Machine(lights, buttons, joltageRequirements)
        }

    }

    fun findMinimumButtonPressesPart1(machine: Machine): Int {
        val desiredLightsState = machine.lights
        val availableButtons = machine.buttons

        // pathfinding
        // BFS to find the minimum button presses
        data class State(val lights: List<Boolean>, val presses: Int)
        val initialState = State(List(desiredLightsState.size) { false }, 0)
        val queue = ArrayDeque<State>()
        val visited = mutableSetOf<List<Boolean>>()
        queue.add(initialState)
        visited.add(initialState.lights)
        while (queue.isNotEmpty()) {
            val currentState = queue.removeFirst()
            if (currentState.lights == desiredLightsState) {
                return currentState.presses
            }
            for (button in availableButtons) {
                val newLights = currentState.lights.toMutableList()
                for (index in button) {
                    newLights[index] = !newLights[index]
                }
                if (newLights !in visited) {
                    visited.add(newLights)
                    queue.add(State(newLights, currentState.presses + 1))
                }
            }
        }
        return Int.MAX_VALUE // If no solution found
    }

    fun firstPart(input: List<String>): Int {
        val machines = parseInput(input)

        val minimumButtonPresses = machines.sumOf { machine ->
            findMinimumButtonPressesPart1(machine)
        }

        return minimumButtonPresses
    }

    fun findMinimumButtonPressesPart2(machine: Machine): Int = Context().use { ctx ->
        val solver = ctx.mkOptimize()
        val zero = ctx.mkInt(0)

        val buttons = machine.buttons.indices
            .map { idx -> ctx.mkIntConst("btn$idx") }
            .toTypedArray()
        for (b in buttons) solver.Add(ctx.mkGe(b, zero))

        machine.joltageRequirements.forEachIndexed { counter, targetValue ->
            val contributing = machine.buttons
                .withIndex()
                .filter { (_, counters) -> counter in counters }
                .map { buttons[it.index] }

            val sumExpr = when (contributing.size) {
                0 -> ctx.mkInt(0)
                1 -> contributing[0]
                else -> ctx.mkAdd(*contributing.toTypedArray())
            } as IntExpr

            val target = ctx.mkInt(targetValue)
            if (contributing.isEmpty() && targetValue != 0) error("No solution: counter $counter requires $targetValue but no button affects it")

            solver.Add(ctx.mkEq(sumExpr, target))
        }

        val presses = ctx.mkIntConst("presses")
        val totalSum = when (buttons.size) {
            0 -> ctx.mkInt(0)
            1 -> buttons[0]
            else -> ctx.mkAdd(*buttons)
        } as IntExpr
        solver.Add(ctx.mkEq(presses, totalSum))
        solver.MkMinimize(presses)

        val status = solver.Check()
        if (status != Status.SATISFIABLE) error("No solution found for machine: $machine. Status: $status")

        val model = solver.model
        model.evaluate(presses, true).let { it as IntNum }.int
    }

    fun secondPart(input: List<String>): Int {
        val machines = parseInput(input)

        val minimumButtonPresses = machines.sumOf { machine ->
            findMinimumButtonPressesPart2(machine)
        }

        return minimumButtonPresses
    }

    data class Machine(
        val lights: List<Boolean>,
        val buttons: List<Set<Int>>,
        val joltageRequirements: List<Int>
    )

}
