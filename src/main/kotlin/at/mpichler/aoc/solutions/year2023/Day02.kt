package at.mpichler.aoc.solutions.year2023

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution

open class Part2A : PartSolution() {
    internal lateinit var games: List<Game>

    override fun parseInput(text: String) {
        games = text.trim().split("\n").map { Game(it) }
    }

    override fun getExampleAnswer() = 8

    override fun compute(): Int {
        return games.filter { it.valid }.sumOf { it.id }
    }

    internal data class Game(val id: Int, val rounds: List<Round>) {
        val valid get() = rounds.all { it.valid }

        private val minRed get() = rounds.maxOf { it.red }
        private val minGreen get() = rounds.maxOf { it.green }
        private val minBlue get() = rounds.maxOf { it.blue }

        val power get() = minRed * minGreen * minBlue
    }

    internal data class Round(val red: Int, val green: Int, val blue: Int) {
        val sum get() = red + green + blue
        val valid = !(sum > 39 || red > 12 || green > 13 || blue > 14)
    }

    private fun Game(text: String): Game {
        val match = "Game (\\d+): (.*)".toRegex().find(text)
        match as MatchResult
        val id = match.groupValues[1].toInt()
        val result = match.groupValues[2]
        val r = result.split("; ")
        val rounds = mutableListOf<Round>()
        for (round in r) {
            var red = 0
            var green = 0
            var blue = 0
            val cubes = round.split(", ")
            for (cube in cubes) {
                when {
                    cube.endsWith("red") -> red += cube.replace(" red", "").toInt()
                    cube.endsWith("green") -> green += cube.replace(" green", "").toInt()
                    cube.endsWith("blue") -> blue += cube.replace(" blue", "").toInt()
                }
            }
            rounds.add(Round(red, green, blue))
        }

        return Game(id, rounds)
    }
}

class Part2B : Part2A() {
    override fun getExampleAnswer() = 2286

    override fun compute(): Int {
        return games.sumOf { it.power }
    }
}

fun main() {
    Day(2023, 2, Part2A(), Part2B())
}
