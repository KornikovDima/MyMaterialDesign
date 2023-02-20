package com.example.mymaterialdesign.navigation

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mymaterialdesign.databinding.FragmentLessonFourLayoutsBinding

class LessonFourFragment: Fragment() {


    private var _binding: FragmentLessonFourLayoutsBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLessonFourLayoutsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swichVisibility.isChecked = true
        binding.swichVisibility.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.groupVisibl.visibility = if (isChecked == true) View.VISIBLE else View.GONE
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = LessonFourFragment()
    }
}