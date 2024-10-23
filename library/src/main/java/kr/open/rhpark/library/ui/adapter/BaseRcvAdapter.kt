package kr.open.rhpark.library.ui.adapter

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
public abstract class BaseRcvAdapter<TYPE, VH : RecyclerView.ViewHolder>() : RecyclerView.Adapter<VH>() {

    /**
     * Adapter Bind ItemList
     */
    private var itemList = listOf<TYPE>()

    private var onItemClickListener: ((Int, View) -> Unit)? = null
    private var onItemLongClickListener: ((Int, View) -> Unit)? = null

    public var detectMoves: Boolean = false

    /**
     * required for child class
     * update in DiffUtil.Callback.areItemsTheSame
     */
    protected open fun diffUtilAreItemsTheSame(oldItem: TYPE, newItem: TYPE): Boolean = oldItem === newItem
    protected open fun diffUtilAreContentsTheSame(oldItem: TYPE, newItem: TYPE): Boolean = oldItem == newItem


    public override fun getItemCount(): Int = itemList.size

    public fun getItem(position: Int): TYPE = itemList[position]

    public fun getItems(): List<TYPE> = itemList

    public fun setItems(items: List<TYPE>) {
        update(items)
    }

    public open fun addAllItem(items: List<TYPE>) {

        val fromSize = itemList.size
        val mutableList = itemList.toMutableList()
        mutableList.addAll(items)
        itemList = mutableList
        notifyItemRangeInserted(fromSize, items.size)
    }

    public open fun addItem(item: TYPE) {
        itemList = itemList + item
        Logx.d(item)

//        itemList.add(item)
        notifyItemInserted(itemList.size - 1)
    }

    public open fun addItemAt(position: Int, item: TYPE) {
        val mutableList = itemList.toMutableList()
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

        val mutableList = itemList.toMutableList()
        mutableList.removeAt(position)
        itemList = mutableList
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.setOnClickListener { onItemClickListener?.invoke(position, it) }
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.invoke(position, it)
            false
        }
    }

    protected fun isPositionValid(position: Int): Boolean = (position > RecyclerView.NO_POSITION && position < itemCount)

    public open fun removeAt(key: TYPE) {
        val position = itemList.indexOf(key)
        if (!isPositionValid(position)) {
            Logx.e("Can not remove item, position is $position itemcount $itemCount")
            return
        }
        removeAt(position)
    }

    private fun update(newItemList: List<TYPE>) {
        val difResult = DiffUtil.calculateDiff(RecyclerViewDiffUtil(itemList, newItemList), detectMoves)
        difResult.dispatchUpdatesTo(this)
        itemList = newItemList
    }

    private inner class RecyclerViewDiffUtil(
        private val oldList: List<TYPE>,
        private val newList: List<TYPE>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            diffUtilAreItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            diffUtilAreContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    public fun setOnItemClickListener(listener: (Int, View) -> Unit) {
        onItemClickListener = listener
    }

    public fun setOnItemLongClickListener(listener: (Int, View) -> Unit) {
        onItemLongClickListener = listener
    }


}