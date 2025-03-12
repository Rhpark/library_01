package kr.open.rhpark.library.ui.adapter.list_adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import kr.open.rhpark.library.ui.adapter.viewholder.BaseRcvViewHolder

public class SimpleRcvListAdapter<ITEM, BINDING : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
    difUtilCallBack:RcvListDifUtilCallBack<ITEM>,
    private val onBind: (BaseRcvViewHolder<BINDING>, ITEM, position: Int) -> Unit
) : BaseRcvListAdapter<ITEM, BaseRcvViewHolder<BINDING>>(difUtilCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : BaseRcvViewHolder<BINDING> = BaseRcvViewHolder(layoutRes, parent)

    override fun onBindViewHolder(holder: BaseRcvViewHolder<BINDING>, position: Int, item: ITEM) {
        onBind(holder, item, position)
    }
}