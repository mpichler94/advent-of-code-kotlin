package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.D1Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.data.set
import org.jetbrains.kotlinx.multik.ndarray.operations.first

open class Part6A : PartSolution() {
    private lateinit var fish: D1Array<Long>
    var numDays = 0

    override fun parseInput(text: String) {
        fish = mk.zeros<Long>(9)
        for (aFish in text.split(',')) {
            val days = aFish.toInt()
            fish[days] += 1L
        }
    }

    override fun config() {
        numDays = 80
    }

    override fun compute(): Long {
        repeat(numDays) {
            simulateDay()
        }

        return mk.math.sum(fish)
    }

    private fun simulateDay() {
        val newFish = fish.first()
        fish[0] = 0
        for (i in 1..<fish.size) {
            fish[i - 1] = fish[i]
            fish[i] = 0
        }

        fish[8] += newFish
        fish[6] += newFish
    }

    override fun getExampleAnswer(): Long {
        return 5934
    }
}

class Part6B : Part6A() {
    override fun config() {
        numDays = 256
    }

    override fun getExampleAnswer(): Long {
        return 26_984_457_539
    }
}

fun main() {
    Day(2021, 6, Part6A(), Part6B())
}