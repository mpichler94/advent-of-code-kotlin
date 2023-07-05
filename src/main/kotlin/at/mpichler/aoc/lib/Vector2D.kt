package at.mpichler.aoc.lib

import at.mpichler.aoc.solutions.year2022.Part9A
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.sqrt

data class Vector2i(val x: Int, val y: Int) {
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