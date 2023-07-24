package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution


open class Part5A : PartSolution() {
    protected lateinit var commands: List<Command>
    internal lateinit var towers: List<MutableList<Char>>

    override fun parseInput(text: String) {
        val parts = text.trimEnd().split("\n\n")

        parseTowers(parts[0])
        commands = getCommands(parts[1])
    }

    private fun parseTowers(input: String) {
        towers = List(10) { mutableListOf() }
        for (line in input.split("\n")) {
            if (line[1] == '1') {
                continue
            }

            var i = 0
            while (i * 4 + 1 < line.length) {
                val crate = line[i * 4 + 1]
                if (crate != ' ') {
                    towers[i].add(crate)
                }
                i += 1
            }
        }
    }

    private fun getCommands(input: String): List<Command> {
        return input.trim().split("\n").map(::Command).toList()
    }

    override fun compute(): String {
        commands.forEach { it.execute(towers) }
        return getMessage()
    }

    internal fun getMessage(): String {
        val message = towers.filter { it.isNotEmpty() }.map { it[0] }.joinToString("")
        return message.trim()
    }

    override fun getExampleAnswer(): String {
        return "CMZ"
    }


    protected data class Command(val count: Int, val src: Int, val dst: Int) {
        fun execute(towers: List<MutableList<Char>>, moveMultipleAtOnce: Boolean = false) {
            for (i in 0..<count) {
                towers[dst].add(if (moveMultipleAtOnce) i else 0, towers[src].removeFirst())
            }
        }
    }

    private fun Command(line: String): Command {
        val match = Regex("move (\\d+) from (\\d+) to (\\d+)").find(line) ?: throw RuntimeException()

        val cnt = match.groupValues[1].toInt()
        val src = match.groupValues[2].toInt()
        val dst = match.groupValues[3].toInt()

        return Command(cnt, src - 1, dst - 1)
    }
}

class Part5B : Part5A() {
    override fun compute(): String {
        commands.forEach { it.execute(towers, true) }
        return getMessage()
    }

    override fun getExampleAnswer(): String {
        return "MCD"
    }
}

fun main() {
    Day(2022, 5, Part5A(), Part5B())
}