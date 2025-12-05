package at.mpichler.aoc.solutions.year2025

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import at.mpichler.aoc.lib.Vector2i
import at.mpichler.aoc.lib.neighbors
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.set
import org.jetbrains.kotlinx.multik.ndarray.operations.mapMultiIndexed
import org.jetbrains.kotlinx.multik.ndarray.operations.sum

open class Part4A : PartSolution() {
    lateinit var grid: D2Array<Int>

    override fun parseInput(text: String) {
        val lines = text.trim().split("\n")
        val values = mk.zeros<Int>(lines.size, lines.first().length)
        for ((y, line) in lines.withIndex()) {
            for ((x, char) in line.withIndex()) {
                if (char == '@') {
                    values[y, x] = 1
                }
            }
        }

        grid = values
    }

    override fun getExampleAnswer() = 13

    override fun compute(): Int = getAvailableRolls()

    protected fun getAvailableRolls(remove: Boolean = false): Int =
        grid
            .mapMultiIndexed { pos, v ->
                if (v == 0) return@mapMultiIndexed 0
                val neighbors = grid.neighbors(Vector2i(pos[1], pos[0]), diagonals = true).filter { it == 1 }.sum()
                if (remove && neighbors < 4) grid[pos] = 0
                if (neighbors < 4) 1 else 0
            }.sum()
}

class Part4B : Part4A() {
    override fun getExampleAnswer() = 43

    override fun compute(): Int {
        var oldGrid = grid.copy()
        var count = 0
        while (true) {
            count += getAvailableRolls(true)
            if (oldGrid == grid) {
                return count
            }
            oldGrid = grid.copy()
        }
    }
}

fun main() {
    Day(2025, 4, Part4A(), Part4B())
}
