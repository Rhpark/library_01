package kr.open.rhpark.app.activity.recyclerview

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import kr.open.rhpark.app.R
import kr.open.rhpark.app.activity.recyclerview.adapter.RcvAdapter
import kr.open.rhpark.app.activity.recyclerview.adapter.item.RcvItem
import kr.open.rhpark.app.databinding.ActivityRecyclerviewBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.activity.BaseBindingActivity

class RecyclerViewActivity : BaseBindingActivity<ActivityRecyclerviewBinding>(R.layout.activity_recyclerview) {

    private val vm: RecyclerviewActivityVm by lazy { getViewModel<RecyclerviewActivityVm>() }

    private val adapter: RcvAdapter by lazy { RcvAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                vm.eventVm.collect {
                    when (it) {
                        is RecyclerviewActivityVmEvent.OnAddItem -> {
                            Logx.d("Add Item ${it.key}")
                            adapter.addItem(RcvItem(it.key.toString(), "Test${it.key}"))
                        }

                        is RecyclerviewActivityVmEvent.OnRemoveItem -> {
                            Logx.d("Remove Item ${it.key}")
                            adapter.removeAt(it.key)
                        }

                        is RecyclerviewActivityVmEvent.OnToastShow -> {
                            toast.showShort(it.msg)
                        }
                    }
                }
            }
        }

        binding.rcvList.adapter = adapter.apply {
            addItem(RcvItem("1","Test01"))
            addItem(RcvItem("2","Test02"))
            addItem(RcvItem("3","Test03"))
            addItem(RcvItem("5","Test05"))
            addItem(RcvItem("7","Test07"))
            setOnItemClickListener { pos, view ->
                toast.showShort("ItemClick position = $pos")
            }
            setOnItemLongClickListener { pos, view ->
                toast.showShort("ItemLongClick position = $pos")
            }
        }

        systemServiceManagerInfo.softKeyboardInfoView.show(binding.edtKey,200L)
    }
}