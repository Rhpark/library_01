package kr.open.rhpark.app.activity.recyclerview.adapter

import android.view.ViewGroup
import kr.open.rhpark.app.R
import kr.open.rhpark.app.activity.recyclerview.adapter.item.RcvItem
import kr.open.rhpark.app.databinding.ItemRecyclerviewBinding
import kr.open.rhpark.library.ui.recyclerview.viewholder.BaseRcvViewHolder
import kr.open.rhpark.library.ui.recyclerview.list_adapter.BaseRcvListAdapter
import kr.open.rhpark.library.ui.recyclerview.list_adapter.RcvListDifUtilCallBack

class RcvListAdapter :
    BaseRcvListAdapter<RcvItem, BaseRcvViewHolder<ItemRecyclerviewBinding>>(
        RcvListDifUtilCallBack<RcvItem>(
            itemsTheSame = { oldItem, newItem -> oldItem.key == newItem.key },
            contentsTheSame = { oldItem, newItem -> oldItem.key === newItem.key }
        )
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            BaseRcvViewHolder<ItemRecyclerviewBinding> =
        BaseRcvViewHolder(R.layout.item_recyclerview, parent)

    override fun onBindViewHolder(
        holder: BaseRcvViewHolder<ItemRecyclerviewBinding>, position: Int, item: RcvItem
    ) {
        holder.binding.item = item
//        holder.binding.rcvKey.text = getItem(position).key
//        holder.binding.rcvMsg.text = getItem(position).msg
    }
}