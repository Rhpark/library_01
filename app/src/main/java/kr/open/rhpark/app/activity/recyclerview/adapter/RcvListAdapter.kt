package kr.open.rhpark.app.activity.recyclerview.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import kr.open.rhpark.app.R
import kr.open.rhpark.app.activity.recyclerview.adapter.item.RcvItem
import kr.open.rhpark.app.databinding.ItemRecyclerviewBinding
import kr.open.rhpark.library.ui.recyclerview.viewholder.BaseRcvViewHolder
import kr.open.rhpark.library.ui.recyclerview.list_adapter.BaseRcvListAdapter

class RcvListAdapter :
    BaseRcvListAdapter<RcvItem, BaseRcvViewHolder<RcvItem, ItemRecyclerviewBinding>>(DifUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            BaseRcvViewHolder<RcvItem, ItemRecyclerviewBinding> =
        BaseRcvViewHolder(R.layout.item_recyclerview, parent)

    override fun onBindViewHolder(
        holder: BaseRcvViewHolder<RcvItem, ItemRecyclerviewBinding>, position: Int, item: RcvItem
    ) {
        holder.binding.item = getItem(position)
//        holder.binding.rcvKey.text = getItem(position).key
//        holder.binding.rcvMsg.text = getItem(position).msg
    }

    class DifUtilCallBack : DiffUtil.ItemCallback<RcvItem>() {

        override fun areItemsTheSame(oldItem: RcvItem, newItem: RcvItem) = oldItem.key == newItem.key

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: RcvItem, newItem: RcvItem) =
            oldItem.key === newItem.key
    }
}