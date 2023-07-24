package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sign

open class Part25A : PartSolution() {
    private lateinit var snafu: List<String>

    override fun parseInput(text: String) {
        snafu = text.trimEnd().split("\n")
    }

    override fun compute(): String {
        var sum = 0L

        for (line in snafu) {
            val dec = snafuToDecimal(line)
            sum += dec
        }

        return decimalToSnafu(sum)
    }

    private fun snafuToDecimal(snafu: String): Long {
        var dec = 0L
        for ((i, char) in snafu.withIndex()) {
            val mul = 5.0.pow(snafu.length - i - 1).toLong()

            when (char) {
                '0' -> continue
                '1' -> dec += mul
                '2' -> dec += 2 * mul
                '-' -> dec -= mul
                '=' -> dec -= 2 * mul
            }
        }

        return dec
    }

    private fun decimalToSnafu(d: Long): String {
        var decimal = d
        val snafu = mutableListOf<Int>()
        var div = decimal
        var fac = 0
        while (div.absoluteValue >= 5) {
            div /= 5
            fac += 1
        }

        while (true) {
            div = decimal / 5.0.pow(fac).toLong()

            snafu.add(div.toInt())
            decimal -= div * 5.0.pow(fac).toLong()

            var newFac = fac - 1
            if (div.absoluteValue > 2) {
                val lastIndex = snafu.size - 1
                for (i in lastIndex downTo 0) {
                    if (snafu[i].absoluteValue > 2) {
                        decimal += snafu[i] * 5.0.pow(lastIndex - i + fac).toLong()
                        val sign = snafu[i].sign
                        snafu.removeLast()
                        newFac += 1

                        if (i > 0) {
                            snafu[i - 1] += sign
                            decimal -= sign * 5.0.pow(lastIndex - i + 1 + fac).toLong()
                        } else {
                            snafu.add(0, sign)
                            decimal -= sign * 5.0.pow(lastIndex + 1 + fac).toLong()
                        }
                    }
                }
            }

            if (decimal == 0L && fac == 0) {
                break
            }

            fac = newFac
        }

        return digitsToString(snafu)
    }

    private fun digitsToString(digits: List<Int>): String {
        var result = ""
        for (digit in digits) {
            when (digit) {
                0 -> result += "0"
                1 -> result += "1"
                2 -> result += "2"
                -1 -> result += '-'
                -2 -> result += "="
            }
        }
        return result
    }

    override fun getExampleAnswer(): String {
        return "2=-1=0"
    }
}

fun main() {
    Day(2022, 25, Part25A())
}