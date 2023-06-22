# advent-of-code-kotlin

This repository contains a library to help solve the Advent of Code Challenges
(https://adventofcode.com) in Kotlin. This library will get the puzzle input and
example data from the website and also submit the results. 

At the moment this repository also contains my solutions for the puzzles; I will
probably move them to a separate repository at some point.

# Install and Run

No specific installation is needed, you only need a somewhat recent Java Runtime.
I will not give any specific version constraints, but would recommend at least
version 1.8.

To use the library, implement a class containing the solution for a puzzle and 
create a `Day` object in the `main` function. This is shown in the following 
snippet:
```kotlin
class PartA : PartSolution() {
    lateinit var numbers: List<Int>

    override fun parseInput(text: String) {
        numbers = text.trim().split("\n").map { it.toInt() }.toList()
    }

    override fun getExampleAnswer(): String {
        return "7"
    }

    override fun compute(): String {
        return countIncreases().toString()
    }

    private fun countIncreases(): Int {
        return numbers.windowed(2).map { it[1] > it[0] }.count { it }
    }
}

class PartB : PartA() {
    ...
}

fun main() {
    Day(2021, 1, PartA(), PartB())
}

```

You should also have a [Logback](https://logback.qos.ch/) configuration file in
your project's root folder or at a configured location.
