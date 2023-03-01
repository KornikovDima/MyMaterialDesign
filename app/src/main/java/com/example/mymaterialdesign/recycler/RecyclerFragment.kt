package com.example.mymaterialdesign.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.mymaterialdesign.databinding.FragmentRecyclerBinding

class RecyclerFragment : Fragment() {

    private var _binding: FragmentRecyclerBinding? = null
    private val binding get() = _binding!!

    private val data = arrayListOf(

        Pair(Data(id = 0, "Mars", type = TYPE_MARS), false),
        Pair(Data(id = 1, "Mars", type = TYPE_MARS), false),
        Pair(Data(id = 2, "Mars", type = TYPE_MARS), false),
        Pair(Data(id = 3, "Earth", type = TYPE_ERTHS), false),
        Pair(Data(id = 4, "Mars", type = TYPE_MARS), false),
        Pair(Data(id = 5, "Mars", type = TYPE_MARS), false),
    )
    private var isNewList = false
    lateinit var adapter: RecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecyclerAdapter(data, callbackAdd, callbackRemove)
        binding.recyclerView.adapter = adapter

        ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(binding.recyclerView)

        binding.recyclerDiffUtilFAB.setOnClickListener {
            changeAdapterData()
        }
    }

    private val callbackAdd = AddItems {
        data.add(it, Pair(Data(0, "Mars(New)", type = TYPE_MARS), false))
        adapter.setListDataAdd(data, it)
    }
    private val callbackRemove = RemoveItems {
        data.removeAt(it)
        adapter.setListDataRemove(data, it)
    }

    private fun changeAdapterData() {
        adapter.setListDataForDiffUtil(createItemList(isNewList).map { it }.toMutableList())
        isNewList = !isNewList
    }

    private fun createItemList(instanceNumber: Boolean): List<Pair<Data, Boolean>> {
        return when (instanceNumber) {
            false -> listOf(
                Pair(Data(0, "Header", type = TYPE_HEADER), false),
                Pair(Data(1, "Mars", ""), false),
                Pair(Data(2, "Mars", ""), false),
                Pair(Data(3, "Mars", ""), false),
                Pair(Data(4, "Mars", ""), false),
                Pair(Data(5, "Mars", ""), false),
                Pair(Data(6, "Mars", ""), false)
            )
            true -> listOf(
                Pair(Data(0, "Header"), false),
                Pair(Data(1, "Mars", ""), false),
                Pair(Data(2, "Jupiter", ""), false),
                Pair(Data(3, "Mars", ""), false),
                Pair(Data(4, "Neptune", ""), false),
                Pair(Data(5, "Saturn", ""), false),
                Pair(Data(6, "Mars", ""), false)
            )
        }
    }
}