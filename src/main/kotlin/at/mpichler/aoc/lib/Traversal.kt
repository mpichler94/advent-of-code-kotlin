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
abstract class Traversal<T>: Iterable<T> {
    private var starts = listOf<T>()
    private var end: T? = null
    private var cameFrom = mapOf<T, T?>()
    protected var finished = false
    protected var currentNode: T? = null
    val depth
        get() = getPaths().minOf { it.size }
    val visited
        get() = cameFrom.keys

    fun startFrom(start: T): Traversal<T> {
        this.starts = listOf(start)
        init(starts)
        return this
    }

    fun startFrom(start: List<T>): Traversal<T> {
        this.starts = start
        init(starts)
        return this
    }

    fun goTo(end: T): Traversal<T> {
        check(starts.isNotEmpty())

        cameFrom = traverse(end)
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
        var current: T? = end
        val path = mutableListOf<T>()

        while (current != start && current != null) {
            path.add(current)
            current = cameFrom[current]
        }

        path.add(start)
        path.reverse()
        return path
    }

    override fun iterator(): Iterator<T> {
        return PathIterator(this)
    }

    private class PathIterator<T>(private val traversal: Traversal<T>): Iterator<T> {
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
class BreadthFirst<T>(private val nextEdges: (T, BreadthFirst<T>) -> Iterable<T>) : Traversal<T>() {
    private lateinit var todo: ArrayDeque<T>
    private lateinit var cameFrom: MutableMap<T, T?>


    override fun init(starts: List<T>) {
        check(starts.isNotEmpty())

        todo = ArrayDeque()
        cameFrom = mutableMapOf()
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
        check(todo.isNotEmpty())

        val current = todo.removeFirst()

        for (next in nextEdges(current, this)) {
            if (next !in cameFrom) {
                todo.addLast(next)
                cameFrom[next] = current
            }
        }

        currentNode = current
        return current
    }
}

/**
 * Search for the shortest path in a graph. The class uses the Dijkstra algorithm
 * to consider the moving costs for each step.
 */
class ShortestPaths<T>(val nextEdges: (T, ShortestPaths<T>) -> Sequence<Pair<T, Int>>) : Traversal<T>() {
    private lateinit var todo: PriorityQueue<Pair<T, Int>>
    private lateinit var cameFrom: MutableMap<T, T?>
    private lateinit var costSoFar:  MutableMap<T, Int>
    val distance
        get() = costSoFar[currentNode] ?: 0

    override fun init(starts: List<T>) {
        todo = PriorityQueue<Pair<T, Int>> { l, r -> l.second.compareTo(r.second) }
        cameFrom = mutableMapOf()
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
        check(todo.isNotEmpty())

        val (current, cost) = todo.poll()!!

        for ((next, nextCost) in nextEdges(current, this)) {
            val newCost = cost + nextCost
            if (next !in cameFrom || costSoFar[next] == null || newCost < costSoFar[next]!!) {
                costSoFar[next] = newCost
                todo.add(Pair(next, newCost))
                cameFrom[next] = current
            }
        }

        currentNode = current
        return current
    }
}