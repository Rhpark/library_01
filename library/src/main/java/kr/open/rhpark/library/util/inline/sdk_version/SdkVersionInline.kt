package kr.open.rhpark.library.util.inline.sdk_version

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

/**
 * Build.VERSION.SDK_INT >= ver
 */
@ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
public inline fun checkSdkVersion(ver: Int, doWork: () -> Unit) {
    if (Build.VERSION.SDK_INT >= ver) {
        doWork()
    }
}

/**
 * Build.VERSION.SDK_INT >= ver
 */
@ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
public inline fun <T>checkSdkVersion(ver: Int, doWork: () -> T): T? {
    if (Build.VERSION.SDK_INT >= ver) {
        return doWork()
    }
    return null
}

/**
 * Build.VERSION.SDK_INT >= ver
 */
@ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
public inline fun <T> checkSdkVersion(ver: Int, positiveWork: () -> T, negativeWork: () -> T): T {
    return if (Build.VERSION.SDK_INT >= ver) {
        positiveWork()
    } else {
        negativeWork()
    }
}
