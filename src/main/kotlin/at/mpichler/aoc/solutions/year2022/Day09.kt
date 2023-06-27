package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import kotlin.math.absoluteValue
import kotlin.math.sign

open class Part9A : PartSolution() {
    private lateinit var commands: MutableList<Command>
    internal lateinit var pos: MutableList<Point>

    override fun parseInput(text: String) {
        commands = mutableListOf()
        for (line in text.trim().split("\n")) {
            val parts = line.split(" ")
            commands.add(Command(Direction(parts[0]), parts[1].toInt()))
        }
    }

    override fun config() {
        pos = MutableList(2) { Point() }
    }

    override fun compute(): Int {
        val positions = mutableListOf<Point>()
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

    private fun move(direction: Direction, pos: MutableList<Point>) {
        pos[0] = moveHead(direction, pos[0])

        for (i in 1 until pos.size) {
            pos[i] = moveTail(pos[i - 1], pos[i])
        }
    }

    private fun moveHead(direction: Direction, pos: Point): Point {
        return direction.dir + pos
    }

    private fun moveTail(headPos: Point, tailPos: Point): Point {
        val diff = headPos - tailPos
        if (diff.x.absoluteValue > 1) {
            if (headPos.y == tailPos.y) {
                return Point(tailPos.x + diff.x.sign, tailPos.y)
            }
            return Point(tailPos.x + diff.x.sign, tailPos.y + diff.y.sign)
        } else if (diff.y.absoluteValue > 1) {
            if (headPos.x == tailPos.x) {
                return Point(tailPos.x, tailPos.y + diff.y.sign)
            }
            return Point(tailPos.x + diff.x.sign, tailPos.y + diff.y.sign)
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
        return Direction.values().first { it.value == value.first() }
    }

    private enum class Direction(val value: Char, val dir: Point) {
        UP('U', Point(0, -1)),
        DOWN('D', Point(0, 1)),
        LEFT('L', Point(-1, 0)),
        RIGHT('R', Point(1, 0)),
    }

    internal data class Point(val x: Int, val y: Int) {
        constructor() : this(0, 0)

        operator fun plus(other: Point) = Point(x + other.x, y + other.y)
        operator fun minus(other: Point) = Point(x - other.x, y - other.y)
    }
}

class Part9B : Part9A() {
    override fun config() {
        pos = MutableList(10) { Point() }
    }

    override fun getExampleAnswer(): Int {
        return 1
    }
}

fun main() {
    Day(2022, 9, Part9A(), Part9B())
}