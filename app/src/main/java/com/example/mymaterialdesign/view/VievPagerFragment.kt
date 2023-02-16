package com.example.mymaterialdesign.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.mymaterialdesign.*
import com.example.mymaterialdesign.databinding.FragmentSettingBinding
import com.example.mymaterialdesign.databinding.FragmentViewPagerBinding
import com.example.mymaterialdesign.navigation.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class VievPagerFragment : Fragment() {

    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = ViewPagerAdapter(this)
        setTabs()
    }

    private fun setTabs() {
        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager,
            object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    tab.text = when (position) {
                        0 -> {
                            "Earth"
                        }
                        1 -> {
                            "Mars"
                        }
                        2 -> {
                            "Systtem"
                        }
                        else -> "None"
                    }
                }
            }).attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
