package kr.open.rhpark.library.ui.recyclerview.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

public open class BaseRcvViewHolder<ITEM, BINDING : ViewDataBinding>(
    @LayoutRes xmlRes: Int,
    parent: ViewGroup,
    attachToRoot: Boolean = false,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(xmlRes, parent, attachToRoot)
) {
    public val binding: BINDING = DataBindingUtil.bind<BINDING>(itemView) ?: throw IllegalStateException("Exception Binding is null!!")

    /**
     * Verification of the existence of an item
     * for listener(ex OnItemClickListener...)
     */
    protected fun isValidPosition(): Boolean = (adapterPosition > RecyclerView.NO_POSITION)

    public open fun bind(type: ITEM) {    }
}