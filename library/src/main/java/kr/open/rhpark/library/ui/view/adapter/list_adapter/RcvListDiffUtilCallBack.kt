package kr.open.rhpark.library.ui.view.adapter.list_adapter

import androidx.recyclerview.widget.DiffUtil

/**
 * RecyclerView ListAdapter에 사용되는 DiffUtil 구현체
 *
 * @param ITEM 아이템 타입
 * @param itemsTheSame 두 아이템이 같은 항목인지 비교하는 람다 (ID 비교 등)
 * @param contentsTheSame 두 아이템의 내용이 같은지 비교하는 람다 (내용 비교)
 */
public class RcvListDiffUtilCallBack<ITEM>(
    private val itemsTheSame: ((oldItem: ITEM, newItem: ITEM) -> Boolean) = { oldItem, newItem ->
        oldItem == newItem
    },
    private val contentsTheSame: ((oldItem: ITEM, newItem: ITEM) -> Boolean) = { oldItem, newItem ->
        oldItem === newItem
    }
) : DiffUtil.ItemCallback<ITEM>() {

    public override fun areItemsTheSame(oldItem: ITEM & Any, newItem: ITEM & Any): Boolean = itemsTheSame(oldItem, newItem)

    public override fun areContentsTheSame(oldItem: ITEM & Any, newItem: ITEM & Any): Boolean = contentsTheSame(oldItem, newItem)
}
