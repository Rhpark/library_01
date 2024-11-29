package kr.open.rhpark.app


import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import kr.open.rhpark.app.databinding.ActivityMainBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.activity.BaseBindingActivity
import kr.open.rhpark.library.util.extensions.debug.logxD
import kr.open.rhpark.library.util.extensions.debug.logxJ

class MainActivity : BaseBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val vm: MainActivityVm by lazy { getViewModel<MainActivityVm>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm
        test()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                vm.eventVm.collect {
                    eventVM(it)
                }
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
        Logx.d("Tag","mmssgg")
        Logx.p()
        Logx.t()
        "[[[{fase}]]]".logxJ()
        "gfas".logxD()
        "gfas".logxD("fsae")
        "a".toIntOrNull()
    }
}