package io.untaek.animal_new.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import io.untaek.animal_new.R
import io.untaek.animal_new.databinding.ActivityMainBinding
import io.untaek.animal_new.tab.MainFragmentAdapter
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem
import me.majiajie.pagerbottomtabstrip.item.NormalItemView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewPager.adapter = MainFragmentAdapter(supportFragmentManager)
        initNavigation()
    }

    private fun initNavigation() {
        val nav = binding.tab.custom()
            .addItem(tabItem(
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_background, "Timeline"))
            .addItem(tabItem(
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_background, "Rank"))
            .addItem(tabItem(
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_background, "Upload"))
            .addItem(tabItem(
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_background, "My Page"))
            .build()

        nav.addSimpleTabItemSelectedListener { index, _ ->
            changeTab(index)
        }
    }

    private fun tabItem(drawable: Int, checkedDrawable: Int, text: String): BaseTabItem {
        return NormalItemView(this).apply {
            initialize(drawable, checkedDrawable, text)
        }
    }

    private fun changeTab(index: Int) {
        binding.viewPager.setCurrentItem(index, false)
    }
}
