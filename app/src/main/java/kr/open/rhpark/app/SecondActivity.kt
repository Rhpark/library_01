package kr.open.rhpark.app


import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import kr.open.rhpark.app.databinding.ActivityMainBinding
import kr.open.rhpark.app.databinding.ActivitySecondBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.activity.BaseBindingActivity

class SecondActivity : BaseBindingActivity<ActivitySecondBinding>(R.layout.activity_second) {

    private val vm: SecondActivityVm by lazy { getViewModel<SecondActivityVm>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm
    }
}