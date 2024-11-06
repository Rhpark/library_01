package kr.open.rhpark.app


import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import kr.open.rhpark.app.activity.display.DisplayActivity
import kr.open.rhpark.app.activity.recyclerview.RecyclerViewActivity
import kr.open.rhpark.app.activity.second.FragmentShowActivity
import kr.open.rhpark.app.activity.toast_snackbar.ToastSnackBarActivity
import kr.open.rhpark.app.activity.vibrator.VibratorActivity
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
                    eventVM(it)
                }
            }
        }
    }

    private fun eventVM(event: MainActivityVmEvent) = when (event) {
        is MainActivityVmEvent.OnPermissionCheck -> {
            requestPermissions(event.permissionList) { grantedPermissions, deniedPermissions ->
                Logx.d("grantedPermissions $grantedPermissions, \n deniedPermissions $deniedPermissions")
            }
        }

        is MainActivityVmEvent.OnShowRecyclerviewActivity -> startActivity(RecyclerViewActivity::class.java)

        is MainActivityVmEvent.OnShowVibratorActivity -> startActivity(VibratorActivity::class.java)

        is MainActivityVmEvent.OnShowWindowActivity -> {
//            startActivity(WindowActivity::class.java)
        }

        is MainActivityVmEvent.OnShowToastSnackBar -> startActivity(ToastSnackBarActivity::class.java)

        is MainActivityVmEvent.OnShowFragmentActivity -> startActivity(FragmentShowActivity::class.java)

        is MainActivityVmEvent.OnDisplayActivity -> startActivity(DisplayActivity::class.java)
    }


    fun test() {
        Logx.d()
        Logx.d("msg")
        Logx.d("Tag","mmssgg")
        Logx.p()
        Logx.t()
    }
}