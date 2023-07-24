package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.Order
import at.mpichler.aoc.lib.PartSolution
import at.mpichler.aoc.lib.Vector2i
import java.lang.IllegalStateException
import kotlin.math.absoluteValue

open class Part15A : PartSolution() {
    lateinit var sensors: MutableList<Sensor>

    override fun parseInput(text: String) {
        sensors = mutableListOf()
        val pattern = Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")
        for (line in text.trim().split("\n")) {
            val result = pattern.find(line)
            checkNotNull(result)
            val pos = Vector2i(result.groupValues[1].toInt(), result.groupValues[2].toInt())
            val beacon = Vector2i(result.groupValues[3].toInt(), result.groupValues[4].toInt())
            val distance = (pos - beacon).norm(Order.L1)
            sensors.add(Sensor(pos, distance, beacon))
        }
    }

    override fun compute(): Any {
        val targetRow = if (sensors.size != 14) 2_000_000 else 10
        return coveredTiles(targetRow).size
    }

    private fun coveredTiles(targetRow: Int): Set<Int> {
        val row = mutableSetOf<Int>()
        val beacons = mutableSetOf<Int>()

        for (sensor in sensors) {
            if (sensor.beacon.y == targetRow) {
                beacons.add(sensor.beacon.x)
            }

            val yDistance = (targetRow - sensor.pos.y).absoluteValue
            val xDistance = sensor.distance - yDistance
            if (xDistance < 0) {
                continue
            }

            val xMin = sensor.pos.x - xDistance
            val xMax = sensor.pos.x + xDistance
            for (x in xMin..xMax) {
                row.add(x)
            }
        }

        for (beacon in beacons) {
            row.remove(beacon)
        }

        return row
    }

    override fun getExampleAnswer(): Int {
        return 26
    }

    data class Sensor(val pos: Vector2i, val distance: Int, val beacon: Vector2i)
}

class Part15B : Part15A() {
    override fun compute(): Long {
        val (x, y) = freeTile()
        return x * 4_000_000L + y
    }

    private fun freeTile(): Vector2i {
        for (sensor in sensors) {
            for (pos in getSensorBorder(sensor)) {
                var found = true
                for (sensor2 in sensors) {
                    val distance = (sensor2.pos - pos).norm(Order.L1)
                    if (distance <= sensor2.distance) {
                        found = false
                        break
                    }
                }
                if (found) {
                    return pos
                }
            }
        }

        throw IllegalStateException("Should not be reached")
    }

    /**
     * Get border of sensor area, within which no beacon can be
     */
    private fun getSensorBorder(sensor: Sensor): Sequence<Vector2i> {
        val max = 4_000_000
        val beacons = sensors.map { it.beacon }

        return sequence {
            val topY = sensor.pos.y + sensor.distance - 1
            val bottomY = sensor.pos.y - sensor.distance - 1

            for (i in 0..<sensor.distance) {
                for ((x, y) in listOf(
                    Vector2i(sensor.pos.x + i, topY - i),
                    Vector2i(sensor.pos.x - i, topY - i),
                    Vector2i(sensor.pos.x + i, bottomY + i),
                    Vector2i(sensor.pos.x - i, bottomY + i)
                )) {
                    if (x < 0 || y < 0 || x > max || y > max || Vector2i(x, y) in beacons) {
                        continue
                    }
                    yield(Vector2i(x, y))
                }
            }
        }
    }

    override fun getExampleAnswer(): Int {
        return 56_000_011
    }
}

fun main() {
    Day(2022, 15, Part15A(), Part15B())
}