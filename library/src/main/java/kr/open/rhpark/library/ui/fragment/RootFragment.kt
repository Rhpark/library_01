package kr.open.rhpark.library.ui.fragment

import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.permission.PermissionListener
import kr.open.rhpark.library.system.service.SystemServiceManagerInfo
import kr.open.rhpark.library.ui.view.snackbar.DefaultSnackBar
import kr.open.rhpark.library.ui.view.toast.DefaultToast

public abstract class RootFragment : Fragment() {

    protected val toast: DefaultToast by lazy { DefaultToast(requireContext()) }
    protected val snackBar: DefaultSnackBar by lazy { DefaultSnackBar(requireView()) }

    protected val systemServiceManagerInfo: SystemServiceManagerInfo by lazy { SystemServiceManagerInfo(requireContext()) }

    private var permissionListener: PermissionListener? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allPermissionsGranted = permissions.all { it.value }

            Logx.d("allPermissionsGranted $allPermissionsGranted")
            if (allPermissionsGranted) {
                permissionListener?.let { it.onAllPermissionGranted() }
            } else {
                permissionListener?.let { it.onPermissionsDenied(permissions.keys.toList()) }
            }
        }

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

        if(permissionsToRequest.isNullOrEmpty()) {
            permissionListener?.let { it.onAllPermissionGranted() }
        } else {
            requestPermissionLauncher.launch(permissionsToRequest)
        }
    }

    protected fun startActivity(activity: Class<*>?) { startActivity(Intent(requireContext(), activity))    }

    protected fun startActivity(activity: Class<*>?, vararg intentFlags: Int) {

        val intent = Intent(requireContext(), activity).apply { intentFlags.forEach { addFlags(it) } }

        startActivity(intent)
    }
}