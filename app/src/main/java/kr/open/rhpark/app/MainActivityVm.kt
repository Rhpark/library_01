package kr.open.rhpark.app

import android.Manifest
import android.view.View
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.open.rhpark.library.viewmodels.BaseViewModelEventFlow

class MainActivityVm : BaseViewModelEventFlow<MainActivityVmEvent>() {
    init {
        viewModelScope.launch {

        }
    }

    fun onClickPermission(v: View) {
        sendEvent(
            MainActivityVmEvent.OnPermissionCheck(
                listOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_PHONE_NUMBERS
                )
            )
        )
    }


    fun onClickSnackBar(v: View) {
        sendEvent(MainActivityVmEvent.OnShowSnackBar("Hello SnackBar"))
    }

    fun onClickToast(v: View) {
        sendEvent(MainActivityVmEvent.OnShowToast("Hello Toast"))
    }
}