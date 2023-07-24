package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part2A : PartSolution() {
    lateinit var commands: List<Command>

    override fun parseInput(text: String) {
        val lines = text.trimEnd().split("\n")
        commands = lines.map { Command(it) }
    }

    override fun compute(): Int {
        var depth = 0
        var pos = 0
        for (command in commands) {
            when (command.direction) {
                Direction.FORWARD -> pos += command.count
                Direction.DOWN -> depth += command.count
                Direction.UP -> depth -= command.count
            }
        }

        return pos * depth
    }

    override fun getExampleAnswer(): Int {
        return 150
    }

    enum class Direction { FORWARD, DOWN, UP }
    data class Command(val direction: Direction, val count: Int)

    private fun Command(line: String): Command {
        val parts = line.split(" ")
        val dir = Direction.valueOf(parts[0].uppercase())
        return Command(dir, parts[1].toInt())
    }
}

class Part2B : Part2A() {
    override fun compute(): Int {
        var aim = 0
        var depth = 0
        var pos = 0

        for (command in commands) {
            when (command.direction) {
                Direction.FORWARD -> {
                    pos += command.count
                    depth += aim * command.count
                }

                Direction.DOWN -> aim += command.count
                Direction.UP -> aim -= command.count
            }
        }

        return pos * depth
    }

    override fun getExampleAnswer(): Int {
        return 900
    }
}

fun main() {
    Day(2021, 2, Part2A(), Part2B())
}