package kr.open.rhpark.app.fragment.dialog

import android.view.View
import kr.open.rhpark.library.ui.viewmodels.BaseViewModelEvent

class FragmentDialogTypeVm : BaseViewModelEvent<FragmentDialogTypeVmEvent>() {

    public fun onClickOk(view: View) {
        sendEventVm(FragmentDialogTypeVmEvent.OnDismiss(true))
    }

    public fun onClickCancel(view: View) {
        sendEventVm(FragmentDialogTypeVmEvent.OnDismiss(false))
    }
}