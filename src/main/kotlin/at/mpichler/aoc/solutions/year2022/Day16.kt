package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import at.mpichler.aoc.lib.ShortestPaths

open class Part16A : PartSolution() {
    private lateinit var valves: Map<String, Valve>
    protected var maxFlow: Int = 0
    protected lateinit var closedValves: Set<String>


    override fun parseInput(text: String) {
        val valves = mutableMapOf<String, Valve>()
        val connectionNames = mutableMapOf<String, List<String>>()
        val pattern = Regex("Valve (.*?) has flow rate=(\\d+); tunnels? leads? to valves? (.*)")
        for (line in text.trim().split("\n")) {
            val result = pattern.find(line)!!
            val connections = result.groupValues[3].split(", ")
            val name = result.groupValues[1]
            connectionNames[name] = connections
            valves[name] = Valve(name, result.groupValues[2].toInt())
        }

        for ((name, con) in connectionNames) {
            val connections = mutableListOf<Valve>()
            for (connection in con) {
                connections.add(valves[connection]!!)
            }
            valves[name]?.connections = connections
        }

        this.valves = valves
    }

    override fun config() {
        maxFlow = valves.values.sumOf { it.flow }
        closedValves = valves.filter { it.value.flow > 0 }.map { it.key }.toSet()
    }

    override fun compute(): Int {
        val traversal = ShortestPaths { node, _ -> nextEdges(node) }
        traversal.startFrom(Node("AA", closedValves, 0))
        for ((_, closed, _) in traversal) {
            if (traversal.depth == 30 || closed.isEmpty()) {
                return maxFlow * 30 - traversal.distance
            }
        }

        error("Non reachable")
    }

    protected fun nextEdges(node: Node): Sequence<Pair<Node, Int>> {
        val (pos, closed, flow) = node
        val valve = valves[pos]!!

        return sequence {
            val cost = maxFlow - flow
            if (pos in closed) {
                yield(Pair(Node(pos, closed.minus(pos), flow + valve.flow), cost))
            }
            for (v in valve.connections) {
                yield(Pair(Node(v.name, closed, flow), cost))
            }
        }
    }

    override fun getExampleAnswer(): Int {
        return 1651
    }

    protected data class Node(val name: String, val closed: Set<String>, val flow: Int)

    data class Valve(val name: String, val flow: Int) {
        var connections: List<Valve> = listOf()
    }
}

class Part16B : Part16A() {
    override fun compute(): Int {
        val traversal = ShortestPaths(this::nextEdgesB)
        traversal.startFrom(NodeB("AA", "AA", closedValves, 0))
        for ((_, closed, _) in traversal) {
            if (traversal.depth == 26 || closed.isEmpty()) {
                return maxFlow * 26 - traversal.distance
            }
        }

        error("Non reachable")
    }

    private fun nextEdgesB(node: NodeB, traversal: ShortestPaths<NodeB>): Sequence<Pair<NodeB, Int>> {
        val (pos1, pos2, closed, flow) = node
        return sequence {
            nextEdges(Node(pos1, closed, flow)).forEach { (s1, weight1) -> nextEdges(Node(pos2, closed, flow)).forEach { (s2, _) ->
                if (s1 != s2 || s1.closed == closed) {
                    yield(Pair(NodeB(s1.name, s2.name, s1.closed.intersect(s2.closed), s1.flow + s2.flow - flow), weight1))
                }
            } }
        }
    }

    override fun getExampleAnswer(): Int {
        return 1707
    }

    private data class NodeB(val pos1: String, val pos2: String, val closed: Set<String>, val flow: Int)
}

fun main() {
    Day(2022, 16, Part16A(), Part16B())
}