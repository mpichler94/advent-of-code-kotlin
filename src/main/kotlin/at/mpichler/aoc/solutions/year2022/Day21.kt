package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part21A : PartSolution() {
    lateinit var monkeys: Map<String, Monkey>

    override fun parseInput(text: String) {
        val monkeys = mutableListOf<Monkey>()
        val pattern = Regex("(.*?): (.*)")
        for (line in text.trim().split("\n")) {
            val result = pattern.find(line)!!
            val op = result.groupValues[2]
            val parts = op.split(" ")
            if (parts.size > 1) {
                monkeys.add(Monkey(result.groupValues[1], listOf(parts[0], parts[2]), parts[1]))
            } else {
                monkeys.add(Monkey(result.groupValues[1], listOf(), op))
            }
        }

        this.monkeys = monkeys.associateBy { it.name }
    }

    override fun compute(): Long {
        return monkeys["root"]!!.getResult(monkeys)
    }

    override fun getExampleAnswer(): Int {
        return 152
    }

    data class Monkey(val name: String, val inputs: List<String>, val operation: String) {
        fun getResult(monkeys: Map<String, Monkey>): Long {
            if (inputs.isEmpty()) {
                return operation.toLong()
            }

            val arg1 = monkeys[inputs[0]]
            val arg2 = monkeys[inputs[1]]
            return when (operation) {
                "+" -> arg1!!.getResult(monkeys) + arg2!!.getResult(monkeys)
                "-" -> arg1!!.getResult(monkeys) - arg2!!.getResult(monkeys)
                "*" -> arg1!!.getResult(monkeys) * arg2!!.getResult(monkeys)
                "/" -> arg1!!.getResult(monkeys) / arg2!!.getResult(monkeys)
                else -> error("Invalid operation")
            }
        }
    }
}

class Part21B : Part21A() {
    override fun compute(): Long {
        val todo = ArrayDeque<String>()
        todo.add("humn")
        val newMonkeys = monkeys.toMutableMap()
        while (todo.isNotEmpty()) {
            val name = todo.removeFirst()
            for (monkey in monkeys.values) {
                if (name in monkey.inputs) {
                    val otherMonkey = if (name == monkey.inputs[0]) monkey.inputs[1] else monkey.inputs[0]
                    if (monkey.name == "root") {
                        newMonkeys[name] = Monkey(name, listOf(), monkeys[otherMonkey]!!.getResult(monkeys).toString())
                        continue
                    }
                    val parts = reorder(monkey, name).split(" ")
                    val inputs = listOf(parts[0], parts[2])
                    newMonkeys[name] = Monkey(name, inputs, parts[1])
                    todo.addLast(monkey.name)
                }
            }
        }

        return newMonkeys["humn"]!!.getResult(newMonkeys)
    }

    private fun reorder(monkey:Monkey, target: String): String {
        val left = monkey.inputs[0]
        val right = monkey.inputs[1]
        val op = monkey.operation
        val other = if (target == left) right else left

        return when (op) {
            "+" -> "${monkey.name} - $other"
            "-" -> {
                if (target == left) {
                    "${monkey.name} + $right"
                } else {
                    "$left - ${monkey.name}"
                }
            }
            "*" -> "${monkey.name} / $other"
            "/" -> {
                if (target == left) {
                    "${monkey.name} * $right"
                } else {
                    "$left / ${monkey.name}"
                }
            }
            else -> error("Unsupported operation")
        }
    }

    override fun getExampleAnswer(): Int {
        return 301
    }
}

fun main() {
    Day(2022, 21, Part21A(), Part21B())
}