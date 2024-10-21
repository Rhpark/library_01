package kr.open.rhpark.library.system.permission

public data class PermissionListener(
    val onAllPermissionGranted: () -> Unit,
    val onPermissionsDenied: (deniedPermissions: List<String>) -> Unit
) {
    public companion object {
        public const val PERMISSION_REQUEST_CODE: Int = 1001
    }
}