package kr.open.rhpark.library.ui.adapter.recyclerView_adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kr.open.rhpark.library.debug.logcat.Logx


/**
 * Recycler View Base Adapter
 *  <T> is Item List Generic Type
 *  <VH> is ViewHolder Generic Type
 *  xmlRes is Item Layout Resource
 */
public abstract class BaseRcvAdapter<ITEM, VH : RecyclerView.ViewHolder>() : RecyclerView.Adapter<VH>() {

    /**
     * Adapter Bind ItemList
     */
    private var itemList = listOf<ITEM>()

    private var onItemClickListener: ((Int, ITEM, View) -> Unit)? = null
    private var onItemLongClickListener: ((Int, ITEM, View) -> Unit)? = null

    public var detectMoves: Boolean = false

    /**
     * required for child class
     * update in DiffUtil.Callback.areItemsTheSame
     */
    protected open fun diffUtilAreItemsTheSame(oldItem: ITEM, newItem: ITEM): Boolean = oldItem === newItem
    protected open fun diffUtilAreContentsTheSame(oldItem: ITEM, newItem: ITEM): Boolean = oldItem == newItem

    protected abstract fun onBindViewHolder(holder: VH, position: Int, item: ITEM)

    public override fun getItemCount(): Int = itemList.size

    public fun getItem(position: Int): ITEM = itemList[position]

    public fun getItems(): List<ITEM> = itemList

    public fun setItems(items: List<ITEM>): Unit = update(items)

    public open fun addItems(items: List<ITEM>) {
        val fromSize = itemList.size
        val mutableList = getMutableItemList()
        mutableList.addAll(items)
        itemList = mutableList
        notifyItemRangeInserted(fromSize, items.size)
    }

    private fun getMutableItemList() = getItems().toMutableList()

    public open fun addItem(item: ITEM) {
        itemList = itemList + item
        Logx.d(item)
        notifyItemInserted(itemList.size - 1)
    }

    public open fun addItemAt(position: Int, item: ITEM) {
        val mutableList = getMutableItemList()
        mutableList.add(position, item)
        itemList = mutableList
        notifyItemInserted(position)
    }

    public open fun removeAll() {
        val itemSize = itemList.size
        itemList = emptyList()
        notifyItemRangeRemoved(0, itemSize)
    }

    public open fun removeAt(position: Int) {
        if (!isPositionValid(position)) {
            Logx.e("Can not remove item, position is $position itemcount $itemCount")
            return
        }

        val mutableList = getMutableItemList()
        mutableList.removeAt(position)
        itemList = mutableList
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener { onItemClickListener?.invoke(position, item, it) }
        holder.itemView.setOnLongClickListener {
            if(onItemLongClickListener == null) {
                false
            } else {
                onItemLongClickListener?.invoke(position, item, it)
                true
            }
        }
        onBindViewHolder(holder, position, item)
    }

    protected fun isPositionValid(position: Int): Boolean = (position > RecyclerView.NO_POSITION && position < itemCount)

    public open fun removeAt(key: ITEM) {
        val position = itemList.indexOf(key)
        if (!isPositionValid(position)) {
            Logx.e("Can not remove item, position is $position itemcount $itemCount")
            return
        }
        removeAt(position)
    }

    private fun update(newItemList: List<ITEM>) {
        val difResult = DiffUtil.calculateDiff(RecyclerViewDiffUtil(itemList, newItemList), detectMoves)
        difResult.dispatchUpdatesTo(this)
        itemList = newItemList
    }

    private inner class RecyclerViewDiffUtil(
        private val oldList: List<ITEM>,
        private val newList: List<ITEM>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            diffUtilAreItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            diffUtilAreContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    public fun setOnItemClickListener(listener: (Int, ITEM, View) -> Unit) {
        onItemClickListener = listener
    }

    public fun setOnItemLongClickListener(listener: (Int, ITEM, View) -> Unit) {
        onItemLongClickListener = listener
    }
}