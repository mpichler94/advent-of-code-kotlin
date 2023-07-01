package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.*
import org.jetbrains.kotlinx.multik.api.d2array
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.zeros
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.data.set


open class Part12A : PartSolution() {
    lateinit var grid: D2Array<Int>

    override fun parseInput(text: String) {
        grid = createGrid(text.trim().split("\n"))
    }

    override fun compute(): Int {
        val bfs = BFS('S', 'E', grid)
        return bfs.calculate()
    }

    override fun getExampleAnswer(): Int {
        return 31
    }

    private fun createGrid(lines: List<String>): D2Array<Int> {
        val values = mk.zeros<Int>(lines.size, lines.first().length)
        for ((y, line) in lines.withIndex()) {
            for ((x, char) in line.withIndex()) {
                values[y, x] = char.code
            }
        }

        return values
    }

    class BFS(private val start: Char, private val end: Char, private val grid: D2Array<Int>) {
        fun calculate(): Int {
            val endPos = getEndPos()

            val depths = mk.d2array(grid.shape[0], grid.shape[1]) { Int.MAX_VALUE }
            for (i in grid.multiIndices) {
                val pos = Vector2i(i[1], i[0])
                val char = grid[pos]
                if (char != start.code) {
                    continue
                }

                depths[pos] = 0
                calculateSingle(pos, endPos, depths)
            }

            return depths[endPos]
        }

        private fun getEndPos(): Vector2i {
            var endPos: Vector2i? = null
            for (i in grid.multiIndices) {
                val value = grid[i[0], i[1]]
                if (value == end.code) {
                    endPos = Vector2i(i[1], i[0])
                    break
                }
            }

            checkNotNull(endPos)
            return endPos
        }

        private fun calculateSingle(start: Vector2i, end: Vector2i, depths: D2Array<Int>): D2Array<Int> {
            val todo = mutableSetOf(start)

            while (todo.isNotEmpty()) {
                val pos = todo.first()
                todo.remove(pos)
                if (pos == end) {
                    return depths
                }

                for (n in grid.neighbors(pos)) {
                    if (height(grid[n]) > height(grid[pos]) + 1) {
                        continue
                    }

                    val old = depths[n]
                    if (old <= depths[pos]) {
                        continue
                    }

                    depths[n] = depths[pos] + 1
                    todo.add(n)
                }
            }

            return depths
        }

        private fun height(code: Int): Int {
            if (code == 'S'.code) {
                return 'a'.code
            } else if (code == 'E'.code) {
                return 'z'.code
            }
            return code
        }
    }
}

class Part12B : Part12A() {
    override fun compute(): Int {
        val bfs = BFS('a', 'E', grid)
        return bfs.calculate()
    }

    override fun getExampleAnswer(): Int {
        return 29
    }
}


fun main() {
    Day(2022, 12, Part12A(), Part12B())
}
