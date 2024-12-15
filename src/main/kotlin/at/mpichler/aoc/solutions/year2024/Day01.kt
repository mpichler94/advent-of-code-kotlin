package at.mpichler.aoc.solutions.year2024

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import kotlin.math.abs

open class Part1A : PartSolution() {
    internal lateinit var left: List<Int>
    internal lateinit var right: List<Int>

    override fun parseInput(text: String) {
        val (left, right) = text
            .trim()
            .lines()
            .map {
                val parts = it.split(' ')
                parts.first().toInt() to parts.last().toInt()
            }.unzip()

        this.left = left
        this.right = right
    }

    override fun getExampleAnswer() = 11

    override fun config() {
        left = left.sorted()
        right = right.sorted()
    }

    override fun compute(): Int = left.zip(right).sumOf { abs(it.first - it.second) }
}

class Part1B : Part1A() {
    override fun getExampleAnswer() = 31

    override fun compute(): Int {
        val frequency = right.groupingBy { it }.eachCount()
        return left.sumOf { frequency.getOrDefault(it, 0) * it }
    }
}

fun main() {
    Day(2024, 1, Part1A(), Part1B())
}
