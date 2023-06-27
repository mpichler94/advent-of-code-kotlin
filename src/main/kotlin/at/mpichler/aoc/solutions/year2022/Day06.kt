package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import at.mpichler.aoc.lib.Test

open class Part6A : PartSolution() {
    lateinit var stream: String

    override fun parseInput(text: String) {
        stream = text.trim()
    }

    override fun compute() = findStartMarker(stream, 4)

    /**
     * Find the start marker
     * @param message Message containing the markers
     * @param size size of the marker
     * @param index start index for the search
     */
    internal fun findStartMarker(message: String, size: Int, index: Int = 0): Int {
        val pos = message.drop(index).windowed(size, transform = CharSequence::toSet).indexOfFirst { it.size == size }
        return pos + index + size
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
    override fun compute() = findStartMarker(stream, 14, super.compute() - 5)

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