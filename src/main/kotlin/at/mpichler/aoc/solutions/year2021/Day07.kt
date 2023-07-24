package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import org.jetbrains.kotlinx.multik.api.d1array
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.D1Array
import org.jetbrains.kotlinx.multik.ndarray.operations.map
import org.jetbrains.kotlinx.multik.ndarray.operations.max
import org.jetbrains.kotlinx.multik.ndarray.operations.min
import org.jetbrains.kotlinx.multik.ndarray.operations.sum
import kotlin.math.absoluteValue

open class Part7A : PartSolution() {
    lateinit var crabs: D1Array<Int>
    lateinit var cost: (Int) -> Int

    override fun parseInput(text: String) {
        crabs = mk.ndarray(text.split(',').map { it.toInt() })
    }

    override fun config() {
        cost = { pos -> crabs.map { (pos - it).absoluteValue }.sum() }
    }

    override fun compute(): Int {
        val costs = allCosts()
        return costs.min()!!
    }

    private fun allCosts(): D1Array<Int> {
        val maxPos = crabs.max()!!
        return mk.d1array<Int>(maxPos + 1) { cost(it) }
    }

    override fun getExampleAnswer(): Int {
        return 37
    }
}

class Part7B : Part7A() {
    override fun config() {
        cost = { pos -> crabs.map { (pos - it).absoluteValue }.map { it * (it + 1) / 2 }.sum() }
    }

    override fun getExampleAnswer(): Int {
        return 168
    }
}

fun main() {
    Day(2021, 7, Part7A(), Part7B())
}