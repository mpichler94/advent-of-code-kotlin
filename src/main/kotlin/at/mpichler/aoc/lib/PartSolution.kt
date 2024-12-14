package at.mpichler.aoc.lib

import kotlin.time.TimeSource
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

/**
 * Specifies the individual parts of a Puzzle.
 */
enum class Part(
    val value: Int,
) {
    /** The first part of the puzzle. */
    A(1),

    /** The second part of the puzzle. */
    B(2),
}

/**
 * Specifies a custom test case for a puzzle.
 * @property input The input for the solution.
 * @property result The expected result for the input.
 * @property name Custom name shown in logs
 */
data class Test(
    val input: String,
    val result: Any,
    val name: String,
)

/**
 * Implements the solution for a part of a puzzle.
 */
abstract class PartSolution {
    companion object {
        private const val GREEN = "${27.toChar()}[32m"
        private const val RED = "${27.toChar()}[31m"
        private const val YELLOW = "${27.toChar()}[33m"
        private const val BLUE = "${27.toChar()}[34m"
        private const val BG_GREEN = "${27.toChar()}[42m"
        private const val BG_RED = "${27.toChar()}[41m"
        private const val DEFAULT = "${27.toChar()}[00m"
    }

    /**
     * Parse the input for the puzzle. This input may be the example input or the
     * input for the actual puzzle. The input is the raw string and not trimmed.
     */
    abstract fun parseInput(text: String)

    /**
     * Here you can do any configuration before the [compute] function is called.
     */
    open fun config() { // May be overridden to do configuration before compute
    }

    /**
     * This function computes the solution for the puzzle.
     * @return Puzzle solution as string without padding or newlines.
     */
    abstract fun compute(): Any

    /**
     * Return custom tests that are executed additionally to the example to test
     * your implementation before it is executed with the actual puzzle input.
     */
    open fun tests(): Sequence<Test> = sequenceOf()

    /**
     * Return the example input for the puzzle. The example is used to test your
     * implementation before it is executed with the real puzzle input.
     *
     * The example input will be gathered from the Advent of Code website but in
     * some cases this input may not be correct or what you expect, in these cases
     * you can override this method to supply custom example input.
     *
     */
    open fun getExampleInput(): String? = null

    /**
     * Return the expected result for the example input.
     */
    abstract fun getExampleAnswer(): Any

    internal fun run(puzzle: Puzzle, part: Part) {
        val testsOk = runTests(puzzle)
        if (!testsOk) {
            return
        }

        val result = doSolve(puzzle)

        puzzle.submit(part, result)
    }

    private fun runTests(puzzle: Puzzle): Boolean {
        var exampleInput = getExampleInput()
        if (exampleInput == null) {
            exampleInput = puzzle.getExampleInput()
        }

        return test(exampleInput)
    }

    private fun testSolve(testInput: String): String {
        val elapsed1 = measureTime { parseInput(testInput.trimEnd()) }
        println("Parse $YELLOW$elapsed1$DEFAULT")

        val elapsed2 = measureTime { config() }
        println("Config $YELLOW$elapsed2$DEFAULT")

        val (result, elapsed3) = measureTimedValue { compute() }
        println("Compute $YELLOW$elapsed3$DEFAULT")
        return result.toString()
    }

    private fun test(exampleInput: String): Boolean {
        println("Start test ...")

        val timeSource = TimeSource.Monotonic
        val start = timeSource.markNow()

        val tests =
            buildList {
                add(Test(exampleInput, getExampleAnswer().toString(), "Example"))
                addAll(tests())
            }

        val passedTests = tests.map { executeTest(it) }.count { it }

        val end = timeSource.markNow()
        val duration = (end - start)
        println("Testing finished after $YELLOW$duration$DEFAULT")
        println("$passedTests of ${tests.size} passed")

        return passedTests == tests.size
    }

    private fun executeTest(test: Test): Boolean {
        val result = testSolve(test.input)
        return if (result == test.result.toString()) {
            println("Test ${test.name} $GREEN OK $DEFAULT Result: $BLUE$result$DEFAULT")
            true
        } else {
            println("ERROR: Test ${test.name} $RED Failed $DEFAULT")
            println("  !!  Expected $BG_GREEN${test.result}$DEFAULT but got $BG_RED$result$DEFAULT")
            false
        }
    }

    private fun doSolve(puzzle: Puzzle): String {
        val timeSource = TimeSource.Monotonic
        val start = timeSource.markNow()

        println("Start solving ...")

        val input = puzzle.getInput()

        val elapsed1 = measureTime { parseInput(input.trimEnd()) }
        println("Parse $YELLOW$elapsed1$DEFAULT")

        val elapsed2 = measureTime { config() }
        println("Config $YELLOW$elapsed2$DEFAULT")

        val (result, elapsed3) = measureTimedValue { compute() }
        println("Compute $YELLOW$elapsed3$DEFAULT")

        val end = timeSource.markNow()
        val duration = (end - start)
        println("Execution finished after $YELLOW$duration$DEFAULT")
        return result.toString()
    }
}
