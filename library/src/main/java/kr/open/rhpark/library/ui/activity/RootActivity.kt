package kr.open.rhpark.library.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import kr.open.rhpark.library.system.permission.PermissionListener
import kr.open.rhpark.library.system.service.SystemServiceManagerInfo
import kr.open.rhpark.library.ui.view.snackbar.DefaultSnackBar
import kr.open.rhpark.library.ui.view.toast.DefaultToast

public abstract class RootActivity : AppCompatActivity() {

    protected val toast: DefaultToast by lazy { DefaultToast(this) }
    protected val snackBar: DefaultSnackBar by lazy { DefaultSnackBar(window.decorView.rootView) }


    protected val systemServiceManagerInfo: SystemServiceManagerInfo by lazy { SystemServiceManagerInfo(this) }

    private var permissionListener: PermissionListener? = null

    protected fun requestPermissions(
        permissions: List<String>,
        onAllPermissionGranted: () -> Unit,
        onPermissionsDenied: (deniedPermissions: List<String>) -> Unit
    ) {
        permissionListener = PermissionListener(onAllPermissionGranted,onPermissionsDenied)

        val permissionsToRequest = permissions.filter {
            ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if(permissionsToRequest.isNullOrEmpty()) {
            permissionListener?.let { it.onAllPermissionGranted() }
        } else {
            ActivityCompat.requestPermissions(this, permissionsToRequest, PermissionListener.PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionListener.PERMISSION_REQUEST_CODE) {
            val allPermissionsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (allPermissionsGranted) {
                permissionListener?.let { it.onAllPermissionGranted() }
            } else {
                val resDeniedPermission = permissions.filterIndexed { index, _ ->
                    grantResults[index] != PackageManager.PERMISSION_GRANTED
                }
                permissionListener?.let { it.onPermissionsDenied(resDeniedPermission)   }
            }
            permissionListener = null
        }
    }

    protected fun startActivity(activity: Class<*>?) { startActivity(Intent(this, activity))    }

    protected fun startActivity(activity: Class<*>?, vararg intentFlags: Int) {

        val intent = Intent(this, activity).apply { intentFlags.forEach { addFlags(it) } }

        startActivity(intent)
    }

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