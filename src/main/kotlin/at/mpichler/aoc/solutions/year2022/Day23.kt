package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import at.mpichler.aoc.lib.Vector2i
import at.mpichler.aoc.lib.moves

open class Part23A : PartSolution() {
    private lateinit var elves: MutableMap<Vector2i, Elf>
    var numRounds = 0

    override fun parseInput(text: String) {
        elves = mutableMapOf()
        var i = 0
        for ((y, line) in text.split("\n").withIndex()) {
            for ((x, char) in line.withIndex()) {
                if (char == '#') {
                    val pos = Vector2i(x, y)
                    elves[pos] = Elf(i, pos)
                    i += 1
                }
            }
        }
    }

    override fun config() {
        numRounds = 10
    }

    override fun compute(): Int {
        doRounds()
        return getBounds()
    }

    fun doRounds(): Int {
        var i = 0
        while (true) {
            val anyMoved = doRound()
            i += 1
            if (!anyMoved || i >= numRounds) {
                break
            }
        }

        return i
    }

    private fun doRound(): Boolean {
        val elves = this.elves.values.toList()
        elves.forEach { it.prepareMove(this.elves) }
        return elves.map { it.move(this.elves) }.any { it }
    }

    private fun getBounds(): Int {
        val min = elves.values.map { it.pos }.reduce { acc, elf -> at.mpichler.aoc.lib.min(acc, elf) }
        val max = elves.values.map { it.pos }.reduce { acc, elf -> at.mpichler.aoc.lib.max(acc, elf) }
        val diff = (max - min + 1)

        return diff.x * diff.y - elves.size
    }

    override fun getExampleAnswer(): Int {
        return 110
    }

    data class Elf(val id: Int, var pos: Vector2i) {
        private val moves = mutableListOf(Vector2i(0, -1), Vector2i(0, 1), Vector2i(-1, 0), Vector2i(1, 0))
        private var consideredMove: Vector2i? = null
        private var lastPos = pos

        fun prepareMove(elves: Map<Vector2i, Elf>) {
            lastPos = pos

            val availableMoves = moves.toMutableList()
            val move = moves.removeFirst()
            moves.add(move)
            for (p in moves(diagonals = true)) {
                if (availableMoves.isEmpty()) {
                    break
                }
                if (pos + p in elves) {
                    if (p.y < 0 && Vector2i(0, -1) in availableMoves) {
                        availableMoves.remove(Vector2i(0, -1))
                    }
                    if (p.y > 0 && Vector2i(0, 1) in availableMoves) {
                        availableMoves.remove(Vector2i(0, 1))
                    }
                    if (p.x < 0 && Vector2i(-1, 0) in availableMoves) {
                        availableMoves.remove(Vector2i(-1, 0))
                    }
                    if (p.x > 0 && Vector2i(1, 0) in availableMoves) {
                        availableMoves.remove(Vector2i(1, 0))
                    }
                }
            }

            consideredMove = if (availableMoves.size in 1..<moves.size) {
                availableMoves.removeFirst()
            } else {
                null
            }
        }

        fun move(elves: MutableMap<Vector2i, Elf>): Boolean {
            if (consideredMove == null) {
                return false
            }

            val newPos = pos + consideredMove!!
            if (newPos in elves) {
                elves[newPos]!!.goBack(elves)
                return false
            }

            elves.remove(pos)
            pos = newPos
            elves[newPos] = this

            return true
        }

        private fun goBack(elves: MutableMap<Vector2i, Elf>) {
            elves.remove(pos)
            pos = lastPos
            elves[pos] = this
        }
    }
}

class Part23B : Part23A() {
    override fun config() {
        numRounds = 1_000_000
    }

    override fun compute(): Int {
        return doRounds()
    }

    override fun getExampleAnswer(): Int {
        return 20
    }
}

fun main() {
    Day(2022, 23, Part23A(), Part23B())
}