package com.example.mymaterialdesign.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import com.example.mymaterialdesign.*
import com.example.mymaterialdesign.databinding.FragmentSettingBinding


class SettingFragment : Fragment(), View.OnClickListener {

    private lateinit var parentActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = requireActivity() as MainActivity
    }

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.RBLimeStyle.setOnClickListener(this)
        binding.RBOrangeStyle.setOnClickListener(this)

        when (parentActivity.getCurrentTheme()) {
            1 -> binding.radioGroupStyle.check(R.id.RBLimeStyle)
            2 -> binding.radioGroupStyle.check(R.id.RBOrangeStyle)

        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.RBLimeStyle -> {
                parentActivity.setCurrentTheme(ThemeLime)
                parentActivity.recreate()
            }

            R.id.RBOrangeStyle -> {
                parentActivity.setCurrentTheme(ThemeOrange)
                parentActivity.recreate()
            }
        }
    }
}