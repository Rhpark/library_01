package kr.open.rhpark.app.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.FragmentFirstBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.fragment.BaseBindingFragment

class FirstFragment:BaseBindingFragment<FragmentFirstBinding>(R.layout.fragment_first) {

    private val vm: FirstFragmentVm by lazy { getViewModel<FirstFragmentVm>() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logx.d()
        binding.vm = vm
        lifecycleScope.launch {
            vm.eventVm.collect{
                when (it) {
                    is FirstFragmentVmEvent.OnPermissionCheck -> {
                        requestPermissions(it.permissionList) { grantedPermissions, deniedPermissions ->
                            Logx.d("grantedPermissions $grantedPermissions, \n deniedPermissions $deniedPermissions")
                        }
                    }
                    is FirstFragmentVmEvent.OnShowSnackBar -> {
                        snackBar.showMsgShort(binding.btnTestToastShow, it.msg)
                    }
                    is FirstFragmentVmEvent.OnShowToast -> {
                        toast.showMsgShort(it.msg)
                    }
                }
            }
        }
    }
}