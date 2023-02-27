package com.example.mymaterialdesign.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mymaterialdesign.R
import com.example.mymaterialdesign.databinding.FragmentRecyclerItemEarthBinding
import com.example.mymaterialdesign.databinding.FragmentRecyclerItemHeaderBinding
import com.example.mymaterialdesign.databinding.FragmentRecyclerItemMarsBinding
import com.example.mymaterialdesign.recycler.diffutil.Change
import com.example.mymaterialdesign.recycler.diffutil.DiffUtilCallback
import com.example.mymaterialdesign.recycler.diffutil.createCombinedPayload

class RecyclerAdapter(
    private var listData: MutableList<Pair<Data, Boolean>>,
    val callbackAdd: AddItems,
    val callbackRemove: RemoveItems
) :
    RecyclerView.Adapter<RecyclerAdapter.BaseHeaderViewHolder>(), ItemTouchHelperAdapter {

    fun setListDataForDiffUtil(listDataNew: MutableList<Pair<Data, Boolean>>) {
        val diff = DiffUtil.calculateDiff(DiffUtilCallback(listData, listDataNew))
        diff.dispatchUpdatesTo(this)
        listData = listDataNew
    }

    fun setListDataAdd(listDataNew: MutableList<Pair<Data, Boolean>>, position: Int) {
        listData = listDataNew
        notifyItemInserted(position)
    }

    fun setListDataRemove(listDataNew: MutableList<Pair<Data, Boolean>>, position: Int) {
        listData = listDataNew
        notifyItemRemoved(position)
    }

    override fun getItemViewType(position: Int): Int {
        return listData[position].first.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHeaderViewHolder {
        return when (viewType) {
            TYPE_ERTHS -> {
                val binding =
                    FragmentRecyclerItemEarthBinding.inflate(LayoutInflater.from(parent.context))
                return EarthViewHolder(binding)
            }
            TYPE_MARS -> {
                val binding =
                    FragmentRecyclerItemMarsBinding.inflate(LayoutInflater.from(parent.context))
                return MarsViewHolder(binding)
            }
            else -> {
                val binding =
                    FragmentRecyclerItemHeaderBinding.inflate(LayoutInflater.from(parent.context))
                return HeaderViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: BaseHeaderViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun onBindViewHolder(
        holder: BaseHeaderViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val createCombinedPayload =
                createCombinedPayload(payloads as List<Change<Pair<Data, Boolean>>>)
            if (createCombinedPayload.newData.first.name != createCombinedPayload.oldData.first.name)
                holder.itemView.findViewById<TextView>(R.id.marsHeader).text =
                    createCombinedPayload.newData.first.name
        }
    }

    inner class MarsViewHolder(val binding: FragmentRecyclerItemMarsBinding) :
        BaseHeaderViewHolder(binding.root) {
        override fun bind(data: Pair<Data, Boolean>) {
            binding.marsHeader.text = data.first.name
            binding.addItemImageView.setOnClickListener {
                callbackAdd.add(layoutPosition)
            }
            binding.removeItemImageView.setOnClickListener {
                callbackRemove.remove(layoutPosition)
            }
            binding.moveItemUp.setOnClickListener {
                layoutPosition.takeIf { it > 1 }?.also {
                    listData.removeAt(layoutPosition).apply {
                        listData.add(layoutPosition - 1, this)
                    }
                    notifyItemMoved(layoutPosition, layoutPosition - 1)
                }
            }
            binding.moveItemDown.setOnClickListener {
                layoutPosition.takeIf { it < listData.size - 1 }?.also {
                    listData.removeAt(layoutPosition).apply {
                        listData.add(layoutPosition + 1, this)
                    }
                    notifyItemMoved(layoutPosition, layoutPosition + 1)
                }
            }
            binding.marsDescriptionTextView.visibility =
                if (listData[layoutPosition].second) View.VISIBLE else View.GONE
            binding.marsImageView.setOnClickListener {
                listData[layoutPosition] = listData[layoutPosition].let {
                    it.first to !it.second
                }
                notifyItemChanged(layoutPosition)
            }
        }


    }

    class EarthViewHolder(val binding: FragmentRecyclerItemEarthBinding) :
        BaseHeaderViewHolder(binding.root) {
        override fun bind(data: Pair<Data, Boolean>) {
            binding.earthHeader.text = data.first.name
        }
    }

    class HeaderViewHolder(val binding: FragmentRecyclerItemHeaderBinding) :
        BaseHeaderViewHolder(binding.root) {
        override fun bind(data: Pair<Data, Boolean>) {
            binding.headerHeader.text = data.first.name
        }
    }

    abstract class BaseHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view),
        ItemTouchHelperViewHolder {
        abstract fun bind(data: Pair<Data, Boolean>)
        override fun onItemSelect() {
            itemView.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.color_gray
                )
            )
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        listData.removeAt(fromPosition).apply {
            listData.add(toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        callbackRemove.remove(position)
    }
}