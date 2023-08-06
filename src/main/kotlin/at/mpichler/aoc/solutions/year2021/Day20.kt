package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part20A : PartSolution() {
    private lateinit var lut: List<Int>
    private lateinit var image: List<List<Int>>
    open val numRounds = 2

    override fun parseInput(text: String) {
        val parts = text.split("\n\n")

        lut = parts[0].trim().filter { it != '\n' }.map { if (it == '#') 1 else 0 }
        image = parts[1].split("\n").map { line -> line.trim().map { if (it == '#') 1 else 0 } }
    }

    override fun compute(): Int {
        var background = 0
        repeat(numRounds) {
            image = enhance(background)
            background = lut[if (background == 0) 0 else 1]
        }

        return image.sumOf { it.sum() }
    }

    private fun enhance(background: Int): List<List<Int>> {
        val width = image.first().size
        val height = image.size
        val newImage = List(height + 2) { MutableList(width + 2) { 0 } }
        for (y in -1..height) {
            for (x in -1..width) {
                val w = getWindow(x, y, background)
                val v = getEnhancedValue(w)
                newImage[y + 1][x + 1] = v
            }
        }

        return newImage
    }

    private fun getWindow(cX: Int, cY: Int, background: Int): List<List<Int>> {
        val window = List(3) { MutableList(3) { background} }

        for (y in cY - 1..cY+1) {
            for (x in cX - 1 .. cX + 1) {
                if (y < 0 || y >= image.size || x < 0 || x >= image.first().size) {
                    continue
                }
                window[y - cY + 1][x - cX + 1] = image[y][x]
            }
        }
        return window
    }

    private fun getEnhancedValue(window: List<List<Int>>): Int {
        val flattened = window.flatten()
        var index = 0
        for (i in flattened.indices) {
            index += flattened[i] shl (8 - i)
        }
        return  lut[index]
    }

    override fun getExampleAnswer(): Int {
        return 35
    }
}

class Part20B : Part20A() {
    override val numRounds = 50

    override fun getExampleAnswer(): Int {
        return 3351
    }
}

fun main() {
    Day(2021, 20, Part20A(), Part20B())
}