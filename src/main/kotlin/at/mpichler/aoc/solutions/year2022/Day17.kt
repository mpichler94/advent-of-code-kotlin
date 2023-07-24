package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import at.mpichler.aoc.lib.Vector2i
import kotlin.math.max
import kotlin.math.min

open class Part17A : PartSolution() {
    private lateinit var text: String
    private lateinit var jets: List<Int>
    private lateinit var jetIterator: Iterator<IndexedValue<Int>>
    lateinit var rocks: List<List<List<Int>>>
    private lateinit var rockIterator: Iterator<List<List<Int>>>
    lateinit var cave: MutableList<MutableList<Int>>

    override fun parseInput(text: String) {
        this.text = text.trim().trim('\n')
        this.jets = this.text.map { if (it == '<') -1 else 1 }
        this.jetIterator = CyclicIterator(jets.withIndex().toList())
    }

    override fun config() {
        rocks = listOf(
            // -
            listOf(listOf(1), listOf(1), listOf(1), listOf(1)),

            // +
            listOf(listOf(0, 1, 0), listOf(1, 1, 1), listOf(0, 1, 0)),

            // J
            listOf(listOf(0, 0, 1), listOf(0, 0, 1), listOf(1, 1, 1)),

            // I
            listOf(listOf(1, 1, 1, 1)),

            // #
            listOf(listOf(1, 1), listOf(1, 1)),
        )
        rockIterator = CyclicIterator(rocks)

        cave = mutableListOf(mutableListOf(1, 1, 1, 1, 1, 1, 1))
    }

    override fun compute(): Any {
        repeat(2022) { doRock() }
        return cave.size - 1
    }

    fun doRock(): Int {
        val rock = rockIterator.next()
        var rockPos = Vector2i(2, cave.size + 2 + rock[0].size)

        while (true) {
            val (idx, jet) = jetIterator.next()
            rockPos = moveH(jet, rock, rockPos)

            val newPos = rockPos - Vector2i(0, 1)
            if (!checkCollision(rock, newPos)) {
                rockPos = newPos
                continue
            }

            updateCave(rock, rockPos)
            return idx
        }
    }

    private fun moveH(jet: Int, rock: List<List<Int>>, rockPos: Vector2i): Vector2i {
        val newX = max(0, min(rockPos.x + jet, 7 - rock.size))
        val newPos = Vector2i(newX, rockPos.y)
        if (!checkCollision(rock, newPos)) {
            return newPos
        }
        return rockPos
    }

    private fun checkCollision(rock: List<List<Int>>, rockPos: Vector2i): Boolean {
        for (x in rock.indices) {
            for (y in 0..<rock[0].size) {
                if (rock[x][y] == 0) {
                    continue
                }

                val yPos = rockPos.y - y
                if (yPos < cave.size && cave[yPos][rockPos.x + x] == 1) {
                    return true
                }
            }
        }
        return false
    }

    private fun updateCave(rock: List<List<Int>>, rockPos: Vector2i) {
        for (y in rock[0].size - 1 downTo 0) {
            for (x in rock.indices) {
                if (rock[x][y] == 0) {
                    continue
                }

                val yPos = rockPos.y - y
                if (yPos >= cave.size) {
                    cave.add(mutableListOf(0, 0, 0, 0, 0, 0, 0))
                }
                cave[yPos][rockPos.x + x] = 1
            }
        }
    }

    override fun getExampleAnswer(): Any {
        return 3068
    }

    override fun getExampleInput(): String? {
        return ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
    }

    class CyclicIterator<T>(private val list: List<T>) : Iterator<T> {
        private var idx = 0

        override fun hasNext(): Boolean {
            return true
        }

        override fun next(): T {
            val next = list[idx]
            idx = (idx + 1) % list.size
            return next
        }
    }
}

class Part17B : Part17A() {
    private val totalRounds = 1_000_000_000_000L

    override fun compute(): Long {
        val (count, cycleLength, cycleHeight) = findCycle()

        val cycleFactor = (totalRounds - count) / cycleLength
        val cycleRounds = cycleFactor * cycleLength
        val roundsTodo = totalRounds - count - cycleRounds

        repeat(roundsTodo.toInt()) { doRock() }

        return cave.size + cycleFactor * cycleHeight - 1L
    }

    private fun findCycle(): CycleSpec {
        var cycleHeight = 0L
        var jetIndices = mutableSetOf<Int>()
        var cyclesCompleted = 0
        var cycleLength = 0L

        var count = 1L
        while (count <= totalRounds) {
            val jetIdx = doRock()
            if (count % rocks.size.toLong() == 0L) {
                if (jetIdx in jetIndices) {
                    if (cyclesCompleted == 0) {
                        cycleLength = count
                        cycleHeight = cave.size.toLong()
                        jetIndices = mutableSetOf(jetIdx)
                        cyclesCompleted += 1
                    } else if (cyclesCompleted == 1) {
                        cycleHeight = cave.size - cycleHeight
                        cycleLength = count - cycleLength
                        break
                    }
                } else if (cyclesCompleted == 0) {
                    jetIndices.add(jetIdx)
                }
            }
            count += 1
        }
        return CycleSpec(count, cycleLength, cycleHeight)
    }

    override fun getExampleAnswer(): Long {
        return 1_514_285_714_288
    }

    data class CycleSpec(val count: Long, val cycleLength: Long, val cycleHeight: Long)
}

fun main() {
    Day(2022, 17, Part17A(), Part17B())
}