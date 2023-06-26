package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import at.mpichler.aoc.lib.Test
import java.lang.RuntimeException

open class Part6A : PartSolution() {
    lateinit var stream: String

    override fun parseInput(text: String) {
        stream = text.trim()
    }

    override fun compute(): Int {
        for (i in 3 until stream.length) {
            val marker = stream.subSequence(i - 3, i + 1).toSet()
            if (marker.size == 4) {
                return (i + 1)
            }
        }

        throw RuntimeException("No marker found")
    }

    override fun getExampleAnswer() = 7

    override fun tests(): Sequence<Test> {
        return sequence {
            yield(Test("bvwbjplbgvbhsrlpgdmjqwftvncz", 5, "Example2"))
            yield(Test("nppdvjthqldpwncqszvftbrmjlhg", 6, "Example3"))
            yield(Test("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10, "Example4"))
            yield(Test("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11, "Example5"))
        }
    }
}

class Part6B : Part6A() {
    override fun compute(): Int {
        val sop = super.compute() - 5
        for (i in sop + 13 until stream.length) {
            val marker = stream.subSequence(i - 13, i + 1).toSet()
            if (marker.size == 14) {
                return i + 1
            }
        }

        throw RuntimeException("No marker found")
    }

    override fun getExampleAnswer() = 19

    override fun tests(): Sequence<Test> {
        return sequenceOf(
            Test("bvwbjplbgvbhsrlpgdmjqwftvncz", 23, "Example2"),
            Test("nppdvjthqldpwncqszvftbrmjlhg", 23, "Example3"),
            Test("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 29, "Example4"),
            Test("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 26, "Example5"),
        )
    }
}

fun main() {
    Day(2022, 6, Part6A(), Part6B())
}