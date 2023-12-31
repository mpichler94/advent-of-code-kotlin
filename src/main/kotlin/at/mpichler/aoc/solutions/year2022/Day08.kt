package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.*
import org.jetbrains.kotlinx.multik.ndarray.operations.any
import kotlin.math.max

open class Part8A : PartSolution() {
    internal lateinit var wood: Wood

    override fun parseInput(text: String) {
        val lines = text.trim().split("\n")
        val trees = mk.zeros<Int>(lines.size, lines[0].length)
        lines.forEachIndexed { y, line -> line.forEachIndexed { x, c -> trees[y, x] = c.digitToInt() } }
        wood = Wood(trees)
    }

    override fun compute(): Int {
        var visible = 0

        for (y in 0..<wood.getHeight()) {
            for (x in 0..<wood.getWidth()) {
                visible += if (wood.isVisible(x, y)) 1 else 0
            }
        }

        return visible
    }

    override fun getExampleAnswer(): Int {
        return 21
    }

    data class Wood(val trees: D2Array<Int>) {
        fun getWidth(): Int {
            return trees.shape[1]
        }

        fun getHeight(): Int {
            return trees.shape[0]
        }

        private fun getRow(y: Int, start: Int = 0, end: Int = getWidth()): MultiArray<Int, D1> {
            return trees[y, start..<end]
        }

        private fun getCol(x: Int, start: Int = 0, end: Int = getHeight()): MultiArray<Int, D1> {
            return trees[start..<end, x]

        }

        fun isVisible(x: Int, y: Int): Boolean {
            if (x == 0 || x == getWidth() || y == 0 || y == getWidth()) {
                return true
            }

            val tree = trees[y][x]
            var larger = 4
            if (getRow(y, x + 1).any { it >= tree }) {
                larger -= 1
            }
            if (getRow(y, end = x).any { it >= tree }) {
                larger -= 1
            }
            if (getCol(x, y + 1).any { it >= tree }) {
                larger -= 1
            }
            if (getCol(x, end = y).any { it >= tree }) {
                larger -= 1
            }

            return larger > 0
        }

        fun getDistance(x: Int, y: Int): Iterable<Int> {
            val distance = mutableListOf(getWidth() - x - 1, x, getHeight() - y - 1, y)
            val tree = trees[y][x]
            for (i in x + 1..<getWidth()) {
                if (trees[y][i] >= tree) {
                    distance[0] = i - x
                    break
                }
            }
            for (i in x - 1 downTo 0) {
                if (trees[y][i] >= tree) {
                    distance[1] = x - i
                    break
                }
            }
            for (i in y + 1..<getHeight()) {
                if (trees[i][x] >= tree) {
                    distance[2] = i - y
                    break
                }
            }
            for (i in y - 1 downTo 0) {
                if (trees[i][x] >= tree) {
                    distance[3] = y - i
                    break
                }
            }
            return distance
        }
    }
}

class Part8B : Part8A() {
    override fun compute(): Int {
        var scenicScore = 0
        for (y in 1..<wood.getHeight() - 1) {
            for (x in 1..<wood.getWidth() - 1) {
                val distance = wood.getDistance(x, y)
                val score = distance.fold(1, Int::times)
                scenicScore = max(scenicScore, score)
            }
        }

        return scenicScore
    }

    override fun getExampleAnswer(): Int {
        return 8
    }
}

fun main() {
    Day(2022, 8, Part8A(), Part8B())
}