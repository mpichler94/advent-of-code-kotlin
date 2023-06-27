package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import kotlin.math.absoluteValue

open class Part10A : PartSolution() {
    internal lateinit var commands: MutableList<Command>
    private val targets = List(6) { 40 * it + 20 }

    override fun parseInput(text: String) {
        commands = mutableListOf()
        for (line in text.trim().split("\n")) {
            if (line.startsWith("addx")) {
                val parts = line.split(" ")
                commands.add(Add(parts[1].toInt()))
            } else {
                commands.add(Nop())
            }
        }
    }

    override fun compute(): Int {
        var cycle = 1
        var x = 1
        val values = mutableListOf<Int>()
        var sum = 0

        for (command in commands) {
            cycle += 1
            if (command is Add) {
                if (cycle in targets) {
                    values.add(x)
                    sum += x * cycle
                }
                cycle += command.cycles
                x += command.param
            }

            if (cycle in targets) {
                values.add(x)
                sum += x * cycle
            }
        }

        return sum
    }

    override fun getExampleAnswer(): Int {
        return 13_140
    }

    override fun getExampleInput(): String? {
        return """
            addx 15
            addx -11
            addx 6
            addx -3
            addx 5
            addx -1
            addx -8
            addx 13
            addx 4
            noop
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx 5
            addx -1
            addx -35
            addx 1
            addx 24
            addx -19
            addx 1
            addx 16
            addx -11
            noop
            noop
            addx 21
            addx -15
            noop
            noop
            addx -3
            addx 9
            addx 1
            addx -3
            addx 8
            addx 1
            addx 5
            noop
            noop
            noop
            noop
            noop
            addx -36
            noop
            addx 1
            addx 7
            noop
            noop
            noop
            addx 2
            addx 6
            noop
            noop
            noop
            noop
            noop
            addx 1
            noop
            noop
            addx 7
            addx 1
            noop
            addx -13
            addx 13
            addx 7
            noop
            addx 1
            addx -33
            noop
            noop
            noop
            addx 2
            noop
            noop
            noop
            addx 8
            noop
            addx -1
            addx 2
            addx 1
            noop
            addx 17
            addx -9
            addx 1
            addx 1
            addx -3
            addx 11
            noop
            noop
            addx 1
            noop
            addx 1
            noop
            noop
            addx -13
            addx -19
            addx 1
            addx 3
            addx 26
            addx -30
            addx 12
            addx -1
            addx 3
            addx 1
            noop
            noop
            noop
            addx -9
            addx 18
            addx 1
            addx 2
            noop
            noop
            addx 9
            noop
            noop
            noop
            addx -1
            addx 2
            addx -37
            addx 1
            addx 3
            noop
            addx 15
            addx -21
            addx 22
            addx -6
            addx 1
            noop
            addx 2
            addx 1
            noop
            addx -10
            noop
            noop
            addx 20
            addx 1
            addx 2
            addx 2
            addx -6
            addx -11
            noop
            noop
            noop      
        """.trimIndent()
    }

    internal sealed class Command(val cycles: Int)
    internal class Nop : Command(0)
    internal data class Add(val param: Int) : Command(1)
}

class Part10B : Part10A() {
    override fun compute(): Int {
        var cycle = 0
        var x = 1
        val image = mutableListOf<String>()
        val row = mutableListOf<Char>()

        for (command in commands) {
            if (row.size >= 40) {
                image.add(row.joinToString(""))
                row.clear()
            }
            if ((cycle - x - 40 * image.size).absoluteValue < 2) {
                row.add('#')
            } else {
                row.add('.')
            }

            cycle += 1
            if (command is Add) {
                if (row.size >= 40) {
                    image.add(row.joinToString(""))
                    row.clear()
                }
                if ((cycle - x - 40 * image.size).absoluteValue < 2) {
                    row.add('#')
                } else {
                    row.add('.')
                }
                cycle += 1
                x += command.param
            }
        }

        image.add(row.joinToString(""))
        println("Lit pixels:")
        for (i in 0 until image.size) {
            println(image[i])
        }

        return 0
    }

    override fun getExampleAnswer(): Int {
        return 0
    }
}

fun main() {
    Day(2022, 10, Part10A(), Part10B(), false)
}