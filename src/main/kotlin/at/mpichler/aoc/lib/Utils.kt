package at.mpichler.aoc.lib

fun <T> product(list: Iterable<T>): Iterable<Pair<T, T>> {
    return buildList {
        for (first in list) {
            for (second in list) {
                add(Pair(first, second))
            }
        }
    }
}

fun <S, T> product(list1: Iterable<S>, list2: Iterable<T>): Iterable<Pair<S, T>> {
    return buildList {
        for (first in list1) {
            for (second in list2) {
                add(Pair(first, second))
            }
        }
    }
}

fun <T> List<T>.combinations(): List<Pair<T, T>> = buildList {
    for (first in 0..<this@combinations.size) {
        for (second in first + 1..<this@combinations.size) {
            add(this@combinations[first] to this@combinations[second])
        }
    }
}

