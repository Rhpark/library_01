package kr.open.rhpark.library.system.permission

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.Fragment
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.system.permission.PermissionManagerBase.Companion.PERMISSION_REQUEST_CODE

public class PermissionManagerForFragment(private val fragment: Fragment) {
    private val requestPermissionList: MutableList<PermissionCheck> = mutableListOf()

    /**
     * The activity result launcher for requesting permissions.
     * 권한을 요청하기 위한 액티비티 결과.
     */
    private val requestPermissionLauncher : ActivityResultLauncher<Array<String>> =
        fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val data = requestPermissionList.find { it.getRemainPermissions() == permissions.map { it.key}.toList() }
            data?.let {
                it.resultPermissionFromFragment(permissions)
                Logx.d("permissions remove ${it.toString()}")
                requestPermissionList.remove(it)
            }?: Logx.d("can not find permissions Data,")
        }

    private val requestPermissionAlertWindowLauncher: ActivityResultLauncher<Intent> =
        fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Logx.d("RESULT_OK SYSTEM_ALERT_WINDOW")
            }
        }


    /**
     * Requests the specified permissions from the user.
     * 사용자에게 지정된 권한을 요청.
     *
     * @param permissions The list of permissions to request.
     * @param onDenied The callback to be invoked when permissions result.
     *
     * @param permissions 요청할 권한 목록.
     * @param onDenied 권한 결과 콜백
     */
    public fun requestPermissions(
        requestCode: Int,
        permissions: List<String>,
        onDenied: ((requestCode:Int, deniedPermissions: List<String>) -> Unit)
    ) {
        Logx.d("permissions $permissions")
        val permission = PermissionCheck(fragment.requireContext(), requestCode, permissions, onDenied)

        val requestPermissionsList = permission.getRemainPermissions()
        Logx.d("requestPermissionsList ${requestPermissionsList.toList()}")

        if(requestPermissionsList.isNotEmpty()) {
            if(permission.isRequestPermissionSystemAlertWindow()) {
                requestPermissionAlertWindowLauncher.launch(
                    permission.requestPermissionAlertWindow(fragment.requireActivity().packageName)
                )
            }
            requestPermissionLauncher.launch(requestPermissionsList.toTypedArray())
            requestPermissionList.add(permission)
        } else {
            onDenied(requestCode, emptyList())
        }
    }

    public fun requestPermissions(
        permissions: List<String>,
        onDenied: ((requestCode:Int, deniedPermissions: List<String>) -> Unit)
    ) {
        requestPermissions(PERMISSION_REQUEST_CODE, permissions, onDenied)
    }
}