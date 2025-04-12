package kr.open.rhpark.library.ui.view.activity

import android.R
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.permission.PermissionManager
import kr.open.rhpark.library.util.extensions.conditional.sdk_version.checkSdkVersion

/**
 * A base activity classthat provides common functionality for all activities in the application.
 * 애플리케이션의 모든 액티비티에 대한 공통 기능을 제공하는 기본 액티비티 클래스.
 *
 * This class handles tasks such as:
 * - Managing system service information.
 * - Requesting permissions.
 * - Starting activities.
 * - Setting the status bar to transparent.
 *
 * 이 클래스는 다음과 같은 작업을 처리합니다.
 * - 시스템 서비스 정보 관리.
 * - 권한 요청.
 * - 액티비티 시작.
 * - 상태 표시줄을 투명하게 설정.
 */
public abstract class RootActivity : AppCompatActivity() {

    /************************
     *   Permission Check   *
     ************************/
    private val permission = PermissionManager()

    /**
     * SystemAlertPermission 처리를 위함.
     */
    private val requestPermissionAlertWindowLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Logx.d("requestPermissionAlertWindowLauncher ${Settings.canDrawOverlays(this)}")
            if (result.resultCode == RESULT_OK) { }
        }

    /**
     * SystemAlertPermission 제외한 권한 처리를 위함.
     */
    private val requestPermissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            Logx.d("requestPermissionLauncher ${permissions}")
            permission.result(this, permissions)
        }

    /**
     * 권한 요청 & 결과 확인 메서드
     */
    protected fun requestPermissions(
        permissions: List<String>,
        onResult: ((deniedPermissions: List<String>) -> Unit)
    ) {
        permission.request(
            this,
            requestPermissionLauncher,
            requestPermissionAlertWindowLauncher,
            permissions,
            onResult
        )
    }

    /************************
     *  화면의 특정 부분 높이  *
     ************************/

    public val statusBarHeight: Int
        get() = checkSdkVersion(Build.VERSION_CODES.R,
            positiveWork = {
                window.decorView.getRootWindowInsets()?.getInsets(WindowInsets.Type.statusBars())?.top ?: 0
            }, negativeWork = {
                Rect().apply { window.decorView.getWindowVisibleDisplayFrame(this) }.top
            }
        )

    public val navigationBarHeight: Int
        get() = checkSdkVersion(Build.VERSION_CODES.R,
            positiveWork = {
                window.decorView.getRootWindowInsets()?.getInsets(WindowInsets.Type.navigationBars())?.bottom ?: 0
            },
            negativeWork = {
                val rootView = window.decorView.rootView
                val contentViewHeight = findViewById<View>(R.id.content).height
                (rootView.height - contentViewHeight) - statusBarHeight
            }
        )

    protected open fun beforeOnCreated(savedInstanceState: Bundle?) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeOnCreated(savedInstanceState)
    }


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
            checkSdkVersion(Build.VERSION_CODES.R) {// API 30 이상에 적용
                WindowCompat.setDecorFitsSystemWindows(this, false)
            }
        }
    }
}