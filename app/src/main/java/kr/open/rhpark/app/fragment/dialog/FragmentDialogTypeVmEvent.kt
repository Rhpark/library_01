package kr.open.rhpark.app.fragment.dialog

sealed class FragmentDialogTypeVmEvent {
    data class OnDismiss(val isSave:Boolean):FragmentDialogTypeVmEvent()
}