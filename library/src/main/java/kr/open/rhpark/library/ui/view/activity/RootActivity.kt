package kr.open.rhpark.library.ui.view.activity

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
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.permission.PermissionManager
import kr.open.rhpark.library.util.extensions.conditional.sdk_version.checkSdkVersion

/**
 * A base activity class that provides common functionality for all activities in the application.
 * 애플리케이션의 모든 액티비티에 대한 공통 기능을 제공하는 기본 액티비티 클래스.
 *
 * This class handles tasks such as:
 * - Managing system service information.
 * - Requesting permissions.
 * - Starting activities.
 * - Setting the status bar to transparent.
 * - Managing status and navigation bar colors.
 * - Handling activity results.
 *
 * 이 클래스는 다음과 같은 작업을 처리합니다.
 * - 시스템 서비스 정보 관리.
 * - 권한 요청.
 * - 액티비티 시작.
 * - 상태 표시줄을 투명하게 설정.
 * - 상태 및 네비게이션 바 색상 관리.
 * - 액티비티 결과 처리.
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
            // Result handling is done by PermissionManager
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
                val contentViewHeight = findViewById<View>(android.R.id.content).height
                (rootView.height - contentViewHeight) - statusBarHeight
            }
        )

    /**
     * Override this method to perform initialization before the standard onCreate logic.
     */
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
            checkSdkVersion(Build.VERSION_CODES.R) {
                WindowCompat.setDecorFitsSystemWindows(this, false)
            }
        }
    }

    /**
     * Sets the status bar color.
     * 상태 표시줄 색상 설정.
     *
     * @param color The color to set.
     * @param isLightStatusBar Whether to use light status bar icons.
     */
    protected fun setStatusBarColor(@ColorInt color: Int, isLightStatusBar: Boolean = false) {
        window.apply {
            @Suppress("DEPRECATION")
            statusBarColor = color
            val insetsController = WindowCompat.getInsetsController(this, decorView)
            insetsController.isAppearanceLightStatusBars = isLightStatusBar
        }
    }

    /**
     * Sets the navigation bar color.
     * 네비게이션 바 색상 설정.
     *
     * @param color The color to set.
     * @param isLightNavigationBar Whether to use light navigation bar icons.
     */
    protected fun setNavigationBarColor(
        @ColorInt color: Int,
        isLightNavigationBar: Boolean = false
    ) {
        window.apply {
            @Suppress("DEPRECATION")
            navigationBarColor = color
            val insetsController = WindowCompat.getInsetsController(this, decorView)
            insetsController.isAppearanceLightNavigationBars = isLightNavigationBar
        }
    }

    /**
     * Configures system bars (status and navigation) with the same color.
     * 시스템 바(상태 및 네비게이션)를 동일한 색상으로 설정.
     *
     * @param color The color to set for both status and navigation bars.
     * @param isLightSystemBars Whether to use light system bar icons.
     */
    protected fun setSystemBarsColor(@ColorInt color: Int, isLightSystemBars: Boolean = false) {
        setStatusBarColor(color, isLightSystemBars)
        setNavigationBarColor(color, isLightSystemBars)
    }

    /**
     * Sets the system bar icons to light or dark mode.
     * 시스템 바 아이콘을 라이트 또는 다크 모드로 설정.
     *
     * @param isLightSystemBars True for dark icons (light mode), false for light icons (dark mode).
     */
    protected fun setSystemBarsAppearance(isLightSystemBars: Boolean) {
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.apply {
            isAppearanceLightStatusBars = isLightSystemBars
            isAppearanceLightNavigationBars = isLightSystemBars
        }
    }
}