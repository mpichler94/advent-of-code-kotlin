package at.mpichler.aoc.solutions.year2025

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part2A : PartSolution() {
    lateinit var ranges: List<LongRange>

    override fun parseInput(text: String) {
        ranges = text.split(",").map {
            val (start, end) = it.split("-").map { it.trim().toLong() }
            LongRange(start, end)
        }
    }

    override fun getExampleAnswer() = 1227775554L

    override fun compute(): Long = ranges.flatMap { getInvalid(it) }.sumOf { it }

    private fun getInvalid(range: LongRange): List<Long> = range.filter { isInvalid(it) }

    open fun isInvalid(id: Long): Boolean {
        val str = id.toString()
        if (str.length % 2 != 0) return false
        val half = str.length / 2
        val first = id.toString().take(half)
        val second = id.toString().drop(half)
        return first == second
    }
}

class Part2B : Part2A() {
    override fun getExampleAnswer(): Long = 4174379265L

    override fun isInvalid(id: Long): Boolean {
        val string = id.toString()
        return ((string + string).indexOf(string, 1) != string.length)
    }
}

fun main() {
    Day(2025, 2, Part2A(), Part2B())
}
