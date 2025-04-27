package kr.open.rhpark.library.util.extensions.time

public inline fun measureTimeMillis(block: () -> Unit): Long {
    return measureTime(System::currentTimeMillis, block)
}

public inline fun <T> measureTimeMillis(block: () -> T): Pair<T, Long> {
    return measureTimeWithResult(System::currentTimeMillis, block)
}

public inline fun measureTimeNanos(block: () -> Unit): Long {
    return measureTime(System::nanoTime, block)
}

public inline fun <T> measureTimeNanos(block: () -> T): Pair<T, Long> {
    return measureTimeWithResult(System::nanoTime, block)
}

public inline fun measureTime(timeProvider: () -> Long, block: () -> Unit): Long {
    val start = timeProvider()
    block()
    return timeProvider() - start
}

public inline fun <T> measureTimeWithResult(timeProvider: () -> Long, block: () -> T): Pair<T, Long> {
    val start = timeProvider()
    val result = block()
    return Pair(result, timeProvider() - start)
}