package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.*
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.set
import kotlin.math.max
import kotlin.math.min

open class Part14A : PartSolution() {
    open var voidY: Int = 0
    lateinit var grid: D2Array<Int>

    override fun parseInput(text: String) {
        voidY = 0
        grid = mk.zeros(500, 1000)

        text.trim().split("\n").forEach { parseWall(it) }
    }

    private fun parseWall(line: String) {
        val points = getPoints(line)
        var prev = points.first()

        for (pos in points.listIterator(1)) {
            voidY = max(voidY, pos.y + 1)
            if (pos.y == prev.y) {
                val start = prev.x
                val end = pos.x
                for (x in min(start, end)..max(start, end)) {
                    grid[pos.y, x] = 1
                }
            } else if (pos.x == prev.x) {
                val start = prev.y
                val end = pos.y
                for (y in min(start, end)..max(start, end)) {
                    grid[y, pos.x] = 1
                }
            }
            prev = pos
        }
    }

    private fun getPoints(line: String): List<Vector2i> {
        return line.split(" -> ").map {
            val s = it.split(",")
            Vector2i(s[0].toInt(), s[1].toInt())
        }
    }

    override fun compute(): Int {
        var count = 0
        while (true) {
            count += 1
            val reachedVoid = putSand()
            if (reachedVoid) {
                return count - 1
            }
        }
    }

    private fun putSand(): Boolean {
        var s = Vector2i(500, 0)

        while (true) {
            if (s.y >= voidY) {
                grid[s] = 2
                return true
            }

            s = getSandPos(s) ?: break
        }
        if (s == Vector2i(500, 0)) {
            return true
        }

        grid[s] = 2
        return s.y >= voidY
    }

    private fun getSandPos(s: Vector2i): Vector2i? {
        return if (grid[s + Vector2i(0, 1)] == 0) {
            s + Vector2i(0, 1)
        } else if (grid[s + Vector2i(-1, 1)] == 0) {
            s + Vector2i(-1, 1)
        } else if (grid[s + Vector2i(1, 1)] == 0) {
            s + Vector2i(1, 1)
        } else {
            null
        }
    }

    override fun getExampleAnswer(): Int {
        return 24
    }

}

class Part14B : Part14A() {
    override fun config() {
        for (x in 0..<grid.shape[1]) {
            grid[voidY + 1, x] = 1
        }
        voidY = 500
    }

    override fun compute(): Int {
        return super.compute() + 1
    }

    override fun getExampleAnswer(): Int {
        return 93
    }
}

fun main() {
    Day(2022, 14, Part14A(), Part14B())
}