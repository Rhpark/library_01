package kr.open.rhpark.library.ui.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.permission.PermissionManagerBase
import kr.open.rhpark.library.ui.permission.PermissionManagerForFragment
import kr.open.rhpark.library.util.extensions.context.hasPermission
import kr.open.rhpark.library.util.extensions.context.hasPermissions
import kr.open.rhpark.library.util.extensions.context.remainPermissions

/**
 * A base fragment classthat provides common functionality for fragments.
 * 프래그먼트에 대한 공통 기능을 제공하는 기본 프래그먼트 클래스.
 *
 * This class handles tasks such as:
 * - Managing system service information.* - Requesting permissions.
 * - Starting activities.
 *
 * 이 클래스는 다음과 같은 작업을 처리한다.
 * - 시스템 서비스 정보 관리.
 * - 권한 요청.
 * - 액티비티 시작.
 */
public abstract class RootFragment : Fragment() {

    /**
     * The PermissionCheck for handling permission request results.
     * 권한 요청 결과를 처리하기 위한 PermissionCheck.
     */
    private lateinit var permissionManager: PermissionManagerForFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionManager = PermissionManagerForFragment(this)
    }

    /**
     * Requests the specified permissions from the user.
     * 사용자에게 지정된 권한을 요청.
     *
     * @param permissions The list of permissions to request.
     * @param onResult The callback to be invoked when permissions result.
     *
     * @param permissions 요청할 권한 목록.
     * @param onResult 권한 결과 콜백
     */
    protected fun requestPermissions(
        permissions: List<String>,
        onResult: ((requestCode:Int, deniedPermissions: List<String>) -> Unit),
    ) {
        Logx.d("permissions $permissions")
        permissionManager.requestPermissions(PermissionManagerBase.PERMISSION_REQUEST_CODE, permissions, onResult)
    }

    protected fun requestPermissions(
        requestCode: Int,
        permissions: List<String>,
        onResult: ((requestCode: Int, deniedPermissions: List<String>) -> Unit)
    ) {
        permissionManager.requestPermissions(requestCode, permissions, onResult)
    }


    public fun hasPermission(permission: String): Boolean = requireContext().hasPermission(permission)

    public fun hasPermissions(vararg permissions: String): Boolean = requireContext().hasPermissions(*permissions)

    public fun remainPermissions(permissions: List<String>): List<String> = requireContext().remainPermissions(permissions)
}