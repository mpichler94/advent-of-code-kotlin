package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class PartA: PartSolution() {

    internal lateinit var sums: List<Int>

    override fun parseInput(text: String){

        sums = text.trim()
            .split("\n\n")
            .map { it.split("\n").map { it.toInt() }.sum() }
    }

    override fun getExampleAnswer(): String {
        return 24000.toString()
    }

    override fun compute(): String {
        return sums.max().toString()
    }
}

class PartB: PartA() {

}

fun main() {
    Day(2022, 1, PartA())
}