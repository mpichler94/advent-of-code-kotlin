package at.mpichler.aoc.lib

import kotlin.test.Test


class PuzzleTest {


    @Test
    fun testExecution() {
        val impl = PartA()

        impl.doPart()
    }


    class Data (val text: String)

    class PartA : Puzzle<Data>(2021, 1, Part.A) {
        override fun parseInput(text: String): Data {
            return Data(text.trim())
        }

        override fun getExampleAnswer(): String {
            return "7"
        }

        override fun compute(data: Data): String {

            return "1195"
        }

    }
}