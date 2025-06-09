package kr.open.rhpark.library.ui.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Base ViewHolder for RecyclerView without ViewDataBinding
 *
 * @param xmlRes Layout resource ID
 * @param parent Parent ViewGroup
 * @param attachToRoot Whether to attach to root (default: false)
 */
public open class BaseRcvViewHolder(
    @LayoutRes xmlRes: Int,
    parent: ViewGroup,
    attachToRoot: Boolean = false
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(xmlRes, parent, attachToRoot)
) {

    /**
     * Find view by ID with type casting
     * @param id View ID
     * @return Found view with type T
     */
    public fun <T : View> findViewById(id: Int): T = itemView.findViewById(id)

    /**
     * Find view by ID with null safety
     * @param id View ID
     * @return Found view with type T or null
     */
    public fun <T : View> findViewByIdOrNull(id: Int): T? = itemView.findViewById(id)

    /**
     * Verification of the existence of an item
     * for listener(ex OnItemClickListener...)
     * @return true if position is valid
     */
    protected fun isValidPosition(): Boolean = (adapterPosition > RecyclerView.NO_POSITION)

    /**
     * Get current adapter position safely
     * @return adapter position or -1 if invalid
     */
    protected fun getAdapterPositionSafe(): Int =
        if (isValidPosition()) adapterPosition else RecyclerView.NO_POSITION
}
