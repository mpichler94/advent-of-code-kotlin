package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import at.mpichler.aoc.lib.Vector2i
import org.jetbrains.kotlinx.multik.api.d2array
import org.jetbrains.kotlinx.multik.api.d2arrayIndices
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.data.set
import org.jetbrains.kotlinx.multik.ndarray.operations.map
import org.jetbrains.kotlinx.multik.ndarray.operations.plusAssign
import kotlin.math.max
import kotlin.math.min

open class Part11A : PartSolution() {
    private lateinit var energyLevels: D2Array<Int>

    override fun parseInput(text: String) {
        val lines = text.split("\n")
        energyLevels = mk.d2arrayIndices(10, 10) { x, y -> lines[y][x].digitToInt() }
    }

    private fun product(list: Iterable<Int>): Iterable<Pair<Int, Int>> {
        return buildList {
            for (first in list) {
                for (second in list) {
                    add(Pair(first, second))
                }
            }
        }
    }

    override fun compute(): Int {
        var flashes = 0
        repeat(100) {
            val stepFlashes = step()
            flashes += stepFlashes
        }

        return flashes
    }

    fun step(): Int {
        energyLevels += 1
        processFlashes()
        val stepFlashes = energyLevels.data.count { it > 9 }
        energyLevels = energyLevels.map { if (it > 9) 0 else it }

        return stepFlashes
    }

    private fun processFlashes() {
        var flashed = true
        val flashedPositions = mutableListOf<Vector2i>()
        while (flashed) {
            flashed = false
            for ((x, y) in product(0..9)) {
                if (energyLevels[x, y] > 9 && Vector2i(x, y) !in flashedPositions) {
                    flashed = true
                    flash(x, y)
                    flashedPositions.add(Vector2i(x, y))
                }
            }
        }
    }

    private fun flash(centerX: Int, centerY: Int) {
        for (y in max(0, centerY - 1)..min(centerY + 1, 9)) {
            for (x in max(0, centerX - 1)..min(centerX + 1, 9)) {
                if (x != centerX || y != centerY) {
                    energyLevels[x, y] += 1
                }
            }
        }
    }

    override fun getExampleAnswer(): Int {
        return 1656
    }
}

class Part11B : Part11A() {
    override fun compute(): Int {
        var flashes = 0
        var steps = 0
        while (flashes < 100) {
            flashes = step()
            steps += 1
        }

        return steps
    }

    override fun getExampleAnswer(): Int {
        return 195
    }
}

fun main() {
    Day(2021, 11, Part11A(), Part11B())
}