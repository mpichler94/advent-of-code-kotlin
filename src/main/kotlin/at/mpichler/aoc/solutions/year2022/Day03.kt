package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part3A : PartSolution() {
    internal lateinit var rucksacks: List<String>

    override fun parseInput(text: String) {
        rucksacks = text.trim().split("\n")
    }

    override fun compute(): Int {
        var sum = 0
        for (rucksack in rucksacks) {
            val compartment1 = rucksack.take(rucksack.length / 2).toSet()
            val compartment2 = rucksack.takeLast(rucksack.length / 2).toSet()

            val both = compartment1.intersect(compartment2)
            if (both.isNotEmpty()) {
                val char = both.first()
                sum += convertToNum(char)
            }
        }

        return sum
    }

    internal fun convertToNum(char: Char): Int {
        if (char.isUpperCase()) {
            return char.code - 65 + 27
        }
        return char.code - 97 + 1
    }

    override fun getExampleAnswer(): Int {
        return 157
    }
}

class Part3B : Part3A() {
    override fun getExampleAnswer(): Int {
        return 70
    }

    override fun compute(): Int {
        var sum = 0
        for (i in rucksacks.indices step 3) {
            val rucksack1 = rucksacks[i].toSet()
            val rucksack2 = rucksacks[i + 1].toSet()
            val rucksack3 = rucksacks[i + 2].toSet()

            var badge = rucksack1.intersect(rucksack2)
            badge = badge.intersect(rucksack3)

            if (badge.isNotEmpty()) {
                sum += convertToNum(badge.first())
            }
        }

        return sum
    }
}

fun main() {
    Day(2022, 3, Part3A(), Part3B())
}