package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part10A : PartSolution() {
    lateinit var lines: List<String>
    override fun parseInput(text: String) {
        lines = text.split("\n")
    }

    override fun compute(): Any {
        return lines.sumOf { getLineScore(it) }
    }

    fun getLineScore(line: String): Int {
        val score = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25_137)
        val stack = ArrayDeque<Char>()
        for (char in line) {
            if (!matchClosing(char, stack)) {
                return score[char]!!
            }
        }

        return 0
    }

    fun matchClosing(char: Char, stack: MutableList<Char>): Boolean {
        val matches = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
        if (char in listOf('(', '[', '{', '<')) {
            stack.add(char)
        } else {
            val openingChar = stack.removeLast()
            val needed = matches[openingChar]
            if (char != needed) {
                return false
            }
        }

        return true
    }

    override fun getExampleAnswer(): Int {
        return 26_397
    }

    override fun getExampleInput(): String? {
        return """
            [({(<(())[]>[[{[]{<()<>>
            [(()[<>])]({[<{<<[]>>(
            {([(<{}[<>[]}>{[]{[(<()>
            (((({<>}<{<{<>}{[]{[]{}
            [[<[([]))<([[{}[[()]]]
            [{[{({}]{}}([{[{{{}}([]
            {<[[]]>}<{[{[{[]{()[[[]
            [<(<(<(<{}))><([]([]()
            <{([([[(<>()){}]>(<<{{
            <{([{{}}[<[[[<>{}]]]>[]]
        """.trimIndent()
    }
}

class Part10B : Part10A() {
    override fun config() {
        lines = lines.filter { isNotCorrupt(it) }
    }

    private fun isNotCorrupt(line: String): Boolean {
        return getLineScore(line) == 0
    }

    override fun compute(): Long {
        val scores = lines.map { getCompletionScore(it) }.sorted()
        return scores[scores.size / 2]
    }

    private fun getCompletionScore(line: String): Long {
        val score = mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4)
        val stack = ArrayDeque<Char>()

        line.map { matchClosing(it, stack) }
        var points = 0L
        stack.reverse()
        for (char in stack) {
            points = points * 5 + score[char]!!
        }

        return points
    }

    override fun getExampleAnswer(): Int {
        return 288_957
    }
}

fun main() {
    Day(2021, 10, Part10A(), Part10B())
}