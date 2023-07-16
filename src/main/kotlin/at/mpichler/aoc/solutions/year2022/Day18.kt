package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.*

open class Part18A : PartSolution() {
    lateinit var cubes: List<Vector3i>

    override fun parseInput(text: String) {
        val lines = text.trim().split("\n")
        val cubes = mutableListOf<Vector3i>()
        for (line in lines) {
            val values = line.split(",").map { it.toInt() }
            val cube = Vector3i(values[0], values[1], values[2])
            cubes.add(cube)
        }
        this.cubes = cubes
    }

    override fun compute(): Int {
        var touching = 0

        for ((cube, cube2) in cubes.combinations()) {
            if (cube == cube2) {
                continue
            }

            if (isTouching(cube, cube2)) {
                touching += 1
            }
        }

        return cubes.size * 6 - touching * 2
    }

    fun isTouching(cube1: Vector3i, cube2: Vector3i): Boolean {
        val norm = (cube1 - cube2).norm(Order.L1)
        return norm == 1
    }

    override fun getExampleAnswer(): Int {
        return 64
    }

    private fun <T> List<T>.combinations(): Sequence<Pair<T, T>> {
        return sequence {
            for (l in indices) {
                for (r in l + 1 until size) {
                    yield(Pair(get(l), get(r)))
                }
            }
        }
    }
}

class Part18B : Part18A() {
    private lateinit var minBound: Vector3i
    private lateinit var maxBound: Vector3i

    override fun config() {
        this.minBound = cubes.fold(Vector3i(1000, 1000, 1000)) { acc, cube -> min(acc, cube - 1) }
        this.maxBound = cubes.fold(Vector3i(-1000, -1000, -1000)) { acc, cube -> max(acc, cube + 1) }
    }

    override fun compute(): Int {
        var touching = 0
        val seen = mutableListOf<Vector3i>()
        val exterior = mutableListOf<Vector3i>()
        val todo = ArrayDeque<Vector3i>()
        todo.addLast(minBound)

        while (todo.isNotEmpty()) {
            val cube = todo.removeFirst()

            if (cube in exterior || cube in cubes
                || cube.x < minBound.x || cube.y < minBound.y || cube.z < minBound.z
                || cube.x > maxBound.x || cube.y > maxBound.y || cube.z > maxBound.z
            ) {
                continue
            }

            exterior.add(cube)

            todo.addLast(Vector3i(cube.x + 1, cube.y, cube.z))
            todo.addLast(Vector3i(cube.x, cube.y + 1, cube.z))
            todo.addLast(Vector3i(cube.x, cube.y, cube.z + 1))
            todo.addLast(Vector3i(cube.x - 1, cube.y, cube.z))
            todo.addLast(Vector3i(cube.x, cube.y - 1, cube.z))
            todo.addLast(Vector3i(cube.x, cube.y, cube.z - 1))
        }

        for (i in cubes.indices) {
            touching += exterior.count { isTouching(cubes[i], it) }
            seen.add(cubes[i])
        }

        return touching
    }

    override fun getExampleAnswer(): Int {
        return 58
    }
}

fun main() {
    Day(2022, 18, Part18A(), Part18B())
}