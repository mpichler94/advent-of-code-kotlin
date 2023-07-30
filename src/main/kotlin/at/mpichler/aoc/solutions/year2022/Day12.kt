package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.*
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.set


open class Part12A : PartSolution() {
    private lateinit var grid: D2Array<Int>
    internal open val startChar = 'S'
    private val endChar = 'E'

    override fun parseInput(text: String) {
        grid = createGrid(text.trim().split("\n"))
    }

    override fun compute(): Int {
        val starts = mutableListOf<Vector2i>()
        var end = Vector2i(0, 0)
        for (p in grid.multiIndices) {
            val pos = Vector2i(p[1], p[0])
            val char = grid[pos]
            if (char == startChar.code) {
                starts.add(pos)
            } else if (char == endChar.code) {
                end = pos
            }
        }

        val traversal = BreadthFirst(this::nextEdges).startFrom(starts).goTo(end)
        return traversal.depth - 1
    }

    private fun nextEdges(pos: Vector2i, traversal: BreadthFirst<*>): List<Vector2i> {
        return grid.neighborPositions(pos).filter { height(grid[it]) <= height(grid[pos]) + 1 }.toList()
    }

    private fun height(code: Int): Int {
        if (code == 'S'.code) {
            return 'a'.code
        } else if (code == 'E'.code) {
            return 'z'.code
        }
        return code
    }

    override fun getExampleAnswer(): Int {
        return 31
    }

    private fun createGrid(lines: List<String>): D2Array<Int> {
        val values = mk.zeros<Int>(lines.size, lines.first().length)
        for ((y, line) in lines.withIndex()) {
            for ((x, char) in line.withIndex()) {
                values[y, x] = char.code
            }
        }

        return values
    }
}

class Part12B : Part12A() {
    override val startChar: Char = 'a'

    override fun getExampleAnswer(): Int {
        return 29
    }
}


fun main() {
    Day(2022, 12, Part12A(), Part12B())
}
