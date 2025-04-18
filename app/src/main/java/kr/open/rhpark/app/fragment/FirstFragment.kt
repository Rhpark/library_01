package kr.open.rhpark.app.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.FragmentFirstBinding
import kr.open.rhpark.app.fragment.dialog.FragmentDialogType
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.view.fragment.BaseBindingFragment
import kr.open.rhpark.library.util.extensions.ui.view.snackBarShowShort
import kr.open.rhpark.library.util.extensions.ui.view.toastShowShort

class FirstFragment: BaseBindingFragment<FragmentFirstBinding>(R.layout.fragment_first) {

    private val vm: FirstFragmentVm by lazy { getViewModel<FirstFragmentVm>() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logx.d()
        binding.vm = vm

        lifecycleScope.launch {
            vm.mEventVm.collect{
                when (it) {
                    is FirstFragmentVmEvent.OnPermissionCheck -> {
                        requestPermissions(it.permissionList) {deniedPermissions ->
                            Logx.d("deniedPermissions $deniedPermissions")
                        }
                    }
                    is FirstFragmentVmEvent.OnShowSnackBar -> {
                        snackBarShowShort(it.msg)
                    }
                    is FirstFragmentVmEvent.OnShowToast -> {
                        toastShowShort(it.msg)
                    }

                    is FirstFragmentVmEvent.OnShowDialog -> {
                        showDialog(it.title)
                    }
                }
            }
        }
    }

    private fun showDialog(title:String) {
        val dlg = FragmentDialogType(title)
        dlg.safeShow(childFragmentManager,"FragmentDialogType")
    }
}