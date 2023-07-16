package at.mpichler.aoc.lib

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

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
}

data class Vector3i(val x: Int, val y: Int, val z: Int) : VectorI(listOf(x, y, z)) {
    operator fun plus(other: Vector3i) = Vector3i(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vector3i) = Vector3i(x - other.x, y - other.y, z - other.z)

    operator fun plus(other: Int) = Vector3i(x + other, y + other, z + other)
    operator fun minus(other: Int) = Vector3i(x - other, y - other, z - other)
}

data class Vector2i_(val x: Int, val y: Int) {
    constructor() : this(0, 0)

    fun norm(ord: Order): Int {
        return when (ord) {
            Order.L1 -> x.absoluteValue + y.absoluteValue
            Order.L2 -> sqrt((x * x + y * y).toDouble()).toInt()
            Order.Linf -> max(x.absoluteValue, y.absoluteValue)
        }
    }

    fun distanceTo(other: Vector2i): Int {
        return (x - other.x).absoluteValue + (y - other.y).absoluteValue
    }

    operator fun plus(other: Vector2i) = Vector2i(x + other.x, y + other.y)
    operator fun minus(other: Vector2i) = Vector2i(x - other.x, y - other.y)
}

enum class Order { L1, L2, Linf, }

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
