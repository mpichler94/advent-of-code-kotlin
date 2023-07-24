package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part3A : PartSolution() {
    lateinit var lines: List<String>
    override fun parseInput(text: String) {
        lines = text.trimEnd().split("\n")
    }

    override fun compute(): Int {
        val mostCommon = getMostCommon(lines)
        val gamma = binToDec(mostCommon)
        val epsilon = binToDec(invertBits(mostCommon))

        return gamma * epsilon
    }

    fun getMostCommon(data: List<String>): String {
        val oneBits = getOneBits(data)

        val num = data.size
        var mostCommon = ""
        for (i in oneBits.indices) {
            val zeroBits = num - oneBits[i]
            mostCommon += if (oneBits[i] > zeroBits) {
                '1'
            } else if (oneBits[i] < zeroBits) {
                '0'
            } else {
                '1'
            }
        }

        return mostCommon
    }

    private fun getOneBits(data: List<String>): List<Int> {
        val numBits = data.first().length
        val oneBits = MutableList(numBits) { 0 }
        for (number in data) {
            for (i in number.indices) {
                if (number[i] == '1') {
                    oneBits[i] += 1
                }
            }
        }
        return oneBits
    }

    fun binToDec(number: String): Int {
        return number.toInt(2)
    }

    private fun invertBits(number: String): String {
        return number.map { if (it == '1') '0' else '1' }.joinToString(separator = "")
    }

    override fun getExampleAnswer(): Int {
        return 198
    }
}

class Part3B : Part3A() {
    override fun compute(): Int {
        val oxygenStr = getOxygen()
        val oxygen = binToDec(oxygenStr)

        val co2Str = getCo2()
        val co2 = binToDec(co2Str)

        return oxygen * co2
    }

    private fun getOxygen(): String {
        var data = lines.toList()
        for (i in data.first().indices) {
            data = getMostCommonNumbers(data, i)
        }

        return data.first()
    }

    private fun getMostCommonNumbers(data: List<String>, i: Int): List<String> {
        val mostCommon = getMostCommon(data)
        return data.filter { it[i] == mostCommon[i] }
    }

    private fun getCo2(): String {
        var data = lines.toList()
        for (i in data.first().indices) {
            data = getLeastCommonNumbers(data, i)
            if (data.size == 1) {
                break
            }
        }
        return data.first()
    }

    private fun getLeastCommonNumbers(data: List<String>, i: Int): List<String> {
        val mostCommon = getMostCommon(data)
        return data.filter { it[i] != mostCommon[i] }
    }

    override fun getExampleAnswer(): Int {
        return 230
    }
}

fun main() {
    Day(2021, 3, Part3A(), Part3B())
}