package at.mpichler.aoc.lib

/**
 * Handles the execution of the [PartSolution]s for a single puzzle-day.
 */
internal class Puzzle(
    private val year: Int,
    private val day: Int,
    private val autoSubmit: Boolean,
) {
    companion object {
        private const val GREEN = "${27.toChar()}[32m"
        private const val RED = "${27.toChar()}[31m"
        private const val YELLOW = "${27.toChar()}[33m"
        private const val BLUE = "${27.toChar()}[34m"
        private const val DEFAULT = "${27.toChar()}[00m"
    }

    private val apiClient = ApiClient(FileAccess.getToken(), year, day)

    fun submit(part: Part, answer: String) {
        val submit: Boolean =
            if (!autoSubmit) {
                println("Submit answer $BLUE'$answer'$DEFAULT? [y,n]")
                val input = readln()
                input == "y"
            } else {
                true
            }
        if (!submit) {
            println("Did not submit answer")
            return
        }

        var savedAnswer = FileAccess.getAnswer(year, day, part)
        if (savedAnswer == null) {
            apiClient.updateAnswers()
            savedAnswer = FileAccess.getAnswer(year, day, part)
        }

        val badAnswers = FileAccess.getBadAnswers(year, day, part)
        if (badAnswers.contains(answer)) {
            println(" $RED Fail $DEFAULT Value is incorrect and already submitted: $BLUE'$answer'$DEFAULT")
        } else if (savedAnswer == null) {
            val result = apiClient.submit(answer, part)
            if (result == ApiClient.Result.OK) {
                FileAccess.saveAnswer(year, day, part, answer)
            } else if (result == ApiClient.Result.INCORRECT) {
                FileAccess.saveBadAnswer(year, day, part, answer)
            } else if (result == ApiClient.Result.ALREADY_ANSWERED) {
                println(" Answer submitted. $YELLOW No saved answer. $YELLOW")
                return
            }
            println(" $GREEN OK $DEFAULT Answer submitted")
        } else if (FileAccess.getAnswer(year, day, part) == answer) {
            println(" $GREEN OK $DEFAULT Already answered, values match. $BLUE'$answer'$DEFAULT")
        } else {
            println(
                " $RED Fail $DEFAULT Value differs from submitted answer. Now: $BLUE'$answer'$DEFAULT Submitted: '$savedAnswer'",
            )
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
