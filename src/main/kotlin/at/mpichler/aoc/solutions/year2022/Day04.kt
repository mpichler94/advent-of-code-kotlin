package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import java.lang.RuntimeException

internal data class Sections(val section1: Set<Int>, val section2: Set<Int>) {
    /**
     * Get if [section1] is a subset of [section2] or [section2] is a subset of [section1]
     */
    fun isSubset(): Boolean {
        return section1.containsAll(section2) || section2.containsAll(section1)
    }

    /**
     * Get if the sections do overlap
     */
    fun doOverlap(): Boolean {
        return section1.intersect(section2).isNotEmpty()
    }
}

internal fun Sections(pair: String): Sections {
    val result = Regex("^(\\d+)-(\\d+),(\\d+)-(\\d+)").find(pair) ?: throw RuntimeException()

    val startS1 = result.groupValues[1].toInt()
    val startS2 = result.groupValues[3].toInt()
    val endS1 = result.groupValues[2].toInt()
    val endS2 = result.groupValues[4].toInt()

    val section1 = (startS1..endS1).toSet()
    val section2 = (startS2..endS2).toSet()

    return Sections(section1, section2)
}

open class Part4A : PartSolution() {
    lateinit var pairs: List<String>
    override fun parseInput(text: String) {
        pairs = text.trim().split("\n")
    }

    override fun compute(): Int {
        return pairs.map(::Sections).count(Sections::isSubset)
    }

    override fun getExampleAnswer(): Int {
        return 2
    }
}

class Part4B : Part4A() {

    override fun compute(): Int {
        return pairs.map(::Sections).count(Sections::doOverlap)
    }

    override fun getExampleAnswer(): Int {
        return 4
    }
}

fun main() {
    Day(2022, 4, Part4A(), Part4B())
}