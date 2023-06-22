package at.mpichler.aoc.lib

import mu.KotlinLogging

/**
 * Handles the execution of the [PartSolution]s for a single puzzle-day.
 */
internal class Puzzle(private val year: Int, private val day: Int) {

    companion object {
        private const val GREEN = "${27.toChar()}[32m"
        private const val RED = "${27.toChar()}[31m"
        private const val DEFAULT = "${27.toChar()}[00m"
    }

    private val logger = KotlinLogging.logger {}
    private val apiClient = ApiClient(FileAccess.getToken(), year, day)

    fun submit(part: Part, result: String) {
        val savedAnswer = FileAccess.getAnswer(year, day, part)

        val badAnswers = FileAccess.getBadAnswers(year, day, part)

        if (badAnswers.contains(result)) {
            logger.info { " $RED Fail $DEFAULT Value is incorrect and already submitted: '$result'" }
        } else if (savedAnswer == null) {
            apiClient.submit(result, part)
            logger.info { " $GREEN OK $DEFAULT Answer submitted" }
        } else if (FileAccess.getAnswer(year, day, part) == result) {
            logger.info { " $GREEN OK $DEFAULT Already answered, values match" }
        } else {
            logger.info { " $RED Fail $DEFAULT Value differs from submitted answer. Now: '$result' Submitted: '$savedAnswer'" }
        }
    }

    fun getExampleInput(): String {
        var exampleInput = FileAccess.getExampleInput(year, day)
        if (exampleInput == null) {
            exampleInput = apiClient.getExample()
            FileAccess.saveExampleInput(year, day, exampleInput)
        }
        return exampleInput
    }

    fun getInput(): String {
        var input = FileAccess.getInput(year, day)
        if (input == null) {
            input = apiClient.getInput()
            FileAccess.saveInput(year, day, input)
        }
        return input
    }
}