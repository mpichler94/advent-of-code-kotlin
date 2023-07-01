package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part1A : PartSolution() {
    internal lateinit var sums: List<Int>

    override fun parseInput(text: String) {
        sums = text.trim()
            .split("\n\n")
            .map { it.split("\n").sumOf { it.toInt() } }
    }

    override fun getExampleAnswer(): Int {
        return 24000
    }

    override fun compute(): Int {
        return sums.max()
    }
}

class Part1B : Part1A() {
    override fun getExampleAnswer(): Int {
        return 45000
    }

    override fun config() {
        sums = sums.sorted()
    }

    override fun compute(): Int {
        return sums.takeLast(3).sum()
    }
}

fun main() {
    Day(2022, 1, Part1A(), Part1B())
}