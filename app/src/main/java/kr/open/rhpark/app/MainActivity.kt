package kr.open.rhpark.app


import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kr.open.rhpark.app.databinding.ActivityMainBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.view.activity.BaseBindingActivity
import kr.open.rhpark.library.util.extensions.context.startActivity
import kr.open.rhpark.library.util.extensions.debug.logxD
import kr.open.rhpark.library.util.extensions.debug.logxJ
import kr.open.rhpark.library.util.extensions.debug.logxP
import kr.open.rhpark.library.util.extensions.conditional.ifEquals
import kr.open.rhpark.library.util.extensions.conditional.ifGreaterThan
import kr.open.rhpark.library.util.extensions.conditional.ifGreaterThanOrEqual
import kr.open.rhpark.library.util.extensions.conditional.ifTrue

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
            requestPermissions( event.permissionList)  { deniedPermissionList ->
                Logx.d("deniedPermissionList $deniedPermissionList")
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

        Logx.j("{{{ HELLO }}}")
        "{{{}}}".logxJ("Hello")


        2.ifGreaterThan(1){
            Logx.d("is positive")
        }
        2.ifGreaterThan(2,
            positiveWork = {
                Logx.d("is positive")
            }, negativeWork = {
                Logx.d("is negative")
            }
        )

        2L.ifGreaterThanOrEqual(2L){
            Logx.d("is positive")
        }
        2L.ifGreaterThanOrEqual(2L,
            positiveWork = {
                Logx.d("is positive")
            },
            negativeWork = {
                Logx.d("is negative")
            }
        )

        1.1.ifEquals(.1){
            Logx.d("is positive")
        }

        0.1.ifEquals(1.1,
            positiveWork = {
                Logx.d("is positive")
            },
            negativeWork = {
                Logx.d("is negative")
            }
        )

        true.ifTrue {
            Logx.d("is positive")
        }
    }
}