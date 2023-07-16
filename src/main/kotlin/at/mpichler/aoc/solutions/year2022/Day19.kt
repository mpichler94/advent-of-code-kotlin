package at.mpichler.aoc.solutions.year2022

import at.mpichler.aoc.lib.Day
import at.mpichler.aoc.lib.PartSolution
import at.mpichler.aoc.lib.ShortestPaths
import kotlin.math.min

open class Part19A : PartSolution() {
    protected lateinit var blueprints: List<Blueprint>

    override fun parseInput(text: String) {
        val lines = text.trim().split("\n")

        val blueprints = mutableListOf<Blueprint>()
        val pattern =
            Regex("Blueprint (\\d+): Each ore robot costs (\\d+) ore\\. Each clay robot costs (\\d+) ore\\. Each obsidian robot costs (\\d+) ore and (\\d+) clay\\. Each geode robot costs (\\d+) ore and (\\d+) obsidian\\.")
        for (line in lines) {
            val result = pattern.find(line)!!

            val id = result.groupValues[1].toInt()
            val oreRobot = result.groupValues[2].toInt()
            val clayRobot = result.groupValues[3].toInt()
            val obsidianRobotOreCost = result.groupValues[4].toInt()
            val obsidianRobotClayCost = result.groupValues[5].toInt()
            val geodeRobotOreCost = result.groupValues[6].toInt()
            val geodeRobotObsidianCost = result.groupValues[7].toInt()

            val costs = listOf(
                Costs(oreRobot),
                Costs(clayRobot),
                Costs(obsidianRobotOreCost, obsidianRobotClayCost, 0, 0),
                Costs(geodeRobotOreCost, 0, geodeRobotObsidianCost)
            )
            blueprints.add(Blueprint(id, costs))
        }
        this.blueprints = blueprints
    }

    override fun compute(): Int {
        val qualityLevels = mutableListOf<Int>()
        for (blueprint in blueprints) {
            qualityLevels.add(blueprint.produce(24) * blueprint.id)
        }

        return qualityLevels.sum()
    }

    override fun getExampleAnswer(): Int {
        return 33
    }

    override fun getExampleInput(): String? {
        return """
         Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
         Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
        """.trimIndent()
    }

    enum class RobotType(val idx: Int) {
        ORE(0), CLAY(1), OBSIDIAN(2), GEODE(3)
    }

    data class Costs(val ore: Int = 0, val clay: Int = 0, val obsidian: Int = 0, val geode: Int = 0) {
        operator fun plus(other: Costs): Costs {
            return Costs(ore + other.ore, clay + other.clay, obsidian + other.obsidian, geode + other.geode)
        }

        operator fun minus(other: Costs): Costs {
            return Costs(ore - other.ore, clay - other.clay, obsidian - other.obsidian, geode - other.geode)
        }

        operator fun times(factor: Int): Costs {
            return Costs(ore * factor, clay * factor, obsidian * factor, geode * factor)
        }

        operator fun get(type: RobotType): Int {
            return when (type) {
                RobotType.ORE -> ore
                RobotType.CLAY -> clay
                RobotType.OBSIDIAN -> obsidian
                RobotType.GEODE -> geode
            }
        }

        fun allLessEqual(other: Costs): Boolean {
            return ore <= other.ore && clay <= other.clay && obsidian <= other.obsidian && geode <= other.geode
        }

        fun inc(type: RobotType): Costs {
            return when (type) {
                RobotType.ORE -> Costs(ore + 1, clay, obsidian, geode)
                RobotType.CLAY -> Costs(ore, clay + 1, obsidian, geode)
                RobotType.OBSIDIAN -> Costs(ore, clay, obsidian + 1, geode)
                RobotType.GEODE -> Costs(ore, clay, obsidian, geode + 1)
            }
        }
    }

    data class Blueprint(val id: Int, private val costs: List<Costs>) {
        private val maxRobots = costs.reduce { acc, c -> max(acc, c) }

        fun produce(minutes: Int): Int {
            fun nextEdges(node: Node, traversal: ShortestPaths<Node>): Sequence<Pair<Node, Int>> {
                val minutesRemaining = minutes - traversal.depth
                if (minutesRemaining == 1) {
                    return sequenceOf()
                }

                return sequence {
                    val newMaterials = node.materials + node.producing
                    var robotsBuild = 0
                    for (robotId in RobotType.entries) {
                        if (robotId != RobotType.GEODE && node.producing[robotId] >= maxRobots[robotId]) {
                            continue
                        }

                        if (costs[robotId].allLessEqual(node.materials)) {
                            robotsBuild += 1
                            val weight = if (robotId == RobotType.GEODE) 0 else minutesRemaining
                            yield(Pair(Node(newMaterials - costs[robotId], node.producing.inc(robotId)), weight))
                        }
                    }

                    if (robotsBuild < 4) {
                        val maxMaterials = node.materials + node.producing * minutesRemaining
                        var robotsBuildable = 0
                        for (robotId in RobotType.entries) {
                            if (costs[robotId].allLessEqual(maxMaterials)) {
                                robotsBuildable += 1
                            }
                        }

                        if (robotsBuildable > 0) {
                            yield(Pair(Node(newMaterials, node.producing), minutesRemaining))
                        }
                    }
                }
            }

            val traversal = ShortestPaths(::nextEdges)
            traversal.startFrom(Node(Costs(), Costs(1)))
            for (node in traversal) {
                if (traversal.depth == minutes - 1) {
                    return node.materials.geode + node.producing.geode
                }
            }

            error("Should not reach")
        }

        private fun max(first: Costs, other: Costs): Costs {
            return Costs(kotlin.math.max(first.ore, other.ore), kotlin.math.max(first.clay, other.clay), kotlin.math.max(first.obsidian, other.obsidian), kotlin.math.max(first.geode, other.geode))
        }

        operator fun <T>List<T>.get(type: RobotType): T {
            return get(type.idx)
        }
    }

    private data class Node(val materials: Costs, val producing: Costs)
}

class Part19B : Part19A() {
    override fun compute(): Int {
        val geodes = mutableListOf<Int>()
        for (i in 0..<min(2, blueprints.size)) {
            val blueprint = blueprints[i]
            geodes.add(blueprint.produce(32))
        }

        return geodes.reduce(Int::times)
    }

    override fun getExampleAnswer(): Int {
        return 3472
    }
}

fun main() {
    Day(2022, 19, Part19A(), Part19B())
}