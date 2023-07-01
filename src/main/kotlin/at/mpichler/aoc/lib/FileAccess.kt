package at.mpichler.aoc.lib

import java.io.File

/**
 * Handles access to the cached files and session token.
 */
internal object FileAccess {
    private val HOME_DIR = System.getProperty("user.home", "")
    private val CONFIG_DIR = "$HOME_DIR/.config/adventofcode"
    private val TOKEN_PATH = "$CONFIG_DIR/token"

    /**
     * Get the session token stored in the file "~/.config/adventofcode/token".
     */
    fun getToken(): String {
        val sessionFile = File(TOKEN_PATH)
        return sessionFile.readText()
    }

    private fun getFilePrefix(year: Int, day: Int): String {
        return "${getToken().takeLast(10)}/${year}_${"%02d".format(day)}"
    }

    private fun getStringFromFile(fileName: String): String? {
        val inputFile = File(fileName)
        if (!inputFile.exists()) {
            return null
        }

        return inputFile.readText()
    }

    private fun saveStringToFile(fileName: String, text: String) {
        val file = File(fileName)
        file.parentFile.mkdirs()
        file.writeText(text)
    }

    fun getExampleInput(year: Int, day: Int): String? {
        return getStringFromFile("$CONFIG_DIR/${getFilePrefix(year, day)}_example_input.txt")
    }

    fun saveExampleInput(year: Int, day: Int, example: String) {
        saveStringToFile("$CONFIG_DIR/${getFilePrefix(year, day)}_example_input.txt", example)
    }

    fun getInput(year: Int, day: Int): String? {
        return getStringFromFile("$CONFIG_DIR/${getFilePrefix(year, day)}_input.txt")
    }

    fun saveInput(year: Int, day: Int, input: String) {
        saveStringToFile("$CONFIG_DIR/${getFilePrefix(year, day)}_input.txt", input)
    }

    fun getAnswer(year: Int, day: Int, part: Part): String? {
        val partName = if (part == Part.A) "a" else "b"
        return getStringFromFile("$CONFIG_DIR/${getFilePrefix(year, day)}${partName}_answer.txt")
    }

    fun saveAnswer(year: Int, day: Int, part: Part, answer: String) {
        val partName = if (part == Part.A) "a" else "b"
        saveStringToFile("$CONFIG_DIR/${getFilePrefix(year, day)}${partName}_answer.txt", answer)
    }

    fun getBadAnswers(year: Int, day: Int, part: Part): List<String> {
        val partName = if (part == Part.A) "a" else "b"
        val inputFile = File("$CONFIG_DIR/${getFilePrefix(year, day)}${partName}_bad_answers.txt")
        if (!inputFile.exists()) {
            return listOf()
        }

        return inputFile.readLines()
    }

    fun saveBadAnswer(year: Int, day: Int, part: Part, answer: String) {
        val partName = if (part == Part.A) "a" else "b"
        val file = File("$CONFIG_DIR/${getFilePrefix(year, day)}${partName}_bad_answers.txt")
        file.parentFile.mkdirs()
        file.appendText(answer)
    }
}