package kr.open.rhpark.app.activity.recyclerview

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import kr.open.rhpark.app.R
import kr.open.rhpark.app.databinding.ActivityRecyclerviewBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.activity.BaseBindingActivity

class RecyclerViewActivity : BaseBindingActivity<ActivityRecyclerviewBinding>(R.layout.activity_recyclerview) {

    private val vm: RecyclerviewActivityVm by lazy { getViewModel<RecyclerviewActivityVm>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm
        binding.rcvList.run {
            setOnReachEdgeListener { edge, isReached ->
                Logx.d("edge : $edge, isReached : $isReached")
                toast.showShort("edge : $edge, isReached : $isReached")
            }
            setOnScrollDirectionListener { scrollDirection ->
                Logx.d("scrollDirection : $scrollDirection")
                toast.showShort("scrollDirection : $scrollDirection")
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                vm.eventVm.collect {
                    when (it) {
                        is RecyclerviewActivityVmEvent.OnUpdateAdapterMode -> {
                            binding.rcvList.adapter = it.adapter
                        }

                        is RecyclerviewActivityVmEvent.OnUpdateListAdapterMode -> {
                            binding.rcvList.adapter = it.listAdapter
                        }

                        is RecyclerviewActivityVmEvent.OnToastShow -> {
                            toast.showShort(it.msg)
                        }
                    }
                }
            }
        }
        systemServiceManagerInfo.softKeyboardInfoView.show(binding.edtKey, 200L)
    }
}