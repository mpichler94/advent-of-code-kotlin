package at.mpichler.aoc.solutions.year2021

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.Graph
import at.mpichler.aoc.lib.PartSolution

open class Part12A : PartSolution() {
    private lateinit var graph: Graph

    override fun parseInput(text: String) {
        graph = Graph()
        val edges = text.split("\n").map { it.split("-") }.map { Pair(it[0], it[1]) }
        graph.addEdges(edges)
    }

    override fun compute(): Int {
        return exploreNeighbors("start", 0, mutableListOf())
    }

    private fun exploreNeighbors(node: String, numPaths: Int, visitedSmallCaves: MutableList<String>): Int {
        var paths = numPaths
        if (!canVisit(node, visitedSmallCaves)) {
            return paths
        }
        if (node == node.lowercase()) {
            visitedSmallCaves.add(node)
        }
        if (node == "end") {
            return paths + 1
        }

        for (neighbor in graph.neighbors(node)) {
            paths = exploreNeighbors(neighbor, paths, visitedSmallCaves.toMutableList())
        }

        return paths
    }

    open fun canVisit(node: String, visitedSmallCaves: List<String>): Boolean {
        return node !in visitedSmallCaves
    }

    override fun getExampleAnswer(): Int {
        return 10
    }
}

class Part12B : Part12A() {
    override fun canVisit(node: String, visitedSmallCaves: List<String>): Boolean {
        if (node != node.lowercase()) {
            return true
        }

        val counts = visitedSmallCaves.associateWith { visitedSmallCaves.count { v -> v == it } }
        if (node == "start" && "start" in counts) {
            return false
        }
        val doubleVisited = counts.values.any { it == 2 }
        if (doubleVisited) {
            return node !in counts
        }

        return true
    }

    override fun getExampleAnswer(): Int {
        return 36
    }
}

fun main() {
    Day(2021, 12, Part12A(), Part12B())
}