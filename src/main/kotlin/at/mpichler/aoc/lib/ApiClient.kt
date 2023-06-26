package at.mpichler.aoc.lib

import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers
import java.nio.charset.StandardCharsets
import java.time.Duration

/**
 * Handles access to the Advent of Code Website and REST API.
 * The client provides convenience functions to get the puzzle input and submit
 * solutions.
 * @property session The session token of the user
 * @property year The year for the puzzle
 * @property day The day for the puzzle
 */
internal class ApiClient (private val session: String, private val year: Int, private val day: Int) {

    private val logger = KotlinLogging.logger {}

    private val client: HttpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .connectTimeout(Duration.ofSeconds(30))
        .build()

    init {
        logger.info { "Initialize API client for $year day $day with token $session" }
    }

    private fun getPuzzlePage(): Document {
        logger.info { "Getting puzzle page for $year day $day" }

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://adventofcode.com/$year/day/$day"))
            .timeout(Duration.ofMinutes(1))
            .header("Cookie", "session=$session")
            .header("User-Agent", "https://github.com/mpichler94/advent-of-code-kotlin by kingkriptor01@gmail.com")
            .build()

        val data = client.send(request, BodyHandlers.ofString())
        if (data.statusCode() < 200 || data.statusCode() >= 300) {
            logger.error { "Request for input failed with code ${data.statusCode()}: ${data.headers()}" }
            throw IOException("Cannot get puzzle page")
        }

        val document = Jsoup.parse(data.body())
        val answers = document.select("p:contains(Your puzzle answer was)").toList()
        if (answers.isNotEmpty()) {
            val answer = answers[0].selectFirst("code")?.text()
            if (answer != null) {
                FileAccess.saveAnswer(year, day, Part.A, answer)
            }
        }
        if (answers.size > 1) {
            val answer = answers[1].selectFirst("code")?.text()
            if (answer != null) {
                FileAccess.saveAnswer(year, day, Part.B, answer)
            }
        }

        return document
    }

    fun getExample(): String {
        val document = getPuzzlePage()

        return document.selectFirst("pre")?.wholeText() ?: ""
    }

    fun getInput(): String {
        logger.info { "Getting input for $year day $day" }

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://adventofcode.com/$year/day/$day/input"))
            .timeout(Duration.ofMinutes(1))
            .header("Cookie", "session=$session")
            .header("User-Agent", "https://github.com/mpichler94/advent-of-code-kotlin by kingkriptor01@gmail.com")
            .build()

        val data = client.send(request, BodyHandlers.ofString())
        if (data.statusCode() < 200 || data.statusCode() >= 300) {
            logger.error { "Request for input failed with code ${data.statusCode()}: ${data.headers()}" }
            return ""
        }

        return data.body() ?: ""
    }

    fun submit(answer: String, part: Part): String {
        logger.info { "Submitting answer ''$answer'' for $year day $day" }

        val partNum = if (part == Part.A) 1 else 2
        val body = "answer=" + URLEncoder.encode(answer, StandardCharsets.UTF_8) + "&level=$partNum"

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://adventofcode.com/$year/day/$day/answer"))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Cookie", "session=$session")
            .header("User-Agent", "https://github.com/mpichler94/advent-of-code-kotlin by kingkriptor01@gmail.com")
            .POST(BodyPublishers.ofString(body))
            .build()

        val data = client.send(request, BodyHandlers.ofString())
        if (data.statusCode() < 200 || data.statusCode() >= 300) {
            logger.error { "Request for answer failed with code ${data.statusCode()}: ${data.headers()}" }
            return ""
        }

        val document = Jsoup.parse(data.body())
        val article = document.selectFirst("article") ?: return ""

        if (article.text().contains("That's the right answer")) {
            logger.info { "Answer is correct" }
        } else if (article.text().contains("Did you already complete it")) {
            logger.info { "Already answered" }
        } else if (article.text().contains("That's not the right answer")) {
            logger.error { "Answer is incorrect" }
        } else if (article.text().contains("You gave an answer too recently")) {
            val waitTime = Regex("You have (?:(\\d+)m )?(\\d+)s left to wait").find(article.text())
            if (waitTime != null && waitTime.groups.isNotEmpty()) {
                logger.error { "You gave an answer too recently. $waitTime" }
            }
        }

        return data.body() ?: ""
    }

}