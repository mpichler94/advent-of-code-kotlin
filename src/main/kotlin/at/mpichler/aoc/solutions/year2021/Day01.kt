package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

class PartA : PartSolution() {

    lateinit var numbers: List<Int>

    override fun parseInput(text: String) {
        numbers = text.trim().split("\n").map { it.toInt() }.toList()
    }

    override fun getExampleAnswer(): String {
        return "7"
    }

    override fun compute(): String {
        return countIncreases().toString()
    }

    private fun countIncreases(): Int {
        return numbers.windowed(2).map { it[1] > it[0] }.count { it }
    }
}

fun main() {
    Day(2021, 1, PartA())
}