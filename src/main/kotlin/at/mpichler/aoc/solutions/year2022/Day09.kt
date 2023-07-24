package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import at.mpichler.aoc.lib.Vector2i
import kotlin.math.absoluteValue
import kotlin.math.sign

open class Part9A : PartSolution() {
    private lateinit var commands: MutableList<Command>
    internal lateinit var pos: MutableList<Vector2i>

    override fun parseInput(text: String) {
        commands = mutableListOf()
        for (line in text.trim().split("\n")) {
            val parts = line.split(" ")
            commands.add(Command(Direction(parts[0]), parts[1].toInt()))
        }
    }

    override fun config() {
        pos = MutableList(2) { Vector2i() }
    }

    override fun compute(): Int {
        val positions = mutableListOf<Vector2i>()
        var count = 0
        for (command in commands) {
            repeat(command.count) {
                move(command.direction, pos)
                if (!positions.contains(pos.last())) {
                    positions.add(pos.last())
                    count += 1
                }
            }
        }

        return count
    }

    private fun move(direction: Direction, pos: MutableList<Vector2i>) {
        pos[0] = moveHead(direction, pos[0])

        for (i in 1..<pos.size) {
            pos[i] = moveTail(pos[i - 1], pos[i])
        }
    }

    private fun moveHead(direction: Direction, pos: Vector2i): Vector2i {
        return direction.dir + pos
    }

    private fun moveTail(headPos: Vector2i, tailPos: Vector2i): Vector2i {
        val diff = headPos - tailPos
        if (diff.x.absoluteValue > 1) {
            if (headPos.y == tailPos.y) {
                return Vector2i(tailPos.x + diff.x.sign, tailPos.y)
            }
            return Vector2i(tailPos.x + diff.x.sign, tailPos.y + diff.y.sign)
        } else if (diff.y.absoluteValue > 1) {
            if (headPos.x == tailPos.x) {
                return Vector2i(tailPos.x, tailPos.y + diff.y.sign)
            }
            return Vector2i(tailPos.x + diff.x.sign, tailPos.y + diff.y.sign)
        }
        return tailPos
    }

    override fun getExampleAnswer(): Int {
        return 13
    }

    override fun getExampleInput(): String? {
        return """
            R 4
            U 4
            L 3
            D 1
            R 4
            D 1
            L 5
            R 2
        """.trimIndent()
    }

    private data class Command(val direction: Direction, val count: Int)

    private fun Direction(value: String): Direction {
        return Direction.entries.first { it.value == value.first() }
    }

    private enum class Direction(val value: Char, val dir: Vector2i) {
        UP('U', Vector2i(0, -1)),
        DOWN('D', Vector2i(0, 1)),
        LEFT('L', Vector2i(-1, 0)),
        RIGHT('R', Vector2i(1, 0)),
    }
}

class Part9B : Part9A() {
    override fun config() {
        pos = MutableList(10) { Vector2i() }
    }

    override fun getExampleAnswer(): Int {
        return 1
    }
}

fun main() {
    Day(2022, 9, Part9A(), Part9B())
}