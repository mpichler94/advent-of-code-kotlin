package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.max
import kotlin.math.min

open class Part20A : PartSolution() {
    lateinit var numbers: ArrayDeque<Pair<Long, Int>>
    var zeroIdx = 0

    override fun parseInput(text: String) {
        numbers = ArrayDeque()
        for ((i, line) in text.trim().split("\n").withIndex()) {
            numbers.addLast(Pair(line.toLong(), i))
            if (line.toInt() == 0) {
                zeroIdx = i
            }
        }
    }

    override fun compute(): Long {
        val originalNumbers = ArrayDeque(numbers)
        mix(originalNumbers)

        val i = numbers.indexOf(Pair(0, zeroIdx))
        val num1 = numbers[(i + 1000) % numbers.size].first
        val num2 = numbers[(i + 2000) % numbers.size].first
        val num3 = numbers[(i + 3000) % numbers.size].first
        return num1 + num2 + num3
    }

    fun mix(originalNumbers: ArrayDeque<Pair<Long, Int>>) {
        for (idx in numbers.indices) {
            val value = originalNumbers[idx]
            val num = value.first
            if (num == 0L) {
                continue
            }

            val i = numbers.indexOf(value)
            Collections.rotate(numbers, -i)
            numbers.removeFirst()

            rotate(numbers, -num)
            numbers.addFirst(value)
            rotate(numbers, num)
            Collections.rotate(numbers, i)
            if (num < 0L) {
                Collections.rotate(numbers, -1)
            }
        }
    }

    fun rotate(list: MutableList<*>, distance: Long) {
        if (distance == 0L) {
            return
        }

        if (distance > 0) {
            var todo = distance
            while (todo != 0L) {
                var current = todo.toInt() and Int.MAX_VALUE
                if (current == 0) {
                    current = Int.MAX_VALUE
                }
                todo -= current
                Collections.rotate(list, current)
            }
        } else {
            var todo = distance
            while (todo != 0L) {
                var current = todo.toInt() or Int.MIN_VALUE
                todo -= current
                Collections.rotate(list, current)
            }
        }
    }

    override fun getExampleAnswer(): Int {
        return 3
    }

}

class Part20B : Part20A() {
    override fun config() {
        numbers = ArrayDeque(numbers.mapIndexed { i, number -> Pair(number.first * 811_589_153L, i) })
    }

    override fun compute(): Long {
        val originalNumbers = ArrayDeque(numbers)
        repeat(10) { mix(originalNumbers) }

        val i = numbers.indexOf(Pair(0, zeroIdx))
        val num1 = numbers[(i + 1000) % numbers.size].first
        val num2 = numbers[(i + 2000) % numbers.size].first
        val num3 = numbers[(i + 3000) % numbers.size].first
        return num1 + num2 + num3
    }

    override fun getExampleAnswer(): Int {
        return 1_623_178_306
    }
}

fun main() {
    Day(2022, 20, Part20A(), Part20B())
}