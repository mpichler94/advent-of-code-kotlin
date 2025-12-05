package at.mpichler.aoc.solutions.year2025

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part5A : PartSolution() {
    lateinit var ranges: List<LongRange>
    lateinit var ids: List<Long>

    override fun parseInput(text: String) {
        val (rawRanges, ids) = text.split("\n\n")

        val ranges = rawRanges
            .lines()
            .map {
                val (start, end) = it.split('-').map { it.toLong() }
                LongRange(start, end)
            }.toMutableList()

        var i = 0
        outer@while (i < ranges.size) {
            var j = i + 1
            while (j < ranges.size) {
                if (ranges[i].first <= ranges[j].first && ranges[i].last >= ranges[j].first) {
                    val mergedRange = LongRange(ranges[i].first, maxOf(ranges[i].last, ranges[j].last))
                    ranges.removeAt(j)
                    ranges.removeAt(i)
                    ranges.add(i, mergedRange)
                    continue@outer
                }
                if (ranges[j].first <= ranges[i].first && ranges[j].last >= ranges[i].first) {
                    val mergedRange = LongRange(ranges[j].first, maxOf(ranges[i].last, ranges[j].last))
                    ranges.removeAt(j)
                    ranges.removeAt(i)
                    ranges.add(i, mergedRange)
                    continue@outer
                }
                j++
            }
            i++
        }

        this.ranges = ranges
        this.ids = ids.lines().map { it.toLong() }
    }

    override fun getExampleAnswer() = 3

    override fun compute(): Long = ids.count { id -> ranges.any { id in it } }.toLong()
}

class Part5B : Part5A() {
    override fun getExampleAnswer() = 14

    override fun compute(): Long = ranges.sumOf { it.last - it.first + 1 }
}

fun main() {
    Day(2025, 5, Part5A(), Part5B())
}
