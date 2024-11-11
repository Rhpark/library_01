package kr.open.rhpark.library.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.permission.PermissionCheck
import kr.open.rhpark.library.system.service.SystemServiceManager
import kr.open.rhpark.library.ui.view.snackbar.DefaultSnackBar
import kr.open.rhpark.library.ui.view.toast.DefaultToast

/**
 * A base activity classthat provides common functionality for all activities in the application.
 * 애플리케이션의 모든 액티비티에 대한 공통 기능을 제공하는 기본 액티비티 클래스.
 *
 * This class handles tasks such as:
 * - Displaying toast messages and snackbars.
 * - Managing system service information.
 * - Requesting permissions.
 * - Starting activities.
 * - Setting the status bar to transparent.
 *
 * 이 클래스는 다음과 같은 작업을 처리합니다.
 * - 토스트 메시지 및 스낵바 표시.
 * - 시스템 서비스 정보 관리.
 * - 권한 요청.
 * - 액티비티 시작.
 * - 상태 표시줄을 투명하게 설정.
 */
public abstract class RootActivity : AppCompatActivity() {

    /**
     * A toast object for displaying messages to the user.
     * 사용자에게 메시지를 표시하기 위한 토스트 객체.
     */

    protected val toast: DefaultToast by lazy { DefaultToast(this) }

    /**
     * A snackbar object for displaying brief messages of the screen.
     * 화면에 간단한 메시지를 표시하기 위한 스낵바 객체.
     */
    protected val snackBar: DefaultSnackBar by lazy { DefaultSnackBar(window.decorView.rootView) }

    /**
     * A system service manager info object for accessing system services.
     * 시스템 서비스에 액세스하기 위한 시스템 서비스 관리자 정보 객체.
     */
    protected val systemServiceManagerInfo: SystemServiceManager by lazy { SystemServiceManager(this) }

    /**
     * The permission listener for handling permission request results.
     * 권한 요청과 결과를 처리하기 위한 permissionCheck
     */
    private var permission: PermissionCheck? = null

    private val requestPermissionAlertWindowLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Logx.d("RESULT_OK SYSTEM_ALERT_WINDOW")
            }
        }

    /**
     * Requests the specified permissions from the user.
     * 사용자에게 지정된 권한을요청.
     *
     * @param permissions The list of permissions to request.
     * @param onPermissionResult The callback to be invoked when permissions result.
     *
     * @param permissions 요청할 권한 목록.
     * @param onPermissionResult 권한 결과 콜백
     */
    protected fun requestPermissions(
        permissions: List<String>,
        onPermissionResult:(grantedPermissions: List<String>, deniedPermissions: List<String>) ->Unit,
    ) {
        permission =
            PermissionCheck(this, permissions, onPermissionResult)

        permission?.let {
            if(it.isAllGranted()) return

            if(it.isRequestPermissionSystemAlertWindow()) {
                requestPermissionAlertWindowLauncher.launch(it.requestPermissionAlertWindow(packageName))
            }
            val remainRequestPermissions = it.getRemainRequestPermissionList()
            if(remainRequestPermissions.isNotEmpty()) {
                ActivityCompat.requestPermissions(this, remainRequestPermissions, PermissionCheck.PERMISSION_REQUEST_CODE)
            }
        }
    }

    /**
     * Handles the results of a permission request.
     * 권한 요청 결과를 처리.
     *
     * @param requestCode The request code passed to [ActivityCompat.requestPermissions].
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     *
     * @param requestCode [ActivityCompat.requestPermissions]에 전달된 요청 코드.
     * @param permissions 요청된 권한.
     * @param grantResults 해당 권한에 대한 부여 결과.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionCheck.PERMISSION_REQUEST_CODE) {
            permission?.let {
                val grantedList = mutableListOf<String>()
                val deniedList = mutableListOf<String>()
                permissions.forEachIndexed { index, s ->
                    if(grantResults[index] == PackageManager.PERMISSION_GRANTED) grantedList.add(s)
                    else deniedList.add(s)
                }
                it.result(grantedList, deniedList)
            }
            permission = null
        }
    }

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
        val intent = Intent(this, activity).apply {
            extras?.let { putExtras(it) }
            intentFlags.forEach { addFlags(it) }
        }
        startActivity(intent)
    }

    /**
     * Starts an activity with the specified class.
     * 지정된 클래스의 액티비티를 시작.
     *
     * @param activity The class of the activity to start.
     *
     * @param activity 시작할 액티비티의 클래스.
     */
    protected fun startActivity(activity: Class<*>?) { startActivity(Intent(this, activity))    }

    /**
     * Sets the status bar to transparent.
     * 상태 표시줄을 투명하게 설정.
     */
    protected fun setStatusBarTransparent() {
        window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            if(Build.VERSION.SDK_INT >= 30) {	// API 30 에 적용
                WindowCompat.setDecorFitsSystemWindows(window, false)
            }
        }
    }
}