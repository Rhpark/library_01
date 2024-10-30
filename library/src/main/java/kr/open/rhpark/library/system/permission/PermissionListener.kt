package kr.open.rhpark.library.system.permission

/**
 * A data class that holdscallbacks for permission request results.
 * 권한 요청 결과에 대한 콜백을 보유하는 데이터 클래스.
 *
 * This class is used to handle the results of a permission request initiated
 * using the `requestPermissions` method of an `Activity` or `Fragment`.
 *
 * @property onAllPermissionGranted A callback function that is invoked when
 * all requested permissions havebeen granted.
 * @property onPermissionsDenied A callback function that is invoked when
 * one or more requested permissions have been denied. This callback receives
 * a list of the denied permissions.
 */
public data class PermissionListener(
    val onAllPermissionGranted: () -> Unit,
    val onPermissionsDenied: (deniedPermissions: List<String>) -> Unit
) {
    /**
     * A companion object that defines a constant for the permission request code.
     *
     * This constant can be used as the request code when calling the
     * `requestPermissions` method.
     * */
    public companion object {
        public const val PERMISSION_REQUEST_CODE: Int = 1001
    }
}