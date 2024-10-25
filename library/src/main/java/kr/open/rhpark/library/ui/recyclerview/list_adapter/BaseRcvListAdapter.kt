package kr.open.rhpark.library.ui.recyclerview.list_adapter

import android.view.View
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.open.rhpark.library.debug.logcat.Logx
import java.util.Collections

public abstract class BaseRcvListAdapter<ITEM, VH : RecyclerView.ViewHolder>(listDiffUtil :DiffUtil.ItemCallback<ITEM>) :
    ListAdapter<ITEM, VH>(listDiffUtil) {

    private val differ = AsyncListDiffer<ITEM>(this, listDiffUtil)

    private var onItemClickListener: ((Int, ITEM, View) -> Unit)? = null
    private var onItemLongClickListener: ((Int, ITEM, View) -> Unit)? = null

    protected abstract fun onBindViewHolder(holder: VH, position: Int, item: ITEM)

    override fun getItemCount(): Int = getItems().size

    public override fun getItem(position: Int): ITEM = getItems()[position]

    protected fun getMutableItemList(): MutableList<ITEM> = differ.currentList.toMutableList()

    public fun getItems(): MutableList<ITEM> = differ.currentList

    public fun changedItemList(itemList: List<ITEM>) {
        differ.submitList(itemList)
    }

    public fun setItems(itemList: List<ITEM>) {
        differ.submitList(itemList)
    }

    /** AddItem **/
    public fun addItem(item: ITEM) {
        val itemList = getMutableItemList().run { add(item); this }
        differ.submitList(itemList)
    }

    public fun addItem(position: Int, item: ITEM) {
        val newItemList = getMutableItemList().run { add(position, item); this }
        differ.submitList(newItemList)
    }

    public fun addItems(itemList: List<ITEM>) {
        val newItemList = getMutableItemList().run { addAll(itemList); this }
        differ.submitList(newItemList)
    }

    /** Remove Item **/
    public fun removeAtItem(item: ITEM) {
        val position = getItems().indexOf(item)
        if (!isPositionValid(position)) {
            Logx.e("Can not remove item, position is $position")
        }
        removeAtItem(position)
    }

    protected fun isPositionValid(position: Int): Boolean =
        (position > RecyclerView.NO_POSITION && position < itemCount)

    public fun removeAtItem(position: Int) {
        if (!isPositionValid(position)) {
            Logx.e("Can not remove item, position is $position")
            return
        }
        val newItemList = getMutableItemList().run { removeAt(position); this }
        differ.submitList(newItemList)
    }

    public fun removeAll() {
//        differ.submitList(listOf<ITEM>())
        differ.submitList(null)
    }

    /** Move **/
    public fun moveItem(fromPosition: Int, toPosition: Int) {
        val newItemList = getMutableItemList().run {
            Collections.swap(this, fromPosition, toPosition)
            this
        }
        differ.submitList(newItemList)
    }

    public fun setOnItemClickListener(listener: (Int, ITEM, View) -> Unit) { onItemClickListener = listener }

    public fun setOnItemLongClickListener(listener: (Int, ITEM, View) -> Unit) { onItemLongClickListener = listener }

    override fun onBindViewHolder(holder: VH, position: Int) {

        val item = getItem(position)

        holder.itemView.setOnClickListener { onItemClickListener?.invoke(position, item, it) }
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.invoke(position, item, it)
            true
        }

        onBindViewHolder(holder, position, item)
    }
}