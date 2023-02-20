package com.example.mymaterialdesign.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fr: Fragment) : FragmentStateAdapter(fr) {
    private val fragments = arrayOf(EarthFragment(), MarsFragment(),
        SystemFragment(), LessonFourFragment())
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> fragments[EARTH_FRAGMENT]
            1 -> fragments[MARS_FRAGMENT]
            2 -> fragments[SYSTEM_FRAGMENT]
            3 -> fragments[LESSON_FOUR_FRAGMENT]
            else -> fragments[EARTH_FRAGMENT]
        }
    }
    override fun getItemCount(): Int {
        return fragments.size
    }
    companion object {
        private const val EARTH_FRAGMENT = 0
        private const val MARS_FRAGMENT = 1
        private const val SYSTEM_FRAGMENT = 2
        private const val LESSON_FOUR_FRAGMENT = 3
    }
}
