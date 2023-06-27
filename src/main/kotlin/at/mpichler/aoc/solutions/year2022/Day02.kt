package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part2A : PartSolution() {
    private lateinit var rounds: List<String>
    internal open val table = mapOf(
        "AX" to 4,
        "AY" to 8,
        "AZ" to 3,
        "BX" to 1,
        "BY" to 5,
        "BZ" to 9,
        "CX" to 7,
        "CY" to 2,
        "CZ" to 6
        )

    override fun parseInput(text: String) {
        rounds = text.trim().split("\n").map { it.replace(" ", "") }.toList()
    }

    override fun compute(): Int {
        return rounds.sumOf { table[it] ?: 0 }
    }

    override fun getExampleAnswer(): Int {
        return 15
    }
}

class Part2B : Part2A() {
    override val table = mapOf(
        "AX" to 3,
        "AY" to 4,
        "AZ" to 8,
        "BX" to 1,
        "BY" to 5,
        "BZ" to 9,
        "CX" to 2,
        "CY" to 6,
        "CZ" to 7
    )

    override fun getExampleAnswer(): Int {
        return 12
    }
}

fun main() {
    Day(2022, 2, Part2A(), Part2B())
}

