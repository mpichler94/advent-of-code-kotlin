package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part8A : PartSolution() {
    lateinit var signalPatterns: List<MutableList<String>>
    lateinit var outputs: List<List<String>>

    override fun parseInput(text: String) {
        val lines = text.split("\n")

        val signalPatterns = mutableListOf<MutableList<String>>()
        val outputs = mutableListOf<List<String>>()

        for (row in lines) {
            val parts = row.split(" | ")
            val patterns = parts[0].split(' ').map { it.toCharArray().sorted().joinToString(separator = "") }
            val rowOutputs = parts[1].split(' ').map { it.toCharArray().sorted().joinToString(separator = "") }

            signalPatterns.add(patterns.toMutableList())
            outputs.add(rowOutputs)
        }

        this.signalPatterns = signalPatterns
        this.outputs = outputs
    }

    override fun compute(): Int {
        var count = 0
        for (row in outputs) {
            for (output in row) {
                val length = output.length
                if (length < 5 || length == 7) {
                    count += 1
                }
            }
        }

        return count
    }

    override fun getExampleAnswer(): Int {
        return 26
    }

    override fun getExampleInput(): String? {
        return """
            be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
            edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
            fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
            fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
            aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
            fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
            dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
            bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
            egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
            gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
        """.trimIndent()
    }

}

class Part8B : Part8A() {
    override fun compute(): Int {
        var outputSum = 0
        for ((patterns, outputs) in signalPatterns.zip(outputs)) {
            val digits = MutableList(10) { "" }
            val tmp = findUnique(patterns)
            digits[1] = tmp[0]
            digits[4] = tmp[1]
            digits[7] = tmp[2]
            digits[8] = tmp[3]
            digits[6] = find6(patterns, digits[1])
            digits[9] = find9(patterns, digits[4])
            digits[0] = find0(patterns, digits[6], digits[9])
            digits[3] = find3(patterns, digits[1], digits[9])
            digits[5] = find5(patterns, digits[6])
            digits[2] = find2(patterns, digits[3], digits[5])

            val lookup = digits.withIndex().associate { it.value to it.index.toString() }
            val number = outputs.mapNotNull { lookup[it] }.joinToString(separator = "")
            outputSum += number.toInt()
        }

        return outputSum
    }

    // find the digits that are unique and remove them from the patterns
    private fun findUnique(patterns: MutableList<String>): List<String> {
        var one = ""
        var four = ""
        var seven = ""
        var eight = ""

        for (pattern in patterns) {
            when (pattern.length) {
                2 -> one = pattern
                3 -> seven = pattern
                4 -> four = pattern
                7 -> eight = pattern
            }
        }

        patterns.remove(one)
        patterns.remove(seven)
        patterns.remove(four)
        patterns.remove(eight)
        return listOf(one, four, seven, eight)
    }

    private fun find6(patterns: MutableList<String>, one: String): String {
        for (pattern in patterns) {
            if (pattern.length != 6) {
                continue
            }
            if (hasOne(one, pattern)) {
                patterns.remove(pattern)
                return pattern
            }
        }

        return ""
    }

    private fun find9(patterns: MutableList<String>, four: String): String {
        for (pattern in patterns) {
            if (pattern.length != 6) {
                continue
            }
            if (hasAll(four, pattern)) {
                patterns.remove(pattern)
                return pattern
            }
        }

        return ""
    }

    private fun find0(patterns: MutableList<String>, six: String, nine: String): String {
        for (pattern in patterns) {
            if (pattern.length != 6) {
                continue
            }
            if (pattern != six && pattern != nine) {
                patterns.remove(pattern)
                return pattern
            }
        }

        return ""
    }

    private fun find3(patterns: MutableList<String>, one: String, nine: String): String {
        for (pattern in patterns) {
            if (pattern.length != 5) {
                continue
            }
            if (hasAll(one, pattern) && hasAllButOne(nine, pattern)) {
                patterns.remove(pattern)
                return pattern
            }
        }

        return ""
    }

    private fun find5(patterns: MutableList<String>, six: String): String {
        for (pattern in patterns) {
            if (pattern.length != 5) {
                continue
            }
            if (hasAll(pattern, six)) {
                patterns.remove(pattern)
                return pattern
            }
        }

        return ""
    }

    private fun find2(patterns: MutableList<String>, three: String, five: String): String {
        for (pattern in patterns) {
            if (pattern.length != 5) {
                continue
            }
            if (pattern != three && pattern != five) {
                patterns.remove(pattern)
                return pattern
            }
        }

        return ""
    }

    // Check if one and only one of chars is in pattern
    private fun hasOne(chars: String, pattern: String): Boolean {
        return chars.map { it in pattern }.count { it } == 1
    }

    // Check if all of chars is in pattern
    private fun hasAll(chars: String, pattern: String): Boolean {
        val checks = chars.map { it in pattern }
        return checks.all { it }
    }

    // Check if all but one of chars is in pattern
    private fun hasAllButOne(chars: String, pattern: String): Boolean {
        val checks = chars.map { it !in pattern }
        return checks.count { it } == 1
    }

    override fun getExampleAnswer(): Int {
        return 61229
    }
}

fun main() {
    Day(2021, 8, Part8A(), Part8B())
}