package at.mpichler.aoc.lib

/**
 * Implements the main entry point for the solution of a Day. The provided
 * solution(s) will be executed directly when the [Day] is created.
 *
 * Example use case:
 * ```
 * fun main() {
 *     Day(2018, 12, PartA(), PartB)
 * ```
 *
 * Where part
 * @property year Year that is solved, should be >= 2015
 * @property day Day that is solved, should be 1 <= day <= 25
 * @property partA Implementation of the solution for the first part of the puzzle
 * @property partB Optional implementation of the solution for the second part
 * of the puzzle
 */
class Day(
    private val year: Int,
    private val day: Int,
    private val partA: PartSolution,
    private val partB: PartSolution? = null,
    private val autoSubmit: Boolean = true,
) {
    init {
        run()
    }

    private fun run() {
        val puzzle = Puzzle(year, day, autoSubmit)

        println("")
        println("================== Part A ==================")
        partA.run(puzzle, Part.A)

        if (partB == null) {
            return
        }

        println("")
        println("================== Part B ==================")
        partB.run(puzzle, Part.B)
    }
}
