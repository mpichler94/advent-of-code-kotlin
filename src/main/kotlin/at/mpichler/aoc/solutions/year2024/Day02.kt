package at.mpichler.aoc.solutions.year2024

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import kotlin.math.abs

open class Part2A : PartSolution() {
    internal lateinit var levels: List<List<Int>>

    override fun parseInput(text: String) {
        levels = text.trim().lines().map { it.split(' ').map(String::toInt) }
    }

    override fun getExampleAnswer() = 2

    override fun compute(): Int = levels.count { checkLevels(it) }

    internal fun checkLevels(levels: List<Int>): Boolean {
        var increasing = false

        for (i in 1..<levels.size) {
            val diff = levels[i] - levels[i - 1]
            if (i == 1) {
                increasing = diff > 0
            }

            if (diff < 0 == increasing || abs(diff) < 1 || abs(diff) > 3) {
                return false
            }
        }

        return true
    }
}

class Part2B : Part2A() {
    override fun getExampleAnswer() = 4

    override fun compute(): Int {
        var count = 0
        for (l in levels) {
            if (checkLevels(l)) {
                count++
                continue
            }

            for (i in l.indices) {
                val list = l.toMutableList()
                list.removeAt(i)
                if (checkLevels(list)) {
                    count++
                    break
                }
            }
        }
        return count
    }
}

fun main() {
    Day(2024, 2, Part2A(), Part2B())
}
