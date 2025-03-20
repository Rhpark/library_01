package kr.open.rhpark.library.ui.permission

public abstract class PermissionManagerBase() {
    protected val requestPermissionList: MutableList<PermissionCheck> = mutableListOf()

    /**
     * A companion object that defines a constant for the default permission request code.
     * 권한 요청 코드에 대한 기본값 상수를 정의
     *
     * This constant can be used as the request code when calling the
     * `requestPermissions` method.
     * */
    public companion object {
        public const val PERMISSION_REQUEST_CODE: Int = 7942
    }
}