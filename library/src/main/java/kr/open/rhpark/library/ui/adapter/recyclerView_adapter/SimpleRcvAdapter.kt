package kr.open.rhpark.library.ui.adapter.recyclerView_adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import kr.open.rhpark.library.ui.adapter.viewholder.BaseRcvViewHolder

public open class SimpleRcvAdapter<ITEM, BINDING : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
    private val onBind: (BaseRcvViewHolder<BINDING>, ITEM, position: Int) -> Unit
) : BaseRcvAdapter<ITEM, BaseRcvViewHolder<BINDING>>() {

    private var diffUtilItemSame: ((oldItem: ITEM, newItem: ITEM) -> Boolean)? = null
    private var diffUtilContentsSame: ((oldItem: ITEM, newItem: ITEM) -> Boolean)? = null

    public fun setDiffUtilItemSame(diffUtilItemSame: (oldItem: ITEM, newItem: ITEM) -> Boolean) {
        this.diffUtilItemSame = diffUtilItemSame
    }

    public fun setDiffUtilContentsSame(diffUtilContentsSame: (oldItem: ITEM, newItem: ITEM) -> Boolean) {
        this.diffUtilContentsSame = diffUtilContentsSame
    }

    override fun diffUtilAreItemsTheSame(oldItem: ITEM, newItem: ITEM): Boolean =
        diffUtilItemSame?.let { it.invoke(oldItem, newItem) } ?: super.diffUtilAreItemsTheSame(oldItem, newItem)


    override fun diffUtilAreContentsTheSame(oldItem: ITEM, newItem: ITEM): Boolean =
        diffUtilContentsSame?.let { it.invoke(oldItem, newItem) } ?: super.diffUtilAreContentsTheSame(oldItem, newItem)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : BaseRcvViewHolder<BINDING> = BaseRcvViewHolder(layoutRes, parent)

    override fun onBindViewHolder(holder: BaseRcvViewHolder<BINDING>, position: Int, item: ITEM) {
        onBind(holder, item, position)
    }
}