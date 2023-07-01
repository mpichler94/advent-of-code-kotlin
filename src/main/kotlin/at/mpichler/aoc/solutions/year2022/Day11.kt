package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part11A : PartSolution() {
    internal lateinit var monkeys: MutableList<Monkey>
    internal open val numRounds = 20

    override fun parseInput(text: String) {
        val lines = text.trim().split("\n")
        monkeys = mutableListOf()
        for (i in lines.indices step 7) {
            val items = lines[i + 1].substring(18).split(", ").map { it.toLong() }.toMutableList()
            val op = if (lines[i + 2].substring(23, 24) == Monkey.Operation.ADD.value) {
                Monkey.Operation.ADD
            } else {
                Monkey.Operation.MUL
            }
            val operand = lines[i + 2].substring(25).toIntOrNull() ?: 0
            val divisor = lines[i + 3].substring(21).toLong()
            val trueNum = lines[i + 4].substring(29).toInt()
            val falseNum = lines[i + 5].substring(30).toInt()
            monkeys.add(Monkey(items, op, operand, divisor, trueNum, falseNum))
        }
    }

    override fun compute(): Long {
        repeat(numRounds) {
            monkeys.forEach { it.round(monkeys) }
        }

        val counts = monkeys.map { it.count }.toMutableList()
        counts.sort()
        return counts.takeLast(2).reduce { acc, i -> acc * i }
    }

    override fun getExampleAnswer(): Any {
        return 10_605
    }

    internal data class Monkey(
        private val items: MutableList<Long>,
        private val operation: Operation,
        private val operand: Int,
        private val divisor: Long,
        val trueNum: Int,
        val falseNum: Int,
        var relief: Boolean = true
    ) {
        private var fac = 0L
        internal var count = 0L
            private set

        private fun addItem(item: Long) {
            items.add(item)
        }

        fun round(monkeys: List<Monkey>) {
            fac = monkeys.map { it.divisor }.reduce { acc, i -> acc * i }
            while (items.isNotEmpty()) {
                processItem(monkeys, items.removeFirst())
            }
        }

        private fun processItem(monkeys: List<Monkey>, item: Long) {
            count += 1
            var newValue = operation.exec(item, operand)
            if (relief) {
                newValue /= 3
            } else {
                newValue %= fac
            }
            if (newValue % divisor == 0L) {
                monkeys[trueNum].addItem(newValue)
            } else {
                monkeys[falseNum].addItem(newValue)
            }
        }

        enum class Operation(val value: String) {
            ADD("+") {
                override fun exec(old: Long, operand: Int): Long {
                    if (operand == 0) {
                        return 2 * old
                    }
                    return old + operand
                }
            },
            MUL("*") {
                override fun exec(old: Long, operand: Int): Long {
                    if (operand == 0) {
                        return old * old
                    }
                    return old * operand
                }
            };

            abstract fun exec(old: Long, operand: Int): Long
        }
    }
}

class Part11B : Part11A() {
    override val numRounds = 10_000

    override fun config() {
        monkeys.forEach { it.relief = false }
    }

    override fun getExampleAnswer(): Long {
        return 2_713_310_158
    }
}

fun main() {
    Day(2022, 11, Part11A(), Part11B())
}