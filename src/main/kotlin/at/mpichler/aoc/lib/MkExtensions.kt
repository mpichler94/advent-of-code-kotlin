package at.mpichler.aoc.lib

import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.data.set


operator fun D2Array<Int>.get(pos: Vector2i): Int {
    return this[pos.y, pos.x]
}

operator fun D2Array<Int>.set(pos: Vector2i, value: Int) {
    this[pos.y, pos.x] = value
}

fun D2Array<Int>.neighbors(
    pos: Vector2i,
    distance: Int = 1,
    diagonals: Boolean = false,
    zeroMove: Boolean = false
): Sequence<Vector2i> {
    val width = this.shape[1]
    val height = this.shape[0]
    require(distance >= 1) { "distance must be >= 1" }

    return sequence {
        if (zeroMove) {
            yield(pos)
        }
        for (d in 1..distance) {
            if (pos.y >= d) {
                yield(Vector2i(pos.x, pos.y - d))
            }
            if (diagonals && pos.y >= d && pos.x < width - d) {
                yield(Vector2i(pos.x + d, pos.y - d))
            }
            if (pos.x < width - d) {
                yield(Vector2i(pos.x + d, pos.y))
            }
            if (diagonals && pos.x < width - d && pos.y < height - d) {
                yield(Vector2i(pos.x + d, pos.y + d))
            }
            if (pos.y < height - d) {
                yield(Vector2i(pos.x, pos.y + d))
            }
            if (diagonals && pos.x >= d && pos.y < height - d) {
                yield(Vector2i(pos.x - d, pos.y + d))
            }
            if (pos.x >= d) {
                yield(Vector2i(pos.x - d, pos.y))
            }
            if (diagonals && pos.x >= d && pos.y >= d) {
                yield(Vector2i(pos.x - d, pos.y - d))
            }
        }
    }
}
