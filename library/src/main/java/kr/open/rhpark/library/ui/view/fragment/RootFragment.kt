package kr.open.rhpark.library.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.Fragment
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.permission.PermissionManager
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

    /************************
     *   Permission Check   *
     ************************/
    private val permission = PermissionManager()

    /**
     * SystemAlertPermission 처리를 위함.
     */
    private val requestPermissionAlertWindowLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Logx.d("requestPermissionAlertWindowLauncher ${Settings.canDrawOverlays(this.requireContext())}")
            if (result.resultCode == RESULT_OK) { }
        }

    /**
     * SystemAlertPermission 제외한 권한 처리를 위함.
     */
    private val requestPermissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            Logx.d("requestPermissionLauncher ${permissions}")
            permission.result(this.requireContext(), permissions)
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        onResult: ((deniedPermissions: List<String>) -> Unit)
    ) {
        permission.request(
            this.requireContext(),
            requestPermissionLauncher,
            requestPermissionAlertWindowLauncher,
            permissions,
            onResult)
    }


    public fun hasPermission(permission: String): Boolean =
        requireContext().hasPermission(permission)

    public fun hasPermissions(vararg permissions: String): Boolean =
        requireContext().hasPermissions(*permissions)

    public fun remainPermissions(permissions: List<String>): List<String> =
        requireContext().remainPermissions(permissions)
}