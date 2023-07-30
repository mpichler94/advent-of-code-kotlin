package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.*
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray

import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get

open class Part15A : PartSolution() {
    lateinit var cave: D2Array<Int>
    lateinit var end: Vector2i
    lateinit var limits: Pair<Vector2i, Vector2i>
    private val moves = moves()

    override fun parseInput(text: String) {
        cave = mk.ndarray(text.split("\n").map { it.map { c -> c.digitToInt() } })
        limits = Vector2i() to Vector2i(cave.shape[0] - 1, cave.shape[1] - 1)
        end = limits.second
    }

    open fun getRisk(pos: Vector2i) = cave[pos]

    override fun compute(): Int {
        val traversal = AStar(this::nextEdges, this::heuristic)
        traversal.startFrom(Vector2i()).goTo(end)
        return traversal.distance
    }

    private fun nextEdges(pos: Vector2i, traversal: AStar<Vector2i>): Sequence<Pair<Vector2i, Int>> {
        return sequence {
            for (neighbor in pos.neighbors(moves, limits)) {
                yield(neighbor to getRisk(neighbor))
            }
        }
    }

    private fun heuristic(pos: Vector2i) = (end - pos).norm(Order.L1)

    override fun getExampleAnswer() = 40
}

class Part15B : Part15A() {
    override fun config() {
        limits = Vector2i() to Vector2i(cave.shape[0] * 5 - 1, cave.shape[1] * 5 - 1)
        end = limits.second
    }

    override fun getRisk(pos: Vector2i): Int {
        val blockX = pos.x / cave.shape[0]
        val x = pos.x % cave.shape[0]
        val blockY = pos.y / cave.shape[1]
        val y = pos.y % cave.shape[1]
        return (cave[x, y] + blockX + blockY - 1) % 9 + 1
    }

    override fun getExampleAnswer() = 315
}

fun main() {
    Day(2021, 15, Part15A(), Part15B())
}