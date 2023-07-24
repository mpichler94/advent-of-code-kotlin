package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.data.set
import org.jetbrains.kotlinx.multik.ndarray.operations.any
import org.jetbrains.kotlinx.multik.ndarray.operations.map
import org.jetbrains.kotlinx.multik.ndarray.operations.mapMultiIndexed
import org.jetbrains.kotlinx.multik.ndarray.operations.plusAssign

open class Part4A : PartSolution() {
    lateinit var numbers: List<Int>
    lateinit var boards: List<D2Array<Int>>

    override fun parseInput(text: String) {
        val lines = text.trimEnd().split("\n")

        numbers = lines.first().split(',').map { it.toInt() }
        val boards = mutableListOf<D2Array<Int>>()

        var board = mk.zeros<Int>(5, 5)
        var y = 0
        for (boardId in 2..<lines.size) {
            val line = lines[boardId]
            if (line == "") {
                y = 0
                boards.add(board)
                board = mk.zeros<Int>(5, 5)
                continue
            }

            val row = line.trim().split(Regex(" +"))
            for (x in row.indices) {
                board[x, y] = row[x].toInt()
            }
            y += 1
        }

        boards.add(board)
        this.boards = boards
    }

    override fun compute(): Int {
        val marks = List(boards.size) { mk.zeros<Int>(5, 5) }

        for (num in numbers) {
            markNumber(num, boards, marks)
            val idx = checkWin(marks)
            if (idx >= 0) {
                return computeScore(num, boards[idx], marks[idx])
            }
        }

        error("No winning score found")
    }

    fun markNumber(number: Int, boards: List<D2Array<Int>>, marks: List<D2Array<Int>>) {
        for (i in boards.indices) {
            val indices = boards[i].map { if (it == number) 1 else 0 }
            marks[i] += indices
        }
    }

    fun checkWin(marks: List<D2Array<Int>>): Int {
        for (i in marks.indices) {
            val boardMarks = marks[i]
            val rowSums = mk.math.sumD2(boardMarks, 1)
            val columnSums = mk.math.sumD2(boardMarks, 0)
            if (rowSums.any { it > 4 } || columnSums.any { it > 4 }) {
                return i
            }
        }
        return -1
    }

    fun computeScore(number: Int, board: D2Array<Int>, marks: D2Array<Int>): Int {
        val score = mk.math.sum(board.mapMultiIndexed { index, it -> if (marks[index[0], index[1]] == 0) it else 0 })
        return score * number
    }

    override fun getExampleAnswer(): Int {
        return 4512
    }
}

class Part4B : Part4A() {
    override fun compute(): Int {
        val marks = MutableList(boards.size) { mk.zeros<Int>(5, 5) }
        val boards = this.boards.toMutableList()

        var lastBoard = mk.zeros<Int>(5, 5)
        var lastMarks = mk.zeros<Int>(5, 5)
        var lastNum = 0
        for (num in numbers) {
            markNumber(num, boards, marks)
            var idx = checkWin(marks)
            if (boards.size == 1 && idx >= 0) {
                return computeScore(num, boards[0], marks[0])
            }
            while (idx >= 0) {
                lastBoard = boards[idx]
                lastMarks = marks[idx]
                lastNum = num
                boards.removeAt(idx)
                marks.removeAt(idx)
                idx = checkWin(marks)
            }
        }
        if (boards.size > 1) {
            return computeScore(lastNum, lastBoard, lastMarks)
        }

        error("No winning score found")
    }

    override fun getExampleAnswer(): Int {
        return 1924
    }
}

fun main() {
    Day(2021, 4, Part4A(), Part4B())
}