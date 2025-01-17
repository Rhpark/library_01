package kr.open.rhpark.app.activity.recyclerview

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import kr.open.rhpark.app.R
import kr.open.rhpark.app.activity.recyclerview.adapter.RcvAdapter
import kr.open.rhpark.app.activity.recyclerview.adapter.RcvListAdapter
import kr.open.rhpark.library.ui.recyclerview.adapter.RcvSimpleAdapter
import kr.open.rhpark.app.activity.recyclerview.adapter.item.RcvItem
import kr.open.rhpark.app.databinding.ActivityRecyclerviewBinding
import kr.open.rhpark.app.databinding.ItemRecyclerviewBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.activity.BaseBindingActivity
import kr.open.rhpark.library.ui.recyclerview.list_adapter.RcvListDifUtilCallBack
import kr.open.rhpark.library.ui.recyclerview.list_adapter.RcvListSimpleAdapter
import kr.open.rhpark.library.util.extensions.context.getSoftKeyboardController

class RecyclerViewActivity : BaseBindingActivity<ActivityRecyclerviewBinding>(R.layout.activity_recyclerview) {

    private val vm: RecyclerviewActivityVm by lazy { getViewModel<RecyclerviewActivityVm>() }

    private val adapter: RcvAdapter = RcvAdapter().apply {
        setOnItemClickListener { i, rcvItem, view ->
            Logx.d("adapter On Click posision $i, item $rcvItem")
        }
    }

    private val rcvSimpleAdapter = RcvSimpleAdapter<RcvItem, ItemRecyclerviewBinding>(R.layout.item_recyclerview) { holder, item, position ->
        holder.binding.item = item
    }.apply {
        setDiffUtilItemSame { oldItem, newItem -> oldItem.key === newItem.key }
        setDiffUtilContentsSame { oldItem, newItem -> oldItem.key == newItem.key }
        setOnItemClickListener { i, rcvItem, view ->
            Logx.d("simpleAdapter On Click posision $i, item $rcvItem")
        }
    }

    private val rcvListAdapter = RcvListAdapter().apply {
        setOnItemClickListener { i, rcvItem, view ->
            Logx.d("listAdapter On Click posision $i, item $rcvItem")
        }
    }

    private val rcvListSimpleAdapter =
        RcvListSimpleAdapter<RcvItem, ItemRecyclerviewBinding>(R.layout.item_recyclerview,
            onBind = { holder, item, position -> holder.binding.item = item },
            RcvListDifUtilCallBack<RcvItem>(
                { oldItem, newItem -> oldItem.key === newItem.key },
                { oldItem, newItem -> oldItem.key == newItem.key }
            )
        ).apply {
            setOnItemClickListener { i, rcvItem, view ->
                Logx.d("rcvListSimpleAdapter On Click posision $i, item $rcvItem")
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm
        binding.rcvList.run {
            setOnReachEdgeListener { edge, isReached ->
                Logx.d("edge : $edge, isReached : $isReached")
                toast.showMsgShort("edge : $edge, isReached : $isReached")
            }
            setOnScrollDirectionListener { scrollDirection ->
                Logx.d("scrollDirection : $scrollDirection")
                toast.showMsgShort("scrollDirection : $scrollDirection")
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                vm.mEventVm.collect {
                    when (it) {
                        is RecyclerviewActivityVmEvent.OnSetAdapter -> {
                            onSetAdapter(it.adapterType,it.datas)
                            setAdapter(it.adapterType)
                        }
                        is RecyclerviewActivityVmEvent.OnRemoveItem -> {
                            onRemoveAt(it.adapterType,it.position)
                        }

                        is RecyclerviewActivityVmEvent.OnToastShow -> {
                            toast.showMsgShort(it.msg)
                        }

                        is RecyclerviewActivityVmEvent.OnUpdateAdapter -> {
                            setAdapter(it.adapterType)
                        }
                    }
                }
            }
        }
        applicationContext.getSoftKeyboardController().showDelay(binding.edtKey, 200L)
    }

    private fun onSetAdapter(adapterType: RecyclerviewActivityVm.AdapterType, datas: List<RcvItem>) {
        when(adapterType) {
            RecyclerviewActivityVm.AdapterType.ADAPTER -> {
                adapter.setItems(datas)
            }

            RecyclerviewActivityVm.AdapterType.SIMPLE_ADAPTER -> {
                rcvSimpleAdapter.setItems(datas)
            }

            RecyclerviewActivityVm.AdapterType.LIST_ADAPTER -> {
                rcvListAdapter.setItems(datas)
            }

            RecyclerviewActivityVm.AdapterType.LIST_SIMPLE_ADAPTER -> {
                rcvListSimpleAdapter.setItems(datas)
            }
        }
    }

    private fun onRemoveAt(adapterType: RecyclerviewActivityVm.AdapterType, position:Int) {
        when(adapterType) {
            RecyclerviewActivityVm.AdapterType.ADAPTER -> {
                adapter.removeAt(position)
            }

            RecyclerviewActivityVm.AdapterType.SIMPLE_ADAPTER -> {
                rcvSimpleAdapter.removeAt(position)
            }

            RecyclerviewActivityVm.AdapterType.LIST_ADAPTER -> {
                rcvListAdapter.removeAt(position)
            }

            RecyclerviewActivityVm.AdapterType.LIST_SIMPLE_ADAPTER -> {
                rcvListSimpleAdapter.removeAt(position)
            }
        }
    }

    private fun setAdapter(adapterType: RecyclerviewActivityVm.AdapterType) {
        when(adapterType) {
            RecyclerviewActivityVm.AdapterType.ADAPTER -> binding.rcvList.adapter = adapter
            RecyclerviewActivityVm.AdapterType.SIMPLE_ADAPTER -> binding.rcvList.adapter = rcvSimpleAdapter
            RecyclerviewActivityVm.AdapterType.LIST_ADAPTER -> binding.rcvList.adapter = rcvListAdapter
            RecyclerviewActivityVm.AdapterType.LIST_SIMPLE_ADAPTER -> binding.rcvList.adapter = rcvListSimpleAdapter
        }

    }
}