package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part14A : PartSolution() {
    private lateinit var template: String
    private lateinit var rules: Map<String, String>
    open val numSteps = 10

    override fun parseInput(text: String) {
        val parts = text.split("\n\n")
        template = parts[0]
        rules = parts[1].split("\n").map { it.split(" -> ") }.associate { it[0] to it[1] }
    }

    override fun compute(): Long {
        val counter = processPolymer()
        val values = counter.values.sorted()
        return values.last() - values.first()
    }

    override fun config() {
        cache = mutableMapOf()
    }

    private fun processPolymer(): Map<Char, Long> {
        val counter = template.toList().groupingBy { it }.eachCount().mapValues { it.value.toLong() }.toMutableMap()
        for (i in 0..<template.length - 1) {
            counter.update(replaceAndCount(template.substring(i..i + 1), 0))
        }
        return counter
    }

    private var cache: MutableMap<Pair<String, Int>, Map<Char, Long>> = mutableMapOf()
    private fun replaceAndCount(pair: String, iteration: Int): Map<Char, Long> {
        if (cache.containsKey(pair to iteration)) {
            return cache[pair to iteration]!!
        }

        val v = doReplaceAndCount(pair, iteration)
        cache[pair to iteration] = v
        return v
    }

    private fun doReplaceAndCount(pair: String, iteration: Int): Map<Char, Long> {
        if (iteration >= numSteps) {
            return mapOf()
        }

        val insert = rules[pair]!!
        val pair1 = pair[0] + insert
        val pair2 = insert + pair[1]
        val counter = insert.toList().associateWith { 1L }.toMutableMap()
        counter.update(replaceAndCount(pair1, iteration + 1))
        counter.update(replaceAndCount(pair2, iteration + 1))
        return counter
    }

    private fun <K> MutableMap<K, Long>.update(other: Map<K, Long>) {
        for (entry in other) {
            val v = getOrDefault(entry.key, 0L)
            set(entry.key, v + entry.value)
        }
    }

    override fun getExampleAnswer(): Long {
        return 1588
    }
}

class Part14B : Part14A() {
    override val numSteps = 40

    override fun getExampleAnswer(): Long {
        return 2_188_189_693_529
    }
}

fun main() {
    Day(2021, 14, Part14A(), Part14B())
}