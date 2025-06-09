package kr.open.rhpark.app.activity.recyclerview

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import kr.open.rhpark.app.R
import kr.open.rhpark.app.activity.recyclerview.adapter.RcvAdapter
import kr.open.rhpark.app.activity.recyclerview.adapter.RcvListAdapter
import kr.open.rhpark.library.ui.view.adapter.recyclerView_adapter.SimpleBindingRcvAdapter
import kr.open.rhpark.app.activity.recyclerview.adapter.item.RcvItem
import kr.open.rhpark.app.databinding.ActivityRecyclerviewBinding
import kr.open.rhpark.app.databinding.ItemRecyclerviewBinding
import kr.open.rhpark.library.debug.logcat.Logx
import kr.open.rhpark.library.ui.view.activity.BaseBindingActivity
import kr.open.rhpark.library.ui.view.adapter.list_adapter.RcvListDiffUtilCallBack
import kr.open.rhpark.library.ui.view.adapter.list_adapter.SimpleBindingRcvListAdapter
import kr.open.rhpark.library.ui.view.adapter.list_adapter.SimpleRcvListAdapter
import kr.open.rhpark.library.util.extensions.ui.view.showSoftKeyBoard
import kr.open.rhpark.library.util.extensions.ui.view.toastShowShort

class RecyclerViewActivity : BaseBindingActivity<ActivityRecyclerviewBinding>(R.layout.activity_recyclerview) {

    private val vm: RecyclerviewActivityVm by lazy { getViewModel<RecyclerviewActivityVm>() }

    private val adapter: RcvAdapter = RcvAdapter().apply {
        setOnItemClickListener { i, rcvItem, view ->
            Logx.d("adapter On Click posision $i, item $rcvItem")
        }
    }

    private val rcvSimpleBindingAdapter = SimpleBindingRcvAdapter<RcvItem, ItemRecyclerviewBinding>(R.layout.item_recyclerview) {
        holder, item, position -> holder.binding.item = item
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

    private val rcvListSimpleBindingAdapter = SimpleBindingRcvListAdapter<RcvItem, ItemRecyclerviewBinding>(R.layout.item_recyclerview,
        RcvListDiffUtilCallBack<RcvItem>(
            { oldItem, newItem -> oldItem.key === newItem.key },
            { oldItem, newItem -> oldItem.key == newItem.key }
        )
    ) { holder, item, position -> holder.binding.item = item }.apply {
        setOnItemClickListener { i, rcvItem, view ->
            Logx.d("rcvListSimpleAdapter On Click posision $i, item $rcvItem")
        }
    }

    private val rcvListSimpleAdapter = SimpleRcvListAdapter<RcvItem>(R.layout.item_recyclerview,
        RcvListDiffUtilCallBack<RcvItem>(
            { oldItem, newItem -> oldItem.key === newItem.key },
            { oldItem, newItem -> oldItem.key == newItem.key }
        )
    ) { holder, item, position ->
        holder.findViewById<TextView>(R.id.rcvKey).text = item.key
        holder.findViewById<TextView>(R.id.rcvMsg).text = item.msg
    }.apply {
        setOnItemClickListener { i, rcvItem, view ->
            Logx.d("rcvListSimpleAdapter On Click posision $i, item $rcvItem")
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm

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
                            toastShowShort(it.msg)
                        }

                        is RecyclerviewActivityVmEvent.OnUpdateAdapter -> {
                            setAdapter(it.adapterType)
                        }
                    }
                }
            }
        }
        recyclerviewScrollStateSet()
        binding.edtKey.showSoftKeyBoard(200L)
    }

    private fun recyclerviewScrollStateSet() {
        lifecycleScope.launch {
            launch {
                binding.rcvList.sfEdgeReachedFlow.collect{
                    toastShowShort("edge : ${it.first}, isReached : ${it.second}")
                }
            }
            launch {
                binding.rcvList.sfScrollDirectionFlow.collect {
                    toastShowShort("scrollDirection : $it")
                }
            }
        }

// or
//        binding.rcvList.setOnScrollDirectionListener { direction->
//
//        }
//
//        binding.rcvList.setOnReachEdgeListener { edge, isReached ->
//
//        }
    }

    private fun onSetAdapter(adapterType: RecyclerviewActivityVm.AdapterType, datas: List<RcvItem>) {
        when(adapterType) {
            RecyclerviewActivityVm.AdapterType.ADAPTER -> {
                adapter.setItems(datas)
            }

            RecyclerviewActivityVm.AdapterType.SIMPLE_BINDING_ADAPTER -> {
                rcvSimpleBindingAdapter.setItems(datas)
            }

            RecyclerviewActivityVm.AdapterType.LIST_ADAPTER -> {
                rcvListAdapter.setItems(datas)
            }

            RecyclerviewActivityVm.AdapterType.LIST_SIMPLE_BINDING_ADAPTER -> {
                rcvListSimpleBindingAdapter.setItems(datas)
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

            RecyclerviewActivityVm.AdapterType.SIMPLE_BINDING_ADAPTER -> {
                rcvSimpleBindingAdapter.removeAt(position)
            }

            RecyclerviewActivityVm.AdapterType.LIST_ADAPTER -> {
                rcvListAdapter.removeAt(position)
            }

            RecyclerviewActivityVm.AdapterType.LIST_SIMPLE_BINDING_ADAPTER -> {
                rcvListSimpleBindingAdapter.removeAt(position)
            }

            RecyclerviewActivityVm.AdapterType.LIST_SIMPLE_ADAPTER -> {
                rcvListSimpleAdapter.removeAt(position)
            }
        }
    }

    private fun setAdapter(adapterType: RecyclerviewActivityVm.AdapterType) {
        when(adapterType) {
            RecyclerviewActivityVm.AdapterType.ADAPTER -> binding.rcvList.adapter = adapter
            RecyclerviewActivityVm.AdapterType.SIMPLE_BINDING_ADAPTER -> binding.rcvList.adapter = rcvSimpleBindingAdapter
            RecyclerviewActivityVm.AdapterType.LIST_ADAPTER -> binding.rcvList.adapter = rcvListAdapter
            RecyclerviewActivityVm.AdapterType.LIST_SIMPLE_BINDING_ADAPTER -> binding.rcvList.adapter = rcvListSimpleBindingAdapter
            RecyclerviewActivityVm.AdapterType.LIST_SIMPLE_ADAPTER -> binding.rcvList.adapter = rcvListSimpleAdapter
        }

    }
}