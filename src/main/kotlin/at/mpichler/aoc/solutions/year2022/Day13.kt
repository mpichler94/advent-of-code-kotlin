package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part13A : PartSolution() {
    lateinit var pairs: List<Pair<List<Any>, List<Any>>>

    override fun parseInput(text: String) {
        val lines = text.trim().split("\n")
        val pairs: MutableList<Pair<List<Any>, List<Any>>> = mutableListOf()

        for (i in lines.indices step 3) {
            val (_, left) = parseList(lines[i])
            val (_, right) = parseList(lines[i + 1])
            pairs.add(Pair(left, right))
        }

        this.pairs = pairs
    }

    private fun parseList(text: String): Pair<Int, List<Any>> {
        val list = mutableListOf<Any>()
        var i = 1
        while (i < text.length) {
            val token = getNextToken(text, i)
            if (token[0] == '[') {
                val (offset, values) = parseList(text.substring(i))
                i += offset
                list.add(values)
                if (text[i] == ',') {
                    i += 1
                }
            } else if (text[i] == ']') {
                return Pair(i + 1, list)
            } else {
                list.add(token.dropLast(1).toInt())
                i += token.length
                if (token.endsWith("]")) {
                    return Pair(i, list)
                }
            }
        }

        return Pair(i, list)
    }

    private fun getNextToken(text: String, offset: Int = 0): String {
        var end = offset
        while (end < text.length) {
            val c = text[end]
            if (c == '[' || c == ']' || c == ',') {
                return text.substring(offset, end + 1)
            }
            end += 1
        }
        return ""
    }

    override fun compute(): Int {
        var indexSum = 0

        for ((i, pair) in pairs.withIndex()) {
            val (result, _) = checkOrder(pair.first, pair.second, i + 1)
            if (result) {
                indexSum += i + 1
            }
        }

        return indexSum
    }

    fun checkOrder(left: Any, right: Any, i: Int, depth: Int = 0): Pair<Boolean, Boolean> {
        if (left is Int) {
            if (right is Int) {
                return checkOrder(left, right, i, depth)
            }
            return checkOrder(left, right as List<Any>, i, depth)
        }
        if (right is Int) {
            return checkOrder(left as List<Any>, right, i, depth)
        }
        return checkOrder(left as List<Any>, right as List<Any>, i, depth)
    }

    private fun checkOrder(left: Int, right: Int, i: Int, depth: Int = 0): Pair<Boolean, Boolean> {
        return checkOrder(listOf(left), listOf(right), i, depth)
    }

    private fun checkOrder(left: Int, right: List<Any>, i: Int, depth: Int = 0): Pair<Boolean, Boolean> {
        return checkOrder(listOf(left), right, i, depth)
    }

    private fun checkOrder(left: List<Any>, right: Int, i: Int, depth: Int = 0): Pair<Boolean, Boolean> {
        return checkOrder(left, listOf(right), i, depth)
    }

    private fun checkOrder(left: List<Any>, right: List<Any>, i: Int, depth: Int = 0): Pair<Boolean, Boolean> {
        var l = 0
        var r = 0

        while (true) {
            if (l >= left.size) {
                if (r < right.size) {
                    return Pair(true, true)
                }
                return Pair(true, false)
            }
            if (r >= right.size) {
                return Pair(false, true)
            }

            val currentLeft = left[l++]
            val currentRight = right[r++]

            if (currentLeft is List<*> || currentRight is List<*>) {
                val (ret, imm) = checkOrder(currentLeft, currentRight, i, depth + 1)
                if (!ret) {
                    return Pair(false, true)
                }
                if (imm) {
                    return Pair(true, true)
                }
            } else {
                if ((currentLeft as Int) < (currentRight as Int)) {
                    return Pair(true, true)
                }
                if (currentLeft > currentRight) {
                    return Pair(false, true)
                }
            }
        }
    }

    override fun getExampleAnswer(): Int {
        return 13
    }
}

class Part13B : Part13A() {
    override fun compute(): Int {
        val packets = pairs.flatMap { it.toList() }
        val sPackets = mutableListOf<Any>(2, 6)

        for (packet in packets) {
            var i = 0
            while (i < sPackets.size) {
                val (result, _) = checkOrder(packet, sPackets[i], 0)
                if (result) {
                    break
                }
                i += 1
            }
            sPackets.add(i, packet)
        }

        val index1 = sPackets.indexOf(2) + 1
        val index2 = sPackets.indexOf(6) + 1
        return index1 * index2
    }

    override fun getExampleAnswer(): Int {
        return 140
    }
}

fun main() {
    Day(2022, 13, Part13A(), Part13B())
}