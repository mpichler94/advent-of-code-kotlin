package at.mpichler.aoc.solutions.year2024

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part3A : PartSolution() {
    internal lateinit var instructions: List<Instruction>

    override fun parseInput(text: String) {
        instructions =
            """mul\((\d+),(\d+)\)"""
                .toRegex()
                .findAll(text)
                .map { Instruction(it.range.first, it.groupValues[1].toInt(), it.groupValues[2].toInt()) }
                .toList()
    }

    override fun getExampleAnswer() = 161

    override fun compute(): Int = instructions.sumOf { it.arg1 * it.arg2 }

    internal data class Instruction(
        val offset: Int,
        val arg1: Int,
        val arg2: Int,
    )
}

class Part3B : Part3A() {
    private lateinit var ranges: List<IntRange>

    override fun parseInput(text: String) {
        super.parseInput(text)

        val dos = """do\(\)""".toRegex().findAll(text).map { it.range.first }.toList()
        val donts = """don't\(\)""".toRegex().findAll(text).map { it.range.first }.toList()

        val ranges = mutableListOf<IntRange>()

        var lastDont = 0
        for (dont in donts) {
            var start = lastDont
            for (d in dos) {
                if (d > lastDont && d < dont) {
                    start = d
                    break
                }
            }
            if (start > lastDont) {
                ranges.add(start..dont)
                lastDont = dont
            }
        }

        ranges.add(0..donts.first())
        if (dos.last() > donts.last()) {
            ranges.add(dos.last()..text.length)
        }

        this.ranges = ranges
    }

    override fun getExampleInput(): String = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"

    override fun getExampleAnswer() = 48

    override fun compute(): Int =
        instructions
            .filter { instruction -> ranges.any { it.contains(instruction.offset) } }
            .sumOf { it.arg1 * it.arg2 }
}

fun main() {
    Day(2024, 3, Part3A(), Part3B())
}
