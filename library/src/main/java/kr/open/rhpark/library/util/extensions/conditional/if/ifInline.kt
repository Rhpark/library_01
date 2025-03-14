package kr.open.rhpark.library.util.extensions.conditional.`if`


/**
 * @param center
 * @param comparison
 *
 * @return center > comparison
 */
public inline fun <T> ifGreaterThan(center: Int, comparison: Int, doWork: () -> T): T? =
    if (center > comparison) { doWork() } else null

/**
 * @param center
 * @param comparison
 *
 * @return center > comparison
 */
public inline fun <T> ifGreaterThan(
    center: Int, comparison: Int, positiveWork: () -> T, negativeWork: () -> T
): T = if (center > comparison) { positiveWork() } else { negativeWork() }


/**
 * @param center
 * @param comparison
 *
 * @return center > comparison
 */
public inline fun <T> ifGreaterThan(center: Float, comparison: Float, doWork: () -> T): T? =
    if (center > comparison) { doWork() } else null

/**
 * @param center
 * @param comparison
 *
 * @return center > comparison
 */
public inline fun <T> ifGreaterThan(
    center: Float, comparison: Float, positiveWork: () -> T, negativeWork: () -> T
): T = if (center > comparison) { positiveWork() } else negativeWork()

/**
 * @param center
 * @param comparison
 *
 * @return center > comparison
 */
public inline fun <T> ifGreaterThan(center: Double, comparison: Double, doWork: () -> T): T? =
    if (center > comparison) { doWork() } else null

/**
 * @param center
 * @param comparison
 *
 * @return center > comparison
 */
public inline fun <T> ifGreaterThan(
    center: Double, comparison: Double, positiveWork: () -> T, negativeWork: () -> T
): T = if (center > comparison) { positiveWork() } else negativeWork()

/**
 * @param center
 * @param comparison
 *
 * @return center > comparison
 */
public inline fun <T> ifGreaterThan(center: Long, comparison: Long, doWork: () -> T): T? =
    if (center > comparison) { doWork() } else null

/**
 * @param center
 * @param comparison
 *
 * @return center > comparison
 */
public inline fun <T> ifGreaterThan(
    center: Long, comparison: Long, positiveWork: () -> T, negativeWork: () -> T
): T = if (center > comparison) { positiveWork() } else negativeWork()


/**
 * @param center
 * @param comparison
 *
 * @return center >= comparison
 */
public inline fun <T> ifGreaterThanOrEqual(center: Int, comparison: Int, doWork: () -> T): T? =
    if (center >= comparison) { doWork() } else null

/**
 * @param center
 * @param comparison
 *
 * @return center >= comparison
 */
public inline fun <T> ifGreaterThanOrEqual(
    center: Int, comparison: Int, positiveWork: () -> T, negativeWork: () -> T
): T = if (center >= comparison) { positiveWork() } else { negativeWork() }


/**
 * @param center
 * @param comparison
 *
 * @return center >= comparison
 */
public inline fun <T> ifGreaterThanOrEqual(center: Float, comparison: Float, doWork: () -> T): T? =
    if (center >= comparison) { doWork() } else null

/**
 * @param center
 * @param comparison
 *
 * @return center >= comparison
 */
public inline fun <T> ifGreaterThanOrEqual(
    center: Float, comparison: Float, positiveWork: () -> T, negativeWork: () -> T
): T = if (center >= comparison) { positiveWork() } else negativeWork()

/**
 * @param center
 * @param comparison
 *
 * @return center >= comparison
 */
public inline fun <T> ifGreaterThanOrEqual(center: Double, comparison: Double, doWork: () -> T): T? =
    if (center >= comparison) { doWork() } else null

/**
 * @param center
 * @param comparison
 *
 * @return center >= comparison
 */
public inline fun <T> ifGreaterThanOrEqual(
    center: Double, comparison: Double, positiveWork: () -> T, negativeWork: () -> T
): T = if (center >= comparison) { positiveWork() } else negativeWork()

/**
 * @param center
 * @param comparison
 *
 * @return center >= comparison
 */
public inline fun <T> ifGreaterThanOrEqual(center: Long, comparison: Long, doWork: () -> T): T? =
    if (center >= comparison) { doWork() } else null

/**
 * @param center
 * @param comparison
 *
 * @return center >= comparison
 */
public inline fun <T> ifGreaterThanOrEqual(
    center: Long, comparison: Long, positiveWork: () -> T, negativeWork: () -> T
): T = if (center >= comparison) { positiveWork() } else negativeWork()

/**
 * @param center
 * @param comparison
 *
 * @return center == comparison
 */
public inline fun <T> ifEquals(center: Int, comparison: Int, doWork: () -> T): T? =
    if (center == comparison) { doWork() } else null

/**
 * @param center
 * @param comparison
 *
 * @return center == comparison
 */
public inline fun <T> ifEquals(
    center: Int, comparison: Int, positiveWork: () -> T, negativeWork: () -> T
): T = if (center == comparison) { positiveWork() } else { negativeWork() }


/**
 * @param center
 * @param comparison
 *
 * @return center == comparison
 */
public inline fun <T> ifEquals(center: Float, comparison: Float, doWork: () -> T): T? =
    if (center == comparison) { doWork() } else null

/**
 * @param center
 * @param comparison
 *
 * @return center == comparison
 */
public inline fun <T> ifEquals(
    center: Float, comparison: Float, positiveWork: () -> T, negativeWork: () -> T
): T = if (center == comparison) { positiveWork() } else negativeWork()

/**
 * @param center
 * @param comparison
 *
 * @return center == comparison
 */
public inline fun <T> ifEquals(center: Double, comparison: Double, doWork: () -> T): T? =
    if (center == comparison) { doWork() } else null

/**
 * @param center
 * @param comparison
 *
 * @return center == comparison
 */
public inline fun <T> ifEquals(
    center: Double, comparison: Double, positiveWork: () -> T, negativeWork: () -> T
): T = if (center == comparison) { positiveWork() } else negativeWork()

/**
 * @param center
 * @param comparison
 *
 * @return center == comparison
 */
public inline fun <T> ifEquals(center: Long, comparison: Long, doWork: () -> T): T? =
    if (center == comparison) { doWork() } else null

/**
 * @param center
 * @param comparison
 *
 * @return center == comparison
 */
public inline fun <T> ifEquals(
    center: Long, comparison: Long, positiveWork: () -> T, negativeWork: () -> T
): T = if (center == comparison) { positiveWork() } else negativeWork()


/**
 * @param center
 * @param comparison
 *
 * @return center == comparison
 */
public inline fun <T> ifNotEquals(center: Int, comparison: Int, doWork: () -> T): T? =
    if (center != comparison) { doWork() } else null



/**
 * @param center
 * @param comparison
 *
 * @return center == comparison
 */
public inline fun <T> ifNotEquals(center: Float, comparison: Float, doWork: () -> T): T? =
    if (center != comparison) { doWork() } else null


/**
 * @param center
 * @param comparison
 *
 * @return center == comparison
 */
public inline fun <T> ifNotEquals(center: Double, comparison: Double, doWork: () -> T): T? =
    if (center != comparison) { doWork() } else null

/**
 * @param center
 * @param comparison
 *
 * @return center == comparison
 */
public inline fun <T> ifNotEquals(center: Long, comparison: Long, doWork: () -> T): T? =
    if (center != comparison) { doWork() } else null
