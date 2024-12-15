package at.mpichler.aoc.solutions.year2024

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part5A : PartSolution() {
    internal lateinit var instructions: List<Pair<Int, Int>>
    internal lateinit var updates: List<List<Int>>

    override fun parseInput(text: String) {
        val parts = text.trim().split("\n\n")
        instructions = parts.first().lines().map {
            val parts = it.split('|')
            parts.first().toInt() to parts.last().toInt()
        }
        updates = parts.last().lines().map { it.split(',').map(String::toInt) }
    }

    override fun getExampleAnswer() = 143

    override fun compute(): Int =
        updates
            .filter { update ->
                update.indices.all { checkRule(it, update) }
            }.sumOf { it[it.size / 2] }

    internal fun checkRule(index: Int, update: List<Int>): Boolean {
        val number = update[index]
        for (rule in instructions) {
            if (rule.first == number && update.subList(0, index).contains(rule.second)) {
                return false
            }
            if (rule.second == number && update.subList(index + 1, update.size).contains(rule.first)) {
                return false
            }
        }

        return true
    }
}

class Part5B : Part5A() {
    override fun getExampleAnswer() = 123

    override fun compute(): Int =
        updates
            .filter { update -> !update.indices.all { checkRule(it, update) } }
            .map { fixUpdate(it) }
            .sumOf { it[it.size / 2] }

    private fun fixUpdate(update: List<Int>): List<Int> {
        val fixed = mutableListOf<Int>()
        for (number in update) {
            var insertPos = 0
            outer@ for (i in fixed.indices) {
                for (rule in instructions) {
                    if (rule.first == number && rule.second == fixed[i]) {
                        insertPos = i
                        break@outer
                    }

                    if (rule.second == number && rule.first == fixed[i]) {
                        insertPos = i + 1
                    }
                }
            }
            fixed.add(insertPos, number)
        }
        return fixed
    }
}

fun main() {
    Day(2024, 5, Part5A(), Part5B())
}
