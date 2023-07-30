package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.*
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.operations.toList

open class Part13A : PartSolution() {
    lateinit var positions: MutableList<Vector2i>
    lateinit var folds: List<Pair<Char, Int>>

    override fun parseInput(text: String) {
        val folds = mutableListOf<Pair<Char, Int>>()
        positions = mutableListOf()

        for (line in text.split("\n")) {
            if (line.startsWith("fold")) {
                val parts = line.split("=")
                folds.add(parts[0].last() to parts[1].toInt())
            } else if (line.isNotEmpty()) {
                val pos = line.split(",")
                positions.add(Vector2i(pos[0].toInt(), pos[1].toInt()))
            }
        }

        this.folds = folds
    }

    override fun config() {
        val fold = folds.first()
        if (fold.first == 'x') {
            foldHorizontal(fold.second)
        } else {
            foldVertical(fold.second)
        }

        discardOverlaps()
    }

    fun foldHorizontal(foldingPos: Int) {
        for (i in positions.indices) {
            val pos = positions[i]
            if (pos.x < foldingPos) {
                continue
            }

            positions[i] = pos.withX(2 * foldingPos - pos.x)
        }
    }

    fun foldVertical(foldingPos: Int) {
        for (i in positions.indices) {
            val pos = positions[i]
            if (pos.y < foldingPos) {
                continue
            }

            positions[i] = pos.withY(2 * foldingPos - pos.y)
        }
    }

    fun discardOverlaps() {
        positions = positions.toSet().toMutableList()
    }

    override fun compute(): Int {
        return positions.size
    }

    override fun getExampleAnswer(): Int {
        return 17
    }

    override fun getExampleInput(): String? {
        return """
            6,10
            0,14
            9,10
            0,3
            10,4
            4,11
            6,0
            6,12
            4,1
            0,13
            10,12
            3,4
            3,0
            8,4
            1,10
            2,14
            8,10
            9,0

            fold along y=7
            fold along x=5
        """.trimIndent()
    }

}

class Part13B : Part13A() {
    override fun compute(): Int {
        for (fold in folds) {
            if (fold.first == 'x') {
                foldHorizontal(fold.second)
            } else {
                foldVertical(fold.second)
            }
            discardOverlaps()
        }

        printPoints()
        return 0
    }

    private fun printPoints() {
        val (width, height) = getDimension()
        val grid = mk.zeros<Int>(height, width)
        for (point in positions) {
            grid[point] = 1
        }

        var output = ""
        for (y in 0..<grid.shape[0]) {
            output += "\n" + grid[y].toList().map { if (it == 0) ' ' else '#' }.joinToString(separator = "")
        }

        println(output)
    }

    private fun getDimension(): Pair<Int, Int> {
        val max = positions.reduce { acc, vector2i -> max(acc, vector2i) }
        return max.x + 1 to max.y + 1
    }

    override fun getExampleAnswer(): Int {
        return 0
    }
}

fun main() {
    Day(2021, 13, Part13A(), Part13B(), false)
}