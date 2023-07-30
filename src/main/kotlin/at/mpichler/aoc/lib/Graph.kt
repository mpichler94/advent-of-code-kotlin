package at.mpichler.aoc.lib

class Graph {
    private val vertices = mutableMapOf<String, Vertex>()

    private operator fun get(name: String) = vertices[name] ?: throw IllegalArgumentException()

    fun addVertex(name: String) {
        vertices[name] = Vertex(name)
    }

    fun addEdge(first: String, second: String) {
        val v1 = vertices.computeIfAbsent(first) { Vertex(first) }
        val v2 = vertices.computeIfAbsent(second) { Vertex(second) }

        v1.neighbors.add(v2)
        v2.neighbors.add(v1)
    }

    fun addEdges(edges: Iterable<Pair<String, String>>) = edges.forEach { addEdge(it.first, it.second) }

    fun neighbors(name: String) = vertices[name]?.neighbors?.map { it.name } ?: listOf()
}

private data class Vertex(val name: String) {
    val neighbors = mutableSetOf<Vertex>()
}
