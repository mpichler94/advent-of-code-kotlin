package at.mpichler.aoc.lib

import at.mpichler.aoc.solutions.year2022.Part9A

class Vector2i(val x: Int, val y: Int) {
    constructor() : this(0, 0)

    operator fun plus(other: Vector2i) = Vector2i(x + other.x, y + other.y)
    operator fun minus(other: Vector2i) = Vector2i(x - other.x, y - other.y)
}
