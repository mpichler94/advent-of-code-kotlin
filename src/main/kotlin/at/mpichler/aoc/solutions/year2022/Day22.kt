package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import at.mpichler.aoc.lib.Vector2i
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get

open class Part22A : PartSolution() {
    lateinit var board: D2Array<Int>
    lateinit var path: List<String>


    override fun parseInput(text: String) {
        val board = mutableListOf<MutableList<Int>>()
        val lines = text.trimEnd().split("\n")
        var end = false
        for (line in lines) {
            val row = mutableListOf<Int>()
            if (line.isEmpty()) {
                end = true
                continue
            }
            if (end) {
                path = parsePath(line)
                break
            }
            for (char in line) {
                when (char) {
                    ' ' -> row.add(0)
                    '.' -> row.add(1)
                    '#' -> row.add(2)
                }
            }
            board.add(row)
        }

        val maxLen = board.maxOf { it.size } + 2
        for (row in board) {
            row.add(0, 0)
            repeat(maxLen - row.size) {
                row.add(0)
            }
        }
        board.add(0, MutableList(maxLen) { 0 })
        board.add(MutableList(maxLen) { 0 })

        this.board = mk.ndarray(board)
        this.board = this.board.transpose()
    }

    private fun parsePath(line: String): List<String> {
        val instructions = mutableListOf<String>()
        var command = ""
        for (char in line) {
            if (char.isDigit()) {
                command += char
                continue
            }
            if (command.isNotEmpty()) {
                instructions.add(command)
                command = ""
            }
            instructions.add(char.toString())
        }

        instructions.add(command)
        return instructions
    }

    override fun compute(): Int {
        var pos = findStart()
        var direction = 0
        for (instruction in path) {
            if (instruction.first().isLetter()) {
                direction = if (instruction == "R") {
                    (direction + 1).mod(4)
                } else {
                    (direction - 1).mod(4)
                }
                continue
            }

            repeat(instruction.toInt()) {
                val newPos = step(pos, direction)
                if (newPos == pos) {
                    return@repeat
                }
                pos = newPos
            }
        }
        return 1000 * pos.y + 4 * pos.x + direction
    }

    protected fun findStart(): Vector2i {
        for (y in 0..<board.shape[1]) {
            for (x in 0..<board.shape[0]) {
                if (board[x, y] == 1) {
                    return Vector2i(x, y)
                }
            }
        }

        error("No start position found")
    }

    private fun step(pos: Vector2i, direction: Int): Vector2i {
        var newX = pos.x
        var newY = pos.y

        when (direction) {
            0 -> newX = pos.x + 1
            2 -> newX = pos.x - 1
            1 -> newY = pos.y + 1
            3 -> newY = pos.y - 1
            else -> error("Unsupported direction")
        }

        if (board[newX, newY] == 2) {
            return pos
        }

        if (board[newX, newY] == 0) {
            if (direction == 0) {
                for (newX in newX - 1 downTo 0) {
                    if (board[newX, newY] == 0 && board[newX + 1, newY] == 1) {
                        return Vector2i(newX + 1, newY)
                    }
                }
            }
            if (direction == 2) {
                for (newX in newX + 1..<board.shape[0]) {
                    if (board[newX, newY] == 0 && board[newX - 1, newY] == 1) {
                        return Vector2i(newX - 1, newY)
                    }
                }
            }
            if (direction == 1) {
                for (newY in newY - 1 downTo 0) {
                    if (board[newX, newY] == 0 && board[newX, newY + 1] == 1) {
                        return Vector2i(newX, newY + 1)
                    }
                }
            }
            if (direction == 3) {
                for (newY in newY + 1..<board.shape[1]) {
                    if (board[newX, newY] == 0 && board[newX, newY - 1] == 1) {
                        return Vector2i(newX, newY - 1)
                    }
                }
            }
            return pos
        }

        return Vector2i(newX, newY)
    }

    override fun getExampleAnswer(): Int {
        return 6032
    }

}

class Part22B : Part22A() {
    private var sideLen = 0
    private lateinit var sides: List<Pair<Int, Int>>
    private lateinit var transitions: Map<Pair<Int, Int>, Pair<Int, Int>>

    override fun config() {
        if (board.shape[0] == 18) { // Example
            sideLen = 4
            sides = listOf(9 to 1, 1 to 5, 5 to 5, 9 to 5, 9 to 9, 13 to 9) // Start positions of cube sides
            // 0r 1d 2l 3u
            // dict with (side, direction) as key -> adjacent (side, direction) as value
            transitions = mapOf(
                Pair(0, 0) to Pair(5, 2),
                Pair(0, 2) to Pair(2, 1),
                Pair(0, 3) to Pair(1, 1),
                Pair(1, 2) to Pair(6, 3),
                Pair(1, 3) to Pair(0, 1),
                Pair(2, 1) to Pair(4, 0),
                Pair(2, 3) to Pair(0, 0),
                Pair(3, 0) to Pair(5, 1),
                Pair(4, 1) to Pair(1, 3),
                Pair(4, 2) to Pair(2, 3),
                Pair(5, 0) to Pair(0, 2),
                Pair(5, 1) to Pair(1, 0),
                Pair(5, 3) to Pair(3, 2),
            )
        } else {
            sideLen = 50
            sides = listOf(51 to 1, 101 to 1, 51 to 51, 1 to 101, 51 to 101, 1 to 151)
            transitions = mapOf(
                Pair(0, 2) to Pair(3, 0),
                Pair(0, 3) to Pair(5, 0),
                Pair(1, 0) to Pair(4, 2),
                Pair(1, 1) to Pair(2, 2),
                Pair(1, 3) to Pair(5, 3),
                Pair(2, 0) to Pair(1, 3),
                Pair(2, 2) to Pair(3, 1),
                Pair(3, 2) to Pair(0, 0),
                Pair(3, 3) to Pair(2, 0),
                Pair(4, 0) to Pair(1, 2),
                Pair(4, 1) to Pair(5, 2),
                Pair(5, 0) to Pair(4, 3),
                Pair(5, 1) to Pair(1, 1),
                Pair(5, 2) to Pair(0, 1),
            )
        }
    }

    override fun compute(): Int {
        var pos = findStart()
        var direction = 0
        var side = 0
        var i = 0
        for (instruction in path) {
            if (instruction.first().isLetter()) {
                direction = if (instruction == "R") {
                    (direction + 1).mod(4)
                } else {
                    (direction - 1).mod(4)
                }
                i += 1
                continue
            }

            repeat(instruction.toInt()) {
                val (newPos, newDir, newSide) = step(side, pos, direction)
                direction = newDir
                side = newSide
                if (newPos == pos) {
                    return@repeat
                }
                pos = newPos
                i += 1
            }
        }
        return 1000 * pos.y + 4 * pos.x + direction
    }

    private fun step(side: Int, pos: Vector2i, direction: Int): State {
        var newX = pos.x
        var newY = pos.y
        var newSide = side
        var newDirection = direction

        when (direction) {
            0 -> newX = pos.x + 1
            2 -> newX = pos.x - 1
            1 -> newY = pos.y + 1
            3 -> newY = pos.y - 1
            else -> error("Unsupported direction")
        }

        if (board[newX, newY] == 2) {
            return State(pos, direction, side)
        }

        val origin = sides[side]

        if (board[newX, newY] == 0) {
            if (Pair(side, direction) !in transitions) {
                error("Impossible")
            }
            newSide = transitions[Pair(side, direction)]!!.first
            newDirection = transitions[Pair(side, direction)]!!.second
            val newOrigin = sides[newSide]

            if (direction == 0) {
                when (newDirection) {
                    0 -> {
                        newX = newOrigin.first
                        newY = newOrigin.second + pos.y - origin.second
                    }

                    1 -> {
                        newX = newOrigin.first + origin.second + sideLen - 1 - pos.y
                        newY = newOrigin.second
                    }

                    2 -> {
                        newX = newOrigin.first + sideLen - 1
                        newY = newOrigin.second + origin.second +  sideLen - 1 - pos.y
                    }

                    3 -> {
                        newX = newOrigin.first + pos.y - origin.second
                        newY = newOrigin.second + sideLen - 1
                    }
                }
            }
            if (direction == 1) {
                when (newDirection) {
                    0 -> {
                        newX = newOrigin.first
                        newY = newOrigin.second + origin.first + sideLen - 1 - pos.x
                    }

                    1 -> {
                        newX = newOrigin.first + pos.x - origin.first
                        newY = newOrigin.second
                    }

                    2 -> {
                        newX = newOrigin.first + sideLen - 1
                        newY = newOrigin.second + pos.x - origin.first
                    }

                    3 -> {
                        newX = newOrigin.first + origin.first + sideLen - 1 - pos.x
                        newY = newOrigin.second + sideLen - 1
                    }
                }
            }
            if (direction == 2) {
                when (newDirection) {
                    0 -> {
                        newX = newOrigin.first
                        newY = newOrigin.second + origin.second + sideLen - 1 - pos.y
                    }

                    1 -> {
                        newX = newOrigin.first + pos.y - origin.second
                        newY = newOrigin.second
                    }

                    2 -> {
                        newX = newOrigin.first + sideLen - 1
                        newY = newOrigin.second + pos.y - origin.second
                    }

                    3 -> {
                        newX = newOrigin.first + origin.second + sideLen - 1 - pos.y
                        newY = newOrigin.second + sideLen - 1
                    }
                }
            }
            if (direction == 3) {
                when (newDirection) {
                    0 -> {
                        newX = newOrigin.first
                        newY = newOrigin.second + pos.x - origin.first
                    }

                    1 -> {
                        newX = newOrigin.first + origin.first + sideLen - 1 - pos.x
                        newY = newOrigin.second
                    }

                    2 -> {
                        newX = newOrigin.first + sideLen - 1
                        newY = newOrigin.second + origin.first + sideLen - 1 - pos.x
                    }

                    3 -> {
                        newX = newOrigin.first + pos.x - origin.first
                        newY = newOrigin.second + sideLen - 1
                    }
                }
            }

            if (board[newX, newY] == 2) {
                return State(pos, direction, side)
            }
        }

        newSide = getSide(newSide, newX, newY)

        return State(Vector2i(newX, newY), newDirection, newSide)
    }

    private fun getSide(oldSide: Int, x: Int, y: Int): Int {
        for ((side, origin) in sides.withIndex()) {
            if (x in origin.first..<origin.first + sideLen && y in origin.second..<origin.second + sideLen) {
                return side
            }
        }
        return oldSide
    }

    override fun getExampleAnswer(): Int {
        return 5031
    }

    data class State(val newPos: Vector2i, val newDirection: Int, val newSide: Int)
}

fun main() {
    Day(2022, 22, Part22A(), Part22B())
}