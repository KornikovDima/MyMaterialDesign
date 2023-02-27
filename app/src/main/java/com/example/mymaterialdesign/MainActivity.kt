package com.example.mymaterialdesign

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.mymaterialdesign.R
import com.example.mymaterialdesign.databinding.ActivityMainBinding
import com.example.mymaterialdesign.navigation.ViewPagerAdapter
import com.example.mymaterialdesign.recycler.RecyclerFragment
import com.example.mymaterialdesign.view.PictureOfTheDayFragment
import com.example.mymaterialdesign.view.SettingFragment
import com.example.mymaterialdesign.view.VievPagerFragment

const val ThemeLime = 1
const val ThemeOrange = 2

class MainActivity : AppCompatActivity() {

    private val KEY_SP = "sp"
    private val KEY_CURRENT_THEME = "current_theme"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getRealStyle(getCurrentTheme()))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commit()
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuPicturesOfTheDay -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                        .commit()
                    true
                }
                R.id.menuViewPager -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, VievPagerFragment())
                        .commit()
                    true
                }
                R.id.menuSetting -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, SettingFragment())
                        .commit()
                    true
                }
                R.id.menuRecycler -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, RecyclerFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

    fun setCurrentTheme(currentTheme: Int) {
        val sharedPreferences = getSharedPreferences(KEY_SP, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_CURRENT_THEME, currentTheme)
        editor.apply()
    }

    fun getCurrentTheme(): Int {
        val sharedPreferences = getSharedPreferences(KEY_SP, MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_CURRENT_THEME, -1)
    }

    private fun getRealStyle(currentTheme: Int): Int {
        return when (currentTheme) {
            ThemeLime -> R.style.LimeTheme
            ThemeOrange -> R.style.OrangeTheme
            else -> 0
        }
    }
}
