package kr.open.rhpark.library.util.extensions.collection


public fun <T, K> List<T>.distinctBy(selector: (T) -> K): List<T> {
    val seen = mutableSetOf<K>()
    return filter { seen.add(selector(it)) }
}


public fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    if (index1 in 0 until size && index2 in 0 until size && index1 != index2) {
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }
}