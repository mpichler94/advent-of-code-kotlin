package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part1A : PartSolution() {

    lateinit var numbers: List<Int>

    override fun parseInput(text: String) {
        numbers = text.trim().split("\n").map { it.toInt() }.toList()
    }

    override fun getExampleAnswer(): Int {
        return 7
    }

    override fun compute(): Int {
        return countIncreases()
    }

    private fun countIncreases(): Int {
        return numbers.windowed(2).map { it[1] > it[0] }.count { it }
    }
}

class Part1B : Part1A() {
    override fun config() {
        numbers = numbers.windowed(3).map { it.sum() }
    }

    override fun getExampleAnswer(): Int {
        return 5
    }
}

fun main() {
    Day(2021, 1, Part1A(), Part1B())
}