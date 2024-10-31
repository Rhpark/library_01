package kr.open.rhpark.library.ui.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.permission.PermissionListener
import kr.open.rhpark.library.system.service.SystemServiceManager
import kr.open.rhpark.library.ui.view.snackbar.DefaultSnackBar
import kr.open.rhpark.library.ui.view.toast.DefaultToast

/**
 * A base fragment classthat provides common functionality for fragments.
 * 프래그먼트에 대한 공통 기능을 제공하는 기본 프래그먼트 클래스.
 *
 * This class handles tasks such as:
 * - Displaying toast messages and snackbars.
 * - Managing system service information.* - Requesting permissions.
 * - Starting activities.
 *
 * 이 클래스는 다음과 같은 작업을 처리한다.
 * - 토스트 메시지 및 스낵바 표시.
 * - 시스템 서비스 정보 관리.
 * - 권한 요청.
 * - 액티비티 시작.
 */
public abstract class RootFragment : Fragment() {

    /**
     * A toast object for displaying short messages to the user.
     * 사용자에게 메시지를 표시하기 위한 토스트 객체.
     */
    protected val toast: DefaultToast by lazy { DefaultToast(requireContext()) }

    /**
     * A snackbar object for displaying brief messages of the screen.
     * 화면에 간단한 메시지를 표시하기 위한 스낵바 객체입니다.
     */
    protected val snackBar: DefaultSnackBar by lazy { DefaultSnackBar(requireView()) }

    /**
     * A system service manager info object for accessing system services.
     * 시스템 서비스에 액세스하기 위한 시스템 서비스 관리자 정보 객체입.
     */
    protected val systemServiceManagerInfo: SystemServiceManager by lazy { SystemServiceManager(requireContext()) }

    /**
     * The permission listener for handling permission request results.
     * 권한 요청 결과를 처리하기 위한 권한 리스너.
     */
    private var permissionListener: PermissionListener? = null

    /**
     * The activity result launcher for requesting permissions.
     * 권한을 요청하기 위한 액티비티 결과.
     */
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allPermissionsGranted = permissions.all { it.value }

            Logx.d("allPermissionsGranted $allPermissionsGranted")
            if (allPermissionsGranted) {
                permissionListener?.let { it.onAllPermissionGranted() }
            } else {
                permissionListener?.let { it.onPermissionsDenied(permissions.keys.toList()) }
            }
            permissionListener = null
        }

    /**
     * Requests the specified permissions from the user.
     * 사용자에게 지정된 권한을 요청.
     *
     * @param permissions The list of permissions to request.
     * @param onAllPermissionGranted The callback to be invoked when all permissions are granted.
     * @param onPermissionsDenied The callback to be invoked when one or more permissions are denied.
     *
     * @param permissions 요청할 권한 목록.
     * @param onAllPermissionGranted 모든 권한이 부여되었을 때 호출될 콜백.
     * @param onPermissionsDenied 하나 이상의 권한이 거부되었을 때 호출될 콜백.
     */
    protected fun requestPermissions(
        permissions: List<String>,
        onAllPermissionGranted: () -> Unit,
        onPermissionsDenied: (deniedPermissions: List<String>) -> Unit
    ) {
        Logx.d("permissions $permissions")
        permissionListener = PermissionListener(onAllPermissionGranted, onPermissionsDenied)

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if(permissionsToRequest.isEmpty()) {
            permissionListener?.let { it.onAllPermissionGranted() }
        } else {
            requestPermissionLauncher.launch(permissionsToRequest)
        }
    }

    /**
     * Starts an activity with the specified class.
     * 지정된 클래스의 액티비티를 시작합니다.
     *
     * @param activity The class of the activity to start.
     *
     * @param activity 시작할 액티비티의 클래스입니다.
     */
    protected fun startActivity(activity: Class<*>?) { startActivity(Intent(requireContext(), activity))    }

    /**
     * Starts an activity with the specified class and intent flags.
     * 지정된 클래스 및 인텐트 플래그로 액티비티를 시작.
     *
     * @param activity The class of the activity to start.
     * @param extras extras to include in the intent.
     * @param intentFlags The intent flags to add to the intent.
     *
     * @param activity 시작할 액티비티의 클래스.
     * @param extras 인텐트에 포함할 Bundle.
     * @param intentFlags 인텐트에 추가할 인텐트 플래그.
     */
    protected fun startActivity(activity: Class<*>, extras: Bundle? = null, vararg intentFlags: Int) {
        val intent = Intent(requireContext(), activity).apply {
            extras?.let { putExtras(it) }
            intentFlags.forEach { addFlags(it) }
        }
        startActivity(intent)
    }
}