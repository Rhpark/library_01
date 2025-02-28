package kr.open.rhpark.library.system.permission

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.app.ActivityCompat
import kr.open.rhpark.library.debug.logcat.Logx

public class PermissionManagerForActivity(private val activity: AppCompatActivity):PermissionManagerBase() {

    private val requestPermissionAlertWindowLauncher: ActivityResultLauncher<Intent>  =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Logx.d("RESULT_OK SYSTEM_ALERT_WINDOW")
            }
        }

    /**
     * Requests the specified permissions from the user.
     * 사용자에게 지정된 권한을 요청.
     *
     * @param requestCode requestCode
     * @param permissions The list of permissions to request.
     * @param onResult The callback to be invoked when permissions result.
     *
     * @param requestCode requestCode.
     * @param permissions 요청할 권한 목록.
     * @param onResult 권한 결과 콜백
     */
    public fun requestPermissions(
        requestCode: Int = PERMISSION_REQUEST_CODE,
        permissions: List<String>,
        onResult: ((requestCode:Int, deniedPermissions: List<String>) -> Unit)
    ) {
        val permission = PermissionCheck(activity, requestCode, permissions, onResult)

        val remainPermissions = permission.getRemainPermissions()

        if(remainPermissions.isNotEmpty()) {
            if(permission.isRequestPermissionSystemAlertWindow()) {
                requestPermissionAlertWindowLauncher.launch(permission.requestPermissionAlertWindow(activity.packageName))
            }
            ActivityCompat.requestPermissions(activity, remainPermissions.toTypedArray(), requestCode)
            requestPermissionList.add(permission)
        } else {
            onResult(requestCode, emptyList())
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
    public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val data = requestPermissionList.find {
            it.requestCode == requestCode && it.getRemainPermissions() == permissions.toList()
        }
        data?.let {
            it.resultPermissionsFromActivity(permissions,grantResults)
            requestPermissionList.remove(it)
        }
    }
}