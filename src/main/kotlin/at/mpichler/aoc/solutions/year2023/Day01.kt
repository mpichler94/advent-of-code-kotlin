package at.mpichler.aoc.solutions.year2023

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part1A : PartSolution() {
    internal lateinit var texts: List<String>

    override fun parseInput(text: String) {
        texts = text.trim().split("\n")
    }

    override fun getExampleAnswer() = 142

    internal fun getNumber(nums: String) = ("" + nums.first() + nums.last()).toInt()

    override fun compute(): Int {
        return texts.sumOf { it.filter(Char::isDigit).let(this::getNumber) }
    }
}

class Part1B : Part1A() {
    private val digits = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9"
    )

    override fun getExampleInput() = """
        two1nine
        eightwothree
        abcone2threexyz
        xtwone3four
        4nineeightseven2
        zoneight234
        7pqrstsixteen
    """.trimIndent()

    override fun getExampleAnswer() = 281

    override fun compute(): Int {
        var sum = 0
        for (text in texts) {
            var nums = text
            while (true) {
                val changed = replaceNumbers(nums)
                if (changed == nums) {
                    nums = changed
                    break
                }
                nums = changed
            }
            nums = nums.filter { it.isDigit() }

            sum += getNumber(nums)
        }

        return sum
    }

    private fun replaceNumbers(text: String): String {
        var nums = text
        for (i in nums.indices) {
            for ((k, v) in digits) {
                if (nums.substring(i).startsWith(k)) {
                    nums = nums.replaceFirst(k, v)
                    return nums
                }
                if (nums.substring(0, nums.length - i).endsWith(k)) {
                    nums = nums.replace(k, v)
                    return nums
                }
            }
        }
        return text
    }
}

fun main() {
    Day(2023, 1, Part1A(), Part1B())
}
