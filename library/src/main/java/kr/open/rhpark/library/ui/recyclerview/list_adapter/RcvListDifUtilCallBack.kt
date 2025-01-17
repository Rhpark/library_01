package kr.open.rhpark.library.ui.recyclerview.list_adapter

import androidx.recyclerview.widget.DiffUtil

public class RcvListDifUtilCallBack<ITEM>(
    private val itemsTheSame: ((oldItem: ITEM, newItem: ITEM) -> Boolean) = {
        oldItem, newItem -> oldItem == newItem
    },
    private val contentsTheSame: ((oldItem: ITEM, newItem: ITEM) -> Boolean) = {
        oldItem, newItem -> oldItem === newItem
    }
) : DiffUtil.ItemCallback<ITEM>() {

    public override fun areItemsTheSame(oldItem: ITEM & Any, newItem: ITEM & Any): Boolean = itemsTheSame(oldItem, newItem)

    public override fun areContentsTheSame(oldItem: ITEM & Any, newItem: ITEM & Any): Boolean = contentsTheSame(oldItem, newItem)
}
