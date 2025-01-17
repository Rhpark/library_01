package kr.open.rhpark.library.ui.recyclerview.list_adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import kr.open.rhpark.library.ui.recyclerview.viewholder.BaseRcvViewHolder

public class RcvListSimpleAdapter<ITEM, BINDING : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
    private val onBind: (BaseRcvViewHolder<BINDING>, ITEM, position: Int) -> Unit,
    difUtilCallBack:RcvListDifUtilCallBack<ITEM>
) : BaseRcvListAdapter<ITEM, BaseRcvViewHolder<BINDING>>(difUtilCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : BaseRcvViewHolder<BINDING> = BaseRcvViewHolder(layoutRes, parent)

    override fun onBindViewHolder(
        holder: BaseRcvViewHolder<BINDING>,
        position: Int,
        item: ITEM
    ) {
        onBind(holder, item, position)
    }
}