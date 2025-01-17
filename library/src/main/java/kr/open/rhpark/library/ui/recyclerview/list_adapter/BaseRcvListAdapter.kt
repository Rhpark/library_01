package kr.open.rhpark.library.ui.recyclerview.list_adapter

import android.view.View
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.open.rhpark.library.debug.logcat.Logx
import java.util.Collections

public abstract class BaseRcvListAdapter<ITEM, VH : RecyclerView.ViewHolder>(listDiffUtil :RcvListDifUtilCallBack<ITEM>) :
    ListAdapter<ITEM, VH>(listDiffUtil) {

    private val differ = AsyncListDiffer<ITEM>(this, listDiffUtil)

    private var onItemClickListener: ((Int, ITEM, View) -> Unit)? = null
    private var onItemLongClickListener: ((Int, ITEM, View) -> Unit)? = null

    protected abstract fun onBindViewHolder(holder: VH, position: Int, item: ITEM)

    public fun getItems(): MutableList<ITEM> = differ.currentList

    override fun getItemCount(): Int = getItems().size

    public override fun getItem(position: Int): ITEM = getItems()[position]

    protected fun getMutableItemList(): MutableList<ITEM> = differ.currentList.toMutableList()

    public fun setItems(itemList: List<ITEM>) { differ.submitList(itemList) }

    /** AddItem **/
    public fun addItem(item: ITEM) {
        val itemList = getMutableItemList().apply { add(item); }
        differ.submitList(itemList)
    }

    public fun addItem(position: Int, item: ITEM) {
        val newItemList = getMutableItemList().apply { add(position, item); }
        differ.submitList(newItemList)
    }

    public fun addItems(itemList: List<ITEM>) {
        val newItemList = getMutableItemList().apply { addAll(itemList); }
        differ.submitList(newItemList)
    }

    protected fun isPositionValid(position: Int): Boolean = (position > RecyclerView.NO_POSITION && position < itemCount)

    /** Remove Item **/
    public fun removeAt(item: ITEM) {
        val position = getItems().indexOf(item)
        if (!isPositionValid(position)) {
            Logx.e("Can not remove item, position is $position")
        }
        removeAt(position)
    }

    public fun removeAt(position: Int) {
        if (!isPositionValid(position)) {
            Logx.e("Can not remove item, position is $position")
            return
        }
        val newItemList = getMutableItemList().apply { removeAt(position); }
        differ.submitList(newItemList)
    }

    public fun removeAll() {
        differ.submitList(emptyList())
    }

    /** Move **/
    public fun moveItem(fromPosition: Int, toPosition: Int) {
        differ.submitList(
            getMutableItemList().apply {
                Collections.swap(this, fromPosition, toPosition)
            }
        )
    }

    public fun setOnItemClickListener(listener: (Int, ITEM, View) -> Unit) { onItemClickListener = listener }

    public fun setOnItemLongClickListener(listener: (Int, ITEM, View) -> Unit) { onItemLongClickListener = listener }

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
}