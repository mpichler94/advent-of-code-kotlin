package at.mpichler.aoc.lib

import mu.KotlinLogging
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

/**
 * Specifies the individual parts of a Puzzle.
 */
enum class Part(val value: Int) {
    /** The first part of the puzzle. */
    A(1),

    /** The second part of the puzzle. */
    B(2)
}

/**
 * Specifies a custom test case for a puzzle.
 * @property input The input for the solution.
 * @property result The expected result for the input.
 * @property name Custom name shown in logs
 */
data class Test(val input: String, val result: Any, val name: String)

/**
 * Implements the solution for a part of a puzzle.
 */
@OptIn(ExperimentalTime::class)
abstract class PartSolution {
    companion object {
        private const val GREEN = "${27.toChar()}[32m"
        private const val RED = "${27.toChar()}[31m"
        private const val DEFAULT = "${27.toChar()}[00m"
    }

    private val logger = KotlinLogging.logger {}

    /**
     * Parse the input for the puzzle. This input may be the example input or the
     * input for the actual puzzle. The input is the raw string and not trimmed.
     */
    abstract fun parseInput(text: String)

    /**
     * Here you can do any configuration before the [compute] function is called.
     */
    open fun config() { /* May be overridden to do configuration before compute */
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
    open fun tests(): Sequence<Test> {
        return sequenceOf()
    }

    /**
     * Return the example input for the puzzle. The example is used to test your
     * implementation before it is executed with the real puzzle input.
     *
     * The example input will be gathered from the Advent of Code website but in
     * some cases this input may not be correct or what you expect, in these cases
     * you can override this method to supply custom example input.
     *
     */
    open fun getExampleInput(): String? {
        return null
    }

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
        val elapsed1 = measureTime { parseInput(testInput) }
        logger.info { "Parse $elapsed1" }

        val elapsed2 = measureTime { config() }
        logger.info { "Config $elapsed2" }

        val (result, elapsed3) = measureTimedValue { compute() }
        logger.info { "Compute $elapsed3" }
        return result.toString()
    }

    private fun test(exampleInput: String): Boolean {
        logger.info { "Start test ..." }

        val startTime = System.currentTimeMillis()

        val tests = buildList {
            add(Test(exampleInput, getExampleAnswer().toString(), "Example"))
            addAll(tests())
        }

        val passedTests = tests.map { executeTest(it) }.count { it }

        val endTime = System.currentTimeMillis()
        val duration = (endTime - startTime) / 1000.0
        logger.info { "Testing finished after ${"%.2f".format(duration)}s" }
        logger.info { "$passedTests of ${tests.size} passed" }

        return passedTests == tests.size
    }

    private fun executeTest(test: Test): Boolean {
        val result = testSolve(test.input)
        return if (result == test.result.toString()) {
            logger.info { "Test ${test.name} $GREEN OK $DEFAULT Result: $result" }
            true
        } else {
            logger.error { "Test ${test.name} $RED Failed $DEFAULT" }
            logger.error { "  !!  Expected ${test.result} but got $result" }
            false
        }
    }

    private fun doSolve(puzzle: Puzzle): String {
        logger.info { "Start solving ..." }

        val input = puzzle.getInput()

        val elapsed1 = measureTime { parseInput(input) }
        logger.info { "Parse $elapsed1" }

        val elapsed2 = measureTime { config() }
        logger.info { "Config $elapsed2" }

        val (result, elapsed3) = measureTimedValue { compute() }
        logger.info { "Compute $elapsed3" }
        return result.toString()
    }
}