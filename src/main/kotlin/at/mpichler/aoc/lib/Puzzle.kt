package at.mpichler.aoc.lib

import mu.KotlinLogging

/**
 * Handles the execution of the [PartSolution]s for a single puzzle-day.
 */
internal class Puzzle(private val year: Int, private val day: Int, private val autoSubmit: Boolean) {

    companion object {
        private const val GREEN = "${27.toChar()}[32m"
        private const val RED = "${27.toChar()}[31m"
        private const val DEFAULT = "${27.toChar()}[00m"
    }

    private val logger = KotlinLogging.logger {}
    private val apiClient = ApiClient(FileAccess.getToken(), year, day)

    fun submit(part: Part, answer: String) {
        val submit: Boolean = if (!autoSubmit) {
            println("Submit answer '$answer'? [y,n]")
            val input = readln()
            input == "y"
        } else {
            true
        }
        if (!submit) {
            logger.info { "Did not submit answer" }
            return
        }

        var savedAnswer = FileAccess.getAnswer(year, day, part)
        if (savedAnswer == null) {
            apiClient.updateAnswers()
            savedAnswer = FileAccess.getAnswer(year, day, part)
        }

        val badAnswers = FileAccess.getBadAnswers(year, day, part)
        if (badAnswers.contains(answer)) {
            logger.info { " $RED Fail $DEFAULT Value is incorrect and already submitted: '$answer'" }
        } else if (savedAnswer == null) {
            val result = apiClient.submit(answer, part)
            if (result == ApiClient.Result.OK) {
                FileAccess.saveAnswer(year, day, part, answer)
            } else if (result == ApiClient.Result.INCORRECT) {
                FileAccess.saveBadAnswer(year, day, part, answer)
            }
            logger.info { " $GREEN OK $DEFAULT Answer submitted" }
        } else if (FileAccess.getAnswer(year, day, part) == answer) {
            logger.info { " $GREEN OK $DEFAULT Already answered, values match" }
        } else {
            logger.info { " $RED Fail $DEFAULT Value differs from submitted answer. Now: '$answer' Submitted: '$savedAnswer'" }
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