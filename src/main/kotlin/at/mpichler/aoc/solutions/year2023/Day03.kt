package at.mpichler.aoc.solutions.year2023

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part3A : PartSolution() {
    internal lateinit var symbols: List<Symbol>
    internal lateinit var numbers: List<Number>

    override fun parseInput(text: String) {
        val numbers = mutableListOf<Number>()
        val symbols = mutableListOf<Symbol>()
        for ((y, line) in text.split("\n").withIndex()) {
            var value = ""
            for ((x, c) in line.withIndex()) {
                if (!c.isDigit() && value.isNotEmpty()) {
                    numbers.add(Number(value.toInt(), x - value.length, y))
                    value = ""
                } else if (c.isDigit()) {
                    value += c
                }
                if (c != '.' && !c.isDigit()) {
                    symbols.add(Symbol(c, x, y))
                }
            }
            if (value.isNotEmpty()) {
                numbers.add(Number(value.toInt(), line.length - value.length, y))
            }
        }

        this.numbers = numbers
        this.symbols = symbols
    }

    override fun getExampleAnswer() = 4361

    override fun compute(): Int {
        return numbers.filter { num -> symbols.any { num.isAdjacent(it) } }.sumOf { it.value }
    }

    internal data class Symbol(val value: Char, val x: Int, val y: Int)

    internal data class Number(val value: Int, val x: Int, val y: Int) {
        fun isAdjacent(pos: Symbol): Boolean {
            if (pos.y < y - 1 || pos.y > y + 1) {
                return false
            }
            if (pos.x < x - 1 || pos.x > x + value.toString().length) {
                return false
            }

            return true
        }
    }
}

class Part3B : Part3A() {
    override fun getExampleAnswer() = 467835

    override fun compute(): Int {
        return symbols.filter { it.value == '*' }.map {
            val products = numbers.filter { num -> num.isAdjacent(it) }.map { it.value }
            return@map if (products.size == 2) {
                products.fold(1) { acc, i -> acc * i }
            } else {
                0
            }
        }.sum()
    }
}

fun main() {
    Day(2023, 3, Part3A(), Part3B())
}
