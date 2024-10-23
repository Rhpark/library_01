package kr.open.rhpark.app


import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import kr.open.rhpark.app.activity.recyclerview.RecyclerViewActivity
import kr.open.rhpark.app.activity.second.SecondActivity
import kr.open.rhpark.app.databinding.ActivityMainBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.activity.BaseBindingActivity

class MainActivity : BaseBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val vm: MainActivityVm by lazy { getViewModel<MainActivityVm>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm
        test()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                vm.eventVm.collect {
                    when (it) {
                        is MainActivityVmEvent.OnPermissionCheck -> {
                            requestPermissions(it.permissionList, {
                                Logx.d("onAllPermissionsGranted")
                            }, { deniedPermissions ->
                                Logx.d("onPermissionsDenied $deniedPermissions")
                            })
                        }
                        is MainActivityVmEvent.OnShowSnackBar -> {
                            snackBar.showShort(binding.btnTestToastShow, it.msg)
                        }
                        is MainActivityVmEvent.OnShowToast -> {
                            toast.showShort(it.msg)
                            startActivity(SecondActivity::class.java, Intent.FLAG_ACTIVITY_NEW_TASK)
                        }

                        is MainActivityVmEvent.OnShowRecyclerviewActivity -> {
                            toast.showShort(it.msg)
                            startActivity(RecyclerViewActivity::class.java, Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                    }
                }
            }
        }
    }


    fun test() {
        Logx.d()
        Logx.d("msg")
        Logx.d("Tag","mmssgg")
        Logx.p()
        Logx.t()
    }
}