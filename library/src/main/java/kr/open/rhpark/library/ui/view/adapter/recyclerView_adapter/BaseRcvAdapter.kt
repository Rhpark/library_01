package kr.open.rhpark.library.ui.view.adapter.recyclerView_adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kr.open.rhpark.library.debug.logcat.Logx

/**
 * Recycler View Base Adapter
 * @param ITEM Type of items in the adapter
 * @param VH ViewHolder type for the adapter
 */
public abstract class BaseRcvAdapter<ITEM: Any, VH : RecyclerView.ViewHolder>() : RecyclerView.Adapter<VH>() {

    /**
     * Current list of items in the adapter
     */
    private var itemList: List<ITEM> = emptyList()

    private var onItemClickListener: ((Int, ITEM, View) -> Unit)? = null
    private var onItemLongClickListener: ((Int, ITEM, View) -> Unit)? = null

    /**
     * Whether DiffUtil should detect moved items
     */
    public var detectMoves: Boolean = false

    /**
     * Override to customize item comparison for DiffUtil
     * Default implementation compares by identity
     */
    protected open fun diffUtilAreItemsTheSame(oldItem: ITEM, newItem: ITEM): Boolean = oldItem === newItem

    /**
     * Override to customize content comparison for DiffUtil
     * Default implementation uses equals
     */
    protected open fun diffUtilAreContentsTheSame(oldItem: ITEM, newItem: ITEM): Boolean = oldItem == newItem

    /**
     * Subclasses must implement this method to bind data to ViewHolder
     */
    protected abstract fun onBindViewHolder(holder: VH, position: Int, item: ITEM)

    public override fun getItemCount(): Int = itemList.size

    public fun getItem(position: Int): ITEM {
        // Add bounds check for safety, although isPositionValid should handle most cases
        if (!isPositionValid(position)) {
            Logx.e("getItem() called with invalid position: $position, size: ${itemList.size}")
            throw IndexOutOfBoundsException("Invalid position: $position, size: ${itemList.size}")
        }
        return itemList[position]
    }

    public fun getItems(): List<ITEM> = itemList

    /**
     * Sets new items using DiffUtil for efficient updates
     */
    public fun setItems(newItems: List<ITEM>) { 
        update(newItems)
    }

    /**
     * Adds items to the end of the current list
     */
    public open fun addItems(items: List<ITEM>) {
        if (items.isEmpty()) {
            Logx.d("addItems() items is empty")
            return
        }

        val fromSize = itemList.size
        itemList = getMutableItemList().apply {
            addAll(items)
        }
        notifyItemRangeInserted(fromSize, items.size)
    }

    /**
     * Creates a mutable copy of the current item list
     */
    private fun getMutableItemList(): MutableList<ITEM> = itemList.toMutableList()

    public open fun addItem(item: ITEM) {
        val newPosition = itemList.size
        itemList = itemList + item
        notifyItemInserted(newPosition)
    }

    /**
     * Adds a single item at the specified position
     */
    public open fun addItemAt(position: Int, item: ITEM) {
        if(!isPositionValid(position)) {
            Logx.d("addItemAt() position is invalid $position, itemSize: ${itemList.size}")
            return
        }
        itemList = getMutableItemList().apply { 
            add(position, item)
        }
        notifyItemInserted(position)
    }

    /**
     * Removes all items from the list
     */
    public open fun removeAll() {
        val itemSize = itemList.size
        itemList = emptyList()
        notifyItemRangeRemoved(0, itemSize)
    }

    /**
     * Removes the item at the specified position
     */
    public open fun removeAt(position: Int) {
        if (!isPositionValid(position)) {
            Logx.e("Can not remove item, position is $position, itemcount $itemCount")
            return
        }
        
        itemList = getMutableItemList().apply {
            removeAt(position)
        }
        notifyItemRemoved(position)
    }

    /**
     * Removes the first occurrence of the specified item
     */
    public open fun removeItem(item: ITEM) {
        val position = itemList.indexOf(item)
        if (position != -1) {
            removeAt(position)
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (!isPositionValid(position)) {
            Logx.e("Cannot bind item, position is $position, itemcount $itemCount")
            return
        }

        val item = getItem(position)

        holder.itemView.apply {
            setOnClickListener { view ->
                val adapterPosition = holder.adapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(adapterPosition, item, view)
                }
            }

            setOnLongClickListener { view ->
                val adapterPosition = holder.adapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemLongClickListener?.invoke(adapterPosition, item, view)
                }
                true
            }
        }

        onBindViewHolder(holder, position, item)
    }

    /**
     * Checks if the given position is valid for the current list
     */
    protected fun isPositionValid(position: Int): Boolean =
        position > RecyclerView.NO_POSITION && position < itemList.size

    /**
     * Updates the list using DiffUtil for efficient updates
     */
    private fun update(newItemList: List<ITEM>) {
        val diffResult = DiffUtil.calculateDiff(
            RecyclerViewDiffUtil(itemList, newItemList),
            detectMoves
        )
        itemList = newItemList
        diffResult.dispatchUpdatesTo(this)
    }

    private inner class RecyclerViewDiffUtil(
        private val oldList: List<ITEM>,
        private val newList: List<ITEM>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            diffUtilAreItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            diffUtilAreContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    public fun setOnItemClickListener(listener: (Int, ITEM, View) -> Unit) {
        onItemClickListener = listener
    }

    public fun setOnItemLongClickListener(listener: (Int, ITEM, View) -> Unit) {
        onItemLongClickListener = listener
    }
}