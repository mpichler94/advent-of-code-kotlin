package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import at.mpichler.aoc.lib.Vector2i
import at.mpichler.aoc.lib.max
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.data.set
import org.jetbrains.kotlinx.multik.ndarray.operations.map

open class Part5A : PartSolution() {
    lateinit var lines: List<Line>

    override fun parseInput(text: String) {
        lines = text.split("\n")
            .map { getPoints(it) }
            .map { Line(it.first, it.second) }
    }

    private fun getPoints(line: String): Pair<Vector2i, Vector2i> {
        val result = Regex("""(\d+),(\d+) -> (\d+),(\d+)""").find(line)
        val (x1, y1, x2, y2) = result!!.destructured
        val p1 = Vector2i(x1.toInt(), y1.toInt())
        val p2 = Vector2i(x2.toInt(), y2.toInt())
        return Pair(p1, p2)
    }

    override fun compute(): Int {
        val (width, height) = getFieldSize()
        val diagram = mk.zeros<Int>(width, height)

        lines.filter { it.isHV }.forEach { it.paint(diagram) }
        return mk.math.sum(diagram.map { if (it > 1) 1 else 0 })
    }

    fun getFieldSize(): Pair<Int, Int> {
        val max = lines.flatMap { listOf(it.from, it.to) }
            .reduce { acc, it -> max(acc, it) }
        return Pair(max.x + 1, max.y + 1)
    }

    override fun getExampleAnswer(): Int {
        return 5
    }

    data class Line(val from: Vector2i, val to: Vector2i) {
        val isHV get() = from.x == to.x || from.y == to.y

        fun paint(diagram: D2Array<Int>) {
            val direction = (to - from).sign()

            var pos = from
            diagram[pos.x, pos.y] += 1
            while (pos != to) {
                pos += direction
                diagram[pos.x, pos.y] += 1
            }
        }
    }
}

class Part5B : Part5A() {
    override fun compute(): Int {
        val (width, height) = getFieldSize()
        val diagram = mk.zeros<Int>(width, height)

        lines.forEach { it.paint(diagram) }
        return mk.math.sum(diagram.map { if (it > 1) 1 else 0 })
    }

    override fun getExampleAnswer(): Int {
        return 12
    }
}

fun main() {
    Day(2021, 5, Part5A(), Part5B())
}