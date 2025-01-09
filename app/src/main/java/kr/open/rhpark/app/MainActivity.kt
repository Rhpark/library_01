package kr.open.rhpark.app


import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.open.rhpark.app.databinding.ActivityMainBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.activity.BaseBindingActivity
import kr.open.rhpark.library.util.extensions.debug.logxD
import kr.open.rhpark.library.util.extensions.debug.logxJ
import kr.open.rhpark.library.util.extensions.debug.logxP

class MainActivity : BaseBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val vm: MainActivityVm by lazy { getViewModel<MainActivityVm>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm
        test()

        lifecycleScope.launch {
            vm.mEventVm.collect {
                eventVM(it)
            }
        }
    }

    private fun eventVM(event: MainActivityVmEvent) = when (event) {
        is MainActivityVmEvent.OnPermissionCheck -> {
            requestPermissions(1231, event.permissionList) { requestCode, deniedPermissionList ->
                Logx.d("requestCode $requestCode, deniedPermissionList $deniedPermissionList")
            }
        }
        is MainActivityVmEvent.OnShowActivity -> startActivity(event.activity)
    }


    fun test() {
        Logx.d()
        Logx.d("msg")
        Logx.d("TagLogx","Logx Msg")
        "".logxD()
        "Msg Ext".logxD("")
        "Msg Ext".logxD("TagLogx")

        Logx.p()
        Logx.p("Msg")
        Logx.p("TagLogx","Msg")
        "".logxP()
        "Msg Ext".logxP("TagLogx")

        Logx.j("{{{}}}")
        "{{{}}}".logxJ("Hello")
    }
}