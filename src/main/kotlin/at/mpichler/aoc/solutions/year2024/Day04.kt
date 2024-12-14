package at.mpichler.aoc.solutions.year2024

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import at.mpichler.aoc.lib.Vector2i
import at.mpichler.aoc.lib.moves

open class Part4A : PartSolution() {
    internal lateinit var puzzle: List<String>
    private val word = listOf('X', 'M', 'A', 'S')

    override fun parseInput(text: String) {
        puzzle = text.lines()
    }

    override fun getExampleInput(): String? =
        """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
        """.trimIndent()

    override fun getExampleAnswer() = 18

    override fun compute(): Int =
        puzzle.indices.sumOf { y ->
            puzzle[y]
                .indices
                .filter { x -> puzzle[y][x] == 'X' }
                .sumOf { x ->
                    moves(diagonals = true).count { dir -> matches(Vector2i(x, y), dir) }
                }
        }

    private fun matches(start: Vector2i, dir: Vector2i): Boolean {
        var pos = start
        val wordIterator = word.iterator()
        wordIterator.next()
        repeat(3) {
            val next = pos + dir
            if (next.x < 0 || next.y < 0 || next.y >= puzzle.size || next.x >= puzzle[next.y].length) {
                return false
            }
            if (puzzle[next.y][next.x] != wordIterator.next()) {
                return false
            }
            pos = next
        }
        return true
    }
}

class Part4B : Part4A() {
    private val word = listOf('M', 'A', 'S')

    override fun getExampleAnswer() = 9

    override fun compute(): Int =
        puzzle.indices.sumOf { y ->
            puzzle[y]
                .indices
                .filter { x -> puzzle[y][x] == 'A' }
                .count { x -> matches(Vector2i(x, y)) }
        }

    private fun matches(start: Vector2i): Boolean {
        if (start.x < 1 || start.y < 1 || start.y >= puzzle.size - 1 || start.x >= puzzle[start.y].length - 1) {
            return false
        }

        var mCount = 0
        var sCount = 0
        val mPos = mutableListOf<Vector2i>()

        neighbors(start).forEach {
            val c = puzzle[it.y][it.x]
            if (c == 'M') {
                mCount++
                mPos += it
            } else if (c == 'S') {
                sCount++
            }
        }

        return mCount == 2 && sCount == 2 && (mPos[0].x == mPos[1].x || mPos[0].y == mPos[1].y)
    }

    private fun neighbors(pos: Vector2i): List<Vector2i> =
        buildList {
            add(Vector2i(pos.x - 1, pos.y - 1))
            add(Vector2i(pos.x - 1, pos.y + 1))
            add(Vector2i(pos.x + 1, pos.y - 1))
            add(Vector2i(pos.x + 1, pos.y + 1))
        }
}

fun main() {
    Day(2024, 4, Part4A(), Part4B())
}
