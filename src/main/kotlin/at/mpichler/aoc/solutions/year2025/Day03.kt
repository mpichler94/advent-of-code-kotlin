package at.mpichler.aoc.solutions.year2025

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part3A : PartSolution() {
    lateinit var banks: List<List<Int>>

    override fun parseInput(text: String) {
        banks = text.split("\n").map { it.map { it.digitToInt() } }
    }

    override fun getExampleAnswer() = 357L

    override fun compute(): Long = banks.sumOf { getJoltage(2, it) }

    protected fun getJoltage(numDigits: Int, bank: List<Int>): Long {
        val digits = mutableListOf(getDigit(0, bank.size - (numDigits - 1), bank))
        repeat(numDigits - 1) {
            val d = getDigit(digits.last().pos + 1, bank.size - (numDigits - 1 - digits.size), bank)
            digits.add(d)
        }

        return digits.joinToString(separator = "") { it.value.toString() }.toLong()
    }

    private fun getDigit(start: Int, end: Int, bank: List<Int>): Digit {
        var maxVal = 0
        var maxId = 0
        bank.subList(start, end).forEachIndexed { i, value ->
            if (value > maxVal) {
                maxVal = value
                maxId = i
            }
        }
        return Digit(maxId + start, maxVal)
    }

    private data class Digit(
        val pos: Int,
        val value: Int,
    )
}

class Part3B : Part3A() {
    override fun getExampleAnswer() = 3121910778619L

    override fun compute(): Long = banks.sumOf { getJoltage(12, it) }
}

fun main() {
    Day(2025, 3, Part3A(), Part3B())
}
