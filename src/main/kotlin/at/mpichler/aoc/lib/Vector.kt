package at.mpichler.aoc.lib

import kotlin.math.*

abstract class VectorI(val data: List<Int>) {
    fun norm(ord: Order): Int {
        return when (ord) {
            Order.L1 -> data.sumOf { it.absoluteValue }
            Order.L2 -> sqrt(data.sumOf { it * it }.toDouble()).toInt()
            Order.Linf -> data.maxOf { it.absoluteValue }
        }
    }

    fun distanceTo(other: VectorI): Int {
        return data.zip(other.data).sumOf { (it.first - it.second).absoluteValue }
    }

}

data class Vector2i(val x: Int, val y: Int) : VectorI(listOf(x, y)) {
    constructor() : this(0, 0)
    operator fun plus(other: Vector2i) = Vector2i(x + other.x, y + other.y)
    operator fun minus(other: Vector2i) = Vector2i(x - other.x, y - other.y)
    operator fun plus(value: Int) = Vector2i(x + value, y + value)

    operator fun times(factor: Int) = Vector2i(x * factor, y * factor)

    fun neighbors(moves: Iterable<Vector2i>): List<Vector2i> {
        return moves.map { it + this }
    }
    fun sign(): Vector2i {
        return Vector2i(x.sign, y.sign)
    }
}

data class Vector3i(val x: Int, val y: Int, val z: Int) : VectorI(listOf(x, y, z)) {
    operator fun plus(other: Vector3i) = Vector3i(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vector3i) = Vector3i(x - other.x, y - other.y, z - other.z)
    operator fun plus(other: Int) = Vector3i(x + other, y + other, z + other)
    operator fun minus(other: Int) = Vector3i(x - other, y - other, z - other)
}

enum class Order { L1, L2, Linf, }

fun min(first: Vector2i, second: Vector2i): Vector2i {
    return Vector2i(
        min(first.x, second.x),
        min(first.y, second.y)
    )
}

fun min(first: Vector3i, second: Vector3i): Vector3i {
    return Vector3i(
        min(first.x, second.x),
        min(first.y, second.y),
        min(first.z, second.z)
    )
}

fun max(first: Vector3i, second: Vector3i): Vector3i {
    return Vector3i(
        max(first.x, second.x),
        max(first.y, second.y),
        max(first.z, second.z)
    )
}

fun max(first: Vector2i, second: Vector2i): Vector2i {
    return Vector2i(
        max(first.x, second.x),
        max(first.y, second.y)
    )
}

fun moves(distance: Int = 1, diagonals: Boolean = false, zeroMove: Boolean = false): List<Vector2i> {
    require(distance >= 1) { "distance must be >= 1" }

    return buildList {
        if (zeroMove) {
            add(Vector2i(0, 0))
        }
        for (d in 1..distance) {
            add(Vector2i(0, -d))
            if (diagonals) {
                add(Vector2i(d, -d))
            }
            add(Vector2i(d, 0))
            if (diagonals) {
                add(Vector2i(d, d))
            }
            add(Vector2i(0, d))
            if (diagonals) {
                add(Vector2i(-d, d))
            }
            add(Vector2i(-d, 0))
            if (diagonals) {
                add(Vector2i(-d, -d))
            }
        }
    }
}

