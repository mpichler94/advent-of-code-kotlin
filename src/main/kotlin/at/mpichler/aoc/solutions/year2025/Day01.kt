package at.mpichler.aoc.solutions.year2025

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part1A : PartSolution() {
    internal lateinit var commands: List<String>

    override fun parseInput(text: String) {
        this.commands = text.split("\n")
    }

    override fun getExampleAnswer() = 3

    override fun compute(): Int = solve(commands)

    open fun solve(inputs: List<String>): Int {
        var dial = 50
        var code = 0

        inputs.forEach {
            dial = applyCommand(dial, it)
            if (dial == 0) code++
        }

        println(code)
        return code
    }

    private fun applyCommand(dial: Int, command: String): Int {
        val dir = command.first()
        val count = command.drop(1).toInt().mod(100)

        var dial = dial + if (dir == 'R') count else -count

        if (dial < 0) dial += 100
        if (dial > 99) dial -= 100

        return dial
    }
}

class Part1B : Part1A() {
    override fun getExampleAnswer() = 6

    override fun solve(inputs: List<String>): Int {
        var dial = 50
        var code = 0

        inputs.forEach {
            dial = applyCommand(dial, it) { count ->
                code += count
            }
        }

        println(code)
        return code
    }

    private fun applyCommand(dial: Int, command: String, onZero: (Int) -> Unit): Int {
        val dir = command.first()
        val fullCount = command.drop(1).toInt()
        val count = fullCount.rem(100)
        val rotations = fullCount.div(100)

        val oldDial = dial
        var dial = dial + if (dir == 'R') count else -count
        var zeroCount = rotations

        if (dial < 0) {
            dial += 100
            if (oldDial != 0) zeroCount++
        }
        if (dial > 99) {
            dial -= 100
            zeroCount++
            if (dial == 0) {
                onZero(zeroCount)
                return 0
            }
        }

        if (dial == 0) zeroCount++

        onZero(zeroCount)
        return dial
    }
}

fun main() {
    Day(2025, 1, Part1A(), Part1B())
}
