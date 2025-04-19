package kr.open.rhpark.library.ui.view.adapter.list_adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import kr.open.rhpark.library.ui.view.adapter.viewholder.BaseRcvViewHolder

/**
 * A simple implementation of RecyclerView adapter with databinding support
 *
 * @param ITEM Type of data items in the list
 * @param BINDING Type of ViewDataBinding for item views
 * @property layoutRes Layout resource ID for item views
 * @property onBind Function to bind data to the ViewHolder
 */
public open class SimpleRcvListAdapter<ITEM : Any, BINDING : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
    listDiffUtil: RcvListDiffUtilCallBack<ITEM>,
    private val onBind: (BaseRcvViewHolder<BINDING>, ITEM, position: Int) -> Unit
) : BaseRcvListAdapter<ITEM, BaseRcvViewHolder<BINDING>>(listDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRcvViewHolder<BINDING> =
        BaseRcvViewHolder(layoutRes, parent)

    override fun onBindViewHolder(holder: BaseRcvViewHolder<BINDING>, position: Int, item: ITEM) {
        onBind(holder, item, position)
    }
}