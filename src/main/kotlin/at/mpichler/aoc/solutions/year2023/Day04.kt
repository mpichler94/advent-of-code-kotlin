package at.mpichler.aoc.solutions.year2023

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part4A : PartSolution() {
    internal lateinit var cards: List<Card>

    override fun parseInput(text: String) {
        val cards = mutableListOf<Card>()
        val pattern = "Card +\\d+: +((?:\\d+ *)+) \\| +((?:\\d+ *)+)".toRegex()
        for (line in text.split("\n")) {
            val match = pattern.find(line) as MatchResult
            val winningNumbers = match.groupValues[1].split(" +".toRegex()).map { it.toInt() }
            val numbers = match.groupValues[2].split(" +".toRegex()).map { it.toInt() }
            cards.add(Card(winningNumbers, numbers))
        }

        this.cards = cards
    }

    override fun getExampleAnswer() = 13

    override fun compute(): Int {
        return cards.sumOf { it.points }
    }

    internal data class Card(val winningNumbers: List<Int>, val numbers: List<Int>) {
        val matches get() = winningNumbers.intersect(numbers.toSet()).size
        val points: Int
            get() {
                val matches = this.matches
                if (matches == 0) {
                    return 0
                }
                return 1 shl (matches - 1)
            }
    }
}

class Part4B : Part4A() {
    override fun getExampleAnswer() = 30

    override fun compute(): Int {
        val copies = MutableList(cards.size) { 1 }

        for (i in cards.indices) {
            for (j in i + 1..i + cards[i].matches) {
                copies[j] += copies[i]
            }
        }

        return copies.sum()
    }
}

fun main() {
    Day(2023, 4, Part4A(), Part4B())
}
