package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.*
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array

open class Part9A : PartSolution() {
    lateinit var heights: D2Array<Int>
    lateinit var lowPoints: List<Vector2i>

    override fun parseInput(text: String) {
        val lines = text.split("\n")
        val map = lines.map { it.map { char -> char.digitToInt() } }
        heights = mk.ndarray(map)
    }

    override fun config() {
        lowPoints = findLowPoints()
    }

    private fun findLowPoints(): List<Vector2i> {
        val lowPoints = mutableListOf<Vector2i>()
        for (x in 0..<heights.shape[1]) {
            for (y in 0..<heights.shape[0]) {
                val pos = Vector2i(x, y)
                val neighborHeights = heights.neighbors(pos).map { heights[it] }
                if (neighborHeights.all { heights[pos] < it }) {
                    lowPoints.add(pos)
                }
            }
        }

        return lowPoints
    }

    override fun compute(): Int {
        return lowPoints.sumOf { heights[it] + 1 }
    }

    override fun getExampleAnswer(): Int {
        return 15
    }
}

class Part9B : Part9A() {
    override fun compute(): Int {
        val basinSizes = lowPoints.map { getBasinSize(it) }.sorted()
        return basinSizes.takeLast(3).reduce { acc, int -> acc * int }
    }

    private fun getBasinSize(pos: Vector2i): Int {
        val traversal = BreadthFirst(this::nextEdges)
        return traversal.startFrom(pos).toSet().size
    }

    private fun nextEdges(pos: Vector2i, traversal: BreadthFirst<Vector2i>): Iterable<Vector2i> {
        return heights.neighbors(pos)
            .filter { heights[it] < 9 }
            .filter { heights[pos] < heights[it] }
            .toList()
    }

    override fun getExampleAnswer(): Int {
        return 1134
    }
}

fun main() {
    Day(2021, 9, Part9A(), Part9B())
}