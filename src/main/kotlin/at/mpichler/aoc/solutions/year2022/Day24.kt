package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.*

open class Part24A : PartSolution() {
    private var width = 0
    var height = 0
    var start = 0
    var goal = 0
    private lateinit var blizzards: List<Blizzard>
    private lateinit var blizzardIterator: BlizzardIterator
    private lateinit var blizzardCache: MutableMap<Int, Set<Vector2i>>

    override fun parseInput(text: String) {
        val lines = text.trimEnd().split("\n")
        width = lines.first().length - 2
        height = lines.size - 2

        start = findDoor(lines.first())
        goal = findDoor(lines.last())

        val blizzards = mutableListOf<Blizzard>()
        for (y in 1..<lines.size - 1) {
            for ((x, c) in lines[y].withIndex()) {
                when (c) {
                    '^' -> blizzards.add(Blizzard(Vector2i(0, -1), Vector2i(x - 1, y - 1)))
                    'v' -> blizzards.add(Blizzard(Vector2i(0, 1), Vector2i(x - 1, y - 1)))
                    '<' -> blizzards.add(Blizzard(Vector2i(-1, 0), Vector2i(x - 1, y - 1)))
                    '>' -> blizzards.add(Blizzard(Vector2i(1, 0), Vector2i(x - 1, y - 1)))
                }
            }
        }

        this.blizzards = blizzards
    }

    private fun findDoor(line: String): Int {
        for ((i, c) in line.withIndex()) {
            if (c == '.') {
                return i - 1
            }
        }
        error("No door in line")
    }

    override fun config() {
        blizzardIterator = BlizzardIterator(blizzards, width, height)
        blizzardCache = mutableMapOf()
    }

    @Suppress("kotlin:S6611")   // value is always present in map
    private fun blizzards(time: Int): Set<Vector2i> {
        if (time in blizzardCache) {
            return blizzardCache[time]!!
        }

        blizzardCache[time] = blizzardIterator.next()
        return blizzardCache[time]!!
    }

    fun nextEdges(state: State, traversal: Traversal<State>): Sequence<Pair<State, Int>> {
        return sequence {
            val nextTime = state.time + 1
            val blizzardLocations = blizzards(nextTime)

            for ((nextX, nextY) in state.pos.neighbors(moves(zeroMove = true))) {
                val nextPos = Vector2i(nextX, nextY)
                if (nextX in 0..<width && nextY in 0..<height && nextPos !in blizzardLocations) {
                    yield(Pair(State(nextPos, nextTime), 1))
                }
                if (nextX == start && nextY == -1) {
                    yield(Pair(State(nextPos, nextTime), 1))
                }
                if (nextX == goal && nextY == height) {
                    yield(Pair(State(nextPos, nextTime), 1))
                }
            }
        }
    }

    fun getHeuristic(end: Vector2i): (node: State) -> Int {
        return { current -> (current.pos - end).norm(Order.L1) }
    }

    override fun compute(): Int {
        val traversal = AStar(this::nextEdges, getHeuristic(Vector2i(goal, height)))
        traversal.startFrom(State(Vector2i(start, -1), 0))
        for (state in traversal) {
            if (state.y == height && state.x == goal) {
                return traversal.depth
            }
        }

        error("No path found")
    }

    override fun getExampleInput(): String? {
        return """
            #.######
            #>>.<^<#
            #.<..<<#
            #>v.><>#
            #<^v^^>#
            ######.#
        """.trimIndent()
    }

    override fun getExampleAnswer(): Int {
        return 18
    }

    data class Blizzard(val dir: Vector2i, val pos: Vector2i)

    class BlizzardIterator(private val blizzards: List<Blizzard>, private val width: Int, private val height: Int) :
        Iterator<Set<Vector2i>> {
        val it = generator().iterator()

        override fun hasNext(): Boolean {
            return true
        }

        private fun generator(): Sequence<Set<Vector2i>> {
            return sequence {
                var count = 1
                while (true) {
                    val blizzardLocations = mutableSetOf<Vector2i>()
                    for (blizzard in blizzards) {
                        val (dir, pos) = blizzard
                        val p = pos + dir * count
                        val x = p.x.mod(width)
                        val y = p.y.mod(height)
                        blizzardLocations.add(Vector2i(x, y))
                    }
                    count += 1
                    yield(blizzardLocations)
                }
            }
        }

        override fun next(): Set<Vector2i> {
            return it.next()
        }
    }

    data class State(val pos: Vector2i, val time: Int) {
        val x get() = pos.x
        val y get() = pos.y
    }
}

class Part24B : Part24A() {
    override fun compute(): Int {
        val paths = listOf(
            Pair(Vector2i(start, -1), Vector2i(goal, height)),
            Pair(Vector2i(goal, height), Vector2i(start, -1)),
            Pair(Vector2i(start, -1), Vector2i(goal, height))
        )
        var totalTime = 0
        for ((start, end) in paths) {
            val traversal = AStar(this::nextEdges, getHeuristic(end))
            traversal.startFrom(State(start, totalTime))
            for (state in traversal) {
                if (state.pos == end) {
                    totalTime += traversal.depth
                    break
                }
            }
        }
        return totalTime
    }

    override fun getExampleAnswer(): Int {
        return 54
    }
}

fun main() {
    Day(2022, 24, Part24A(), Part24B())
}