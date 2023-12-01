# advent-of-code-kotlin

![GitHub](https://img.shields.io/github/license/mpichler94/advent-of-code-kotlin)
![Static Badge](https://img.shields.io/badge/Kotlin-1.9.20-blue)
![womm](https://cdn.rawgit.com/nikku/works-on-my-machine/v0.2.0/badge.svg)
![GitHub last commit (by committer)](https://img.shields.io/github/last-commit/mpichler94/advent-of-code-kotlin)

This repository contains a library to help solve the Advent of Code Challenges
(https://adventofcode.com) in Kotlin. This library will get the puzzle input and
example data from the website and also submit the results. 

There are additional helper functions to work with vectors and do traversals through
graphs. 

At the moment this repository also contains my solutions for the puzzles; I will
probably move them to a separate repository at some point.

# Install and Run

No specific installation is needed, you only need a somewhat recent Java Runtime.
I will not give any specific version constraints, but would recommend at least
version 1.8.

The library is not yet available on Maven, so you need to check out or download 
the repository and add the source files to your project manually.

Puzzle inputs are different for each user, so the library needs your session 
token to get the puzzle inputs for your specific user. You can get the session 
token from your browser cookies after you are logged in on https://adventofcode.com. 
Check this [post from Stackexchange](https://superuser.com/a/1114501) for help. Then you need to create a 
file with path `~/.config/adventofcode/token` containing the session token without 
whitespace or newlines. On Windows, the path will be 
`C:\User\<username>\.config\adventofcode\token`.

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
