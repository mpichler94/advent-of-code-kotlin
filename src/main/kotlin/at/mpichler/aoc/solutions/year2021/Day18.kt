package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import kotlin.math.ceil

open class Part18A : PartSolution() {
    private val pairPattern = Regex("""\[(\d+),(\d+)]""")
    private val numberPattern = Regex("""\d+""")
    private val doubleDigitPattern = Regex("""\d\d""")

    lateinit var numbers: List<String>

    override fun parseInput(text: String) {
        numbers = text.split("\n")
    }

    override fun compute(): Int {
        val number = add(numbers)
        return magnitude(number)
    }

    fun add(numbers: Iterable<String>): String {
        var left = numbers.first()
        for (right in numbers.drop(1)) {
            left = reduce("[$left,$right]")
        }
        return left
    }

    private tailrec fun reduce(number: String): String {
        var reduced = explode(number)
        if (reduced != number) {
            return reduce(reduced)
        }

        reduced = split(number)
        if (reduced != number) {
            return reduce(reduced)
        }

        return reduced
    }

    private fun explode(num: String): String {
        var i = 0
        var number = num
        while (i < number.length) {
            val pair = pairPattern.find(number.substring(i)) ?: break
            var depth = number.substring(0, pair.range.first + i).count { it == '[' }
            depth -= number.substring(0, pair.range.first + i).count { it == ']' }
            if (depth < 4) {
                i += pair.range.last + 1
                continue
            }

            val lAdd = pair.groupValues[1].toInt()
            val rAdd = pair.groupValues[2].toInt()
            var leftPart = number.substring(0, pair.range.first + i).reversed()
            var rightPart = number.substring(pair.range.last + i + 1)
            val left = numberPattern.find(leftPart)
            if (left != null) {
                leftPart = "${leftPart.substring(0, left.range.first)}${
                    (lAdd + left.value.reversed().toInt()).toString().reversed()
                }${leftPart.substring(left.range.last + 1)}"
            }
            val right = numberPattern.find(number.substring(pair.range.last + i + 1))
            if (right != null) {
                rightPart = "${rightPart.substring(0, right.range.first)}${rAdd + right.value.toInt()}${
                    rightPart.substring(right.range.last + 1)
                }"
            }
            number = "${leftPart.reversed()}0${rightPart}"
            break
        }
        return number
    }

    fun split(number: String): String {
        val regularNum = doubleDigitPattern.find(number) ?: return number

        val left = regularNum.value.toInt() / 2
        val right = ceil(regularNum.value.toDouble() / 2).toInt()
        return "${number.substring(0, regularNum.range.first)}[$left,$right]${
            number.substring(regularNum.range.last + 1)}"
    }

    fun magnitude(num: String): Int {
        var number = num
        while (true) {
            val pair = pairPattern.find(number) ?: return number.toInt()
            val mag = 3 * pair.groupValues[1].toInt() + 2 * pair.groupValues[2].toInt()
            number = "${number.substring(0, pair.range.first)}$mag${number.substring(pair.range.last + 1)}"
        }
    }

    override fun getExampleAnswer(): Int {
        return 4230
    }
}

class Part18B : Part18A() {
    override fun compute(): Int {
        val magnitudes = mutableListOf<Int>()
        for (pair in permutations(numbers)) {
            val number = add(pair.toList())
            magnitudes.add(magnitude(number))
        }

        return magnitudes.max()
    }

    private fun permutations(list: List<String>): Iterable<Pair<String, String>> {
        return buildList {
            for (first in list) {
                for (second in list) {
                    if (first != second) {
                        add(first to second)
                    }
                }
            }
        }
    }

    override fun getExampleAnswer(): Int {
        return 4647
    }
}

fun main() {
    Day(2021, 18, Part18A(), Part18B())
}