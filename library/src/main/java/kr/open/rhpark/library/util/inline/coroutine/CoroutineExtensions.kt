package kr.open.rhpark.library.util.inline.coroutine

import kotlinx.coroutines.flow.MutableSharedFlow

// MutableSharedFlow에 안전하게 이벤트 발행하는 확장 함수
public inline fun <T> MutableSharedFlow<T>.safeEmit(value: T, failure: () -> Unit) {
    if (!tryEmit(value)) {
        failure()
    }
}