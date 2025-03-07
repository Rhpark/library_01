package kr.open.rhpark.library.util.extensions.time

public inline fun measureTimeMillis(block: () -> Unit): Long {
    val start = System.currentTimeMillis()
    block()
    return System.currentTimeMillis() - start
}

public inline fun <T> measureTimeMillis(block: () -> T): Pair<T, Long> {
    val start = System.currentTimeMillis()
    val result = block()
    return Pair(result, System.currentTimeMillis() - start)
}

public inline fun measureTimeNanos(block: () -> Unit): Long {
    val start = System.nanoTime()
    block()
    return System.nanoTime() - start
}

public inline fun <T> measureTimeNanos(block: () -> T): Pair<T, Long> {
    val start = System.nanoTime()
    val result = block()
    return Pair(result, System.nanoTime() - start)
}

