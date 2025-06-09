package kr.open.rhpark.app.activity.recyclerview.adapter

import android.view.ViewGroup
import kr.open.rhpark.app.R
import kr.open.rhpark.app.activity.recyclerview.adapter.item.RcvItem
import kr.open.rhpark.app.databinding.ItemRecyclerviewBinding
import kr.open.rhpark.library.ui.view.adapter.viewholder.BaseBindingRcvViewHolder
import kr.open.rhpark.library.ui.view.adapter.list_adapter.BaseRcvListAdapter
import kr.open.rhpark.library.ui.view.adapter.list_adapter.RcvListDiffUtilCallBack

class RcvListAdapter : BaseRcvListAdapter<RcvItem, BaseBindingRcvViewHolder<ItemRecyclerviewBinding>>(
        RcvListDiffUtilCallBack<RcvItem>(
            itemsTheSame = { oldItem, newItem -> oldItem.key == newItem.key },
            contentsTheSame = { oldItem, newItem -> oldItem.key === newItem.key }
        )
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            BaseBindingRcvViewHolder<ItemRecyclerviewBinding> =
        BaseBindingRcvViewHolder(R.layout.item_recyclerview, parent)

    override fun onBindViewHolder(
        holder: BaseBindingRcvViewHolder<ItemRecyclerviewBinding>, position: Int, item: RcvItem
    ) {
        holder.binding.item = item
//        holder.binding.rcvKey.text = getItem(position).key
//        holder.binding.rcvMsg.text = getItem(position).msg
    }
}