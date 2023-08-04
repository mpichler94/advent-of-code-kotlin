package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import at.mpichler.aoc.lib.Vector2i
import kotlin.math.ceil
import kotlin.math.sign
import kotlin.math.sqrt

open class Part17A : PartSolution() {
    lateinit var targetArea: Pair<Vector2i, Vector2i>

    override fun parseInput(text: String) {
        val pattern = Regex("""target area: x=(-?\d+)\.\.(-?\d+), y=(-?\d+)\.\.(-?\d+)""")
        val match = pattern.find(text)!!
        val start = Vector2i(match.groupValues[1].toInt(), match.groupValues[3].toInt())
        val end = Vector2i(match.groupValues[2].toInt(), match.groupValues[4].toInt())
        targetArea = start to end
    }

    override fun compute(): Int {
        val vY = -targetArea.first.y - 1
        return vY * (vY + 1) / 2
    }

    override fun getExampleAnswer(): Int {
        return 45
    }
}

class Part17B : Part17A() {
    override fun compute(): Int {
        val (min, max) = getPossibleVelocities()
        val velocities = product(min.x..max.x, min.y..max.y)
        return velocities.count { checkHit(it) }
    }

    private fun getPossibleVelocities(): Pair<Vector2i, Vector2i> {
        val minX = ceil((-1 + sqrt(1 + 8.0 * targetArea.first.x)) / 2)
        val maxX = targetArea.second.x
        val minY = targetArea.first.y
        val maxY = -targetArea.first.y - 1
        return Vector2i(minX.toInt(), minY) to Vector2i(maxX, maxY)
    }

    private fun checkHit(velocity: Vector2i): Boolean {
        var x = 0
        var y = 0
        var vX = velocity.x
        var vY = velocity.y
        while (true) {
            x += vX
            y += vY
            vX -= vX.sign
            vY -= 1

            if (x > targetArea.second.x || y < targetArea.first.y) {
                return false
            }
            if (x >= targetArea.first.x && y <= targetArea.second.y) {
                return true
            }
        }
    }

    private fun product(xValues: IntRange, yValues: IntRange): Iterable<Vector2i> {
        return buildList {
            for (x in xValues) {
                for (y in yValues) {
                    add(Vector2i(x, y))
                }
            }
        }
    }

    override fun getExampleAnswer(): Int {
        return 112
    }
}

fun main() {
    Day(2021, 17, Part17A(), Part17B())
}