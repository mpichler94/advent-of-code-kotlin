package at.mpichler.aoc.lib

import java.util.PriorityQueue


/**
 * Traversal through a graph.
 * To perform a traversal, set the start node(s) and call [goTo] to start the
 * traversal. After that the path and depth can be queried.
 *
 * The traversal algorithm is implemented by the subclass implementing this
 * class.
 *
 * @param T Type of the nodes
 */
abstract class Traversal<T> : Iterable<T> {
    private var starts = listOf<T>()
    private var end: T? = null
    protected var cameFrom = mutableMapOf<T, T?>()
        private set
    protected var finished = false
    protected var currentNode: T? = null
    val depth
        get() = getPaths().minOf { it.size } - 1
    val visited
        get() = cameFrom.keys

    fun startFrom(start: T): Traversal<T> {
        this.starts = listOf(start)

        cameFrom = mutableMapOf()
        init(starts)

        return this
    }

    fun startFrom(start: List<T>): Traversal<T> {
        this.starts = start

        cameFrom = mutableMapOf()
        init(starts)

        return this
    }

    fun goTo(end: T): Traversal<T> {
        check(starts.isNotEmpty())

        traverse(end)
        this.end = end
        return this
    }

    abstract fun init(starts: List<T>)
    abstract fun traverse(end: T): Map<T, T?>
    abstract fun advance(): T

    private fun getPath(): List<T> {
        checkNotNull(end) { "No traversal has been made. Call 'goTo' first. " }

        return getPath(starts.first(), end!!)
    }

    private fun getPaths(): List<List<T>> {
        checkNotNull(currentNode) { "No traversal has been started. Call 'startFrom' first. " }

        return starts.map { getPath(it, currentNode!!) }
    }

    private fun getPath(start: T, end: T): List<T> {
        var current: T = end
        val path = mutableListOf<T>()

        while (current != start) {
            path.add(current)
            current = cameFrom[current] ?: break
        }

        path.add(start)
        path.reverse()
        return path
    }

    override fun iterator(): Iterator<T> {
        return PathIterator(this)
    }

    private class PathIterator<T>(private val traversal: Traversal<T>) : Iterator<T> {
        override fun hasNext(): Boolean {
            return !traversal.finished
        }

        override fun next(): T {
            return traversal.advance()
        }

    }
}

/**
 * Breadth first traversal through a graph.
 *
 * Movement costs and heuristics cannot be considered
 *
 * @param T Type of the nodes
 * @property nextEdges Function returning the neighbors of a node in the tree
 */
class BreadthFirst<T>(private val nextEdges: (node: T, traversal: BreadthFirst<T>) -> Iterable<T>) : Traversal<T>() {
    private lateinit var todo: ArrayDeque<T>

    override fun init(starts: List<T>) {
        check(starts.isNotEmpty())

        todo = ArrayDeque()
        cameFrom.putAll(starts.map { Pair(it, null) })
        todo.addAll(starts)
    }

    override fun traverse(end: T): Map<T, T?> {
        while (todo.isNotEmpty()) {
            val current = todo.removeFirst()

            if (current == end) {
                break
            }

            for (next in nextEdges(current, this)) {
                if (next !in cameFrom) {
                    todo.addLast(next)
                    cameFrom[next] = current
                }
            }
        }

        currentNode = end
        finished = true
        return cameFrom
    }

    override fun advance(): T {
        val current = todo.removeFirst()

        for (next in nextEdges(current, this)) {
            if (next !in cameFrom) {
                todo.addLast(next)
                cameFrom[next] = current
            }
        }

        finished = todo.isEmpty()

        currentNode = current
        return current
    }
}

/**
 * Search for the shortest path in a graph. The class uses the Dijkstra algorithm
 * to consider the moving costs for each step.
 * @property nextEdges function returning the next nodes with their weights
 */
class ShortestPaths<T>(val nextEdges: (node: T, traversal: ShortestPaths<T>) -> Sequence<Pair<T, Int>>) : Traversal<T>() {
    private lateinit var todo: PriorityQueue<Pair<T, Int>>
    private lateinit var costSoFar: MutableMap<T, Int>
    val distance
        get() = costSoFar[currentNode] ?: 0

    override fun init(starts: List<T>) {
        todo = PriorityQueue<Pair<T, Int>> { l, r -> l.second.compareTo(r.second) }
        costSoFar = mutableMapOf()

        cameFrom.putAll(starts.map { Pair(it, null) })
        costSoFar.putAll(starts.map { Pair(it, 0) })
        todo.addAll(starts.map { Pair(it, 0) })
    }

    override fun traverse(end: T): Map<T, T?> {
        while (todo.isNotEmpty()) {
            val (current, cost) = todo.poll()!!

            if (current == end) {
                break
            }

            for ((next, nextCost) in nextEdges(current, this)) {
                val newCost = cost + nextCost
                if (next !in cameFrom || costSoFar[next] == null || newCost < costSoFar[next]!!) {
                    costSoFar[next] = newCost
                    todo.add(Pair(next, newCost))
                    cameFrom[next] = current
                }
            }
        }

        currentNode = end
        return cameFrom
    }

    override fun advance(): T {
        val (current, cost) = todo.poll()!!
        currentNode = current

        for ((next, nextCost) in nextEdges(current, this)) {
            val newCost = cost + nextCost
            if (next !in cameFrom || costSoFar[next] == null || newCost < costSoFar[next]!!) {
                costSoFar[next] = newCost
                todo.add(Pair(next, newCost))
                cameFrom[next] = current
            }
        }

        finished = todo.isEmpty()

        return current
    }
}


/**
 * Search for the shortest path in a graph. The class uses the Dijkstra algorithm
 * to consider the moving costs for each step.
 * @property nextEdges function returning the next nodes with their weights for
 * a given node
 * @property heuristic A heuristic function that estimates the cost of the
 * cheapest path from a given node to the goal and never overestimates the
 * actual needed costs ("admissible heuristic function").
 */
class AStar<T>(val nextEdges: (T, AStar<T>) -> Sequence<Pair<T, Int>>, val heuristic: (node: T) -> Int) : Traversal<T>() {
    private lateinit var todo: PriorityQueue<Pair<T, Int>>
    private lateinit var costSoFar: MutableMap<T, Int>
    val distance
        get() = costSoFar[currentNode] ?: 0

    override fun init(starts: List<T>) {
        todo = PriorityQueue<Pair<T, Int>> { l, r -> l.second.compareTo(r.second) }
        costSoFar = mutableMapOf()

        cameFrom.putAll(starts.map { Pair(it, null) })
        costSoFar.putAll(starts.map { Pair(it, 0) })
        todo.addAll(starts.map { Pair(it, 0) })
    }

    override fun traverse(end: T): Map<T, T?> {
        while (todo.isNotEmpty()) {
            val (current, cost) = todo.poll()!!

            if (current == end) {
                break
            }

            for ((next, nextCost) in nextEdges(current, this)) {
                val newCost = cost + nextCost
                if (next !in cameFrom || costSoFar[next] == null || newCost < costSoFar[next]!!) {
                    costSoFar[next] = newCost
                    val priority = newCost + heuristic(next)
                    todo.add(Pair(next, priority))
                    cameFrom[next] = current
                }
            }
        }

        currentNode = end
        return cameFrom
    }

    override fun advance(): T {
        val (current, _) = todo.poll()!!
        currentNode = current

        for ((next, nextCost) in nextEdges(current, this)) {
            val newCost = costSoFar[current]!! + nextCost
            if (next !in costSoFar ||  newCost < costSoFar[next]!!) {
                costSoFar[next] = newCost
                val priority = newCost + heuristic(next)
                todo.add(Pair(next, priority))
                cameFrom[next] = current
            }
        }

        finished = todo.isEmpty()

        return current
    }
}
