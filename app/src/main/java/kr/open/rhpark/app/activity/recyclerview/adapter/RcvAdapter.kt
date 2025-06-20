package kr.open.rhpark.app.activity.recyclerview.adapter

import android.view.ViewGroup
import kr.open.rhpark.app.R
import kr.open.rhpark.app.activity.recyclerview.adapter.item.RcvItem
import kr.open.rhpark.app.databinding.ItemRecyclerviewBinding
import kr.open.rhpark.library.ui.view.adapter.recyclerView_adapter.BaseRcvAdapter
import kr.open.rhpark.library.ui.view.adapter.viewholder.BaseBindingRcvViewHolder

class RcvAdapter : BaseRcvAdapter<RcvItem, BaseBindingRcvViewHolder<ItemRecyclerviewBinding>>() {

    override fun diffUtilAreItemsTheSame(oldItem: RcvItem, newItem: RcvItem): Boolean {
        return oldItem.key === newItem.key
    }

    override fun diffUtilAreContentsTheSame(oldItem: RcvItem, newItem: RcvItem): Boolean {
        return oldItem.key === newItem.key
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : BaseBindingRcvViewHolder<ItemRecyclerviewBinding> = BaseBindingRcvViewHolder(R.layout.item_recyclerview, parent)

    override fun onBindViewHolder(
        holder: BaseBindingRcvViewHolder<ItemRecyclerviewBinding>,
        position: Int,
        item: RcvItem
    ) {
        holder.binding.item = item
//        holder.binding.rcvKey.text = getItem(position).key
//        holder.binding.rcvMsg.text = getItem(position).msg
    }
}