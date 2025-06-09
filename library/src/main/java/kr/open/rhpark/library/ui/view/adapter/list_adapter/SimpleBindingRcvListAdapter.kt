package kr.open.rhpark.library.ui.view.adapter.list_adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import kr.open.rhpark.library.ui.view.adapter.viewholder.BaseBindingRcvViewHolder

/**
 * A simple implementation of RecyclerView adapter with databinding support
 *
 * @param ITEM Type of data items in the list
 * @param BINDING Type of ViewDataBinding for item views
 * @property layoutRes Layout resource ID for item views
 * @property onBind Function to bind data to the ViewHolder
 */
public open class SimpleBindingRcvListAdapter<ITEM : Any, BINDING : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
    listDiffUtil: RcvListDiffUtilCallBack<ITEM>,
    private val onBind: (BaseBindingRcvViewHolder<BINDING>, ITEM, position: Int) -> Unit
) : BaseRcvListAdapter<ITEM, BaseBindingRcvViewHolder<BINDING>>(listDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingRcvViewHolder<BINDING> =
        BaseBindingRcvViewHolder(layoutRes, parent)

    override fun onBindViewHolder(holder: BaseBindingRcvViewHolder<BINDING>, position: Int, item: ITEM) {
        onBind(holder, item, position)
    }
}