package io.untaek.animal_new.tab

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.lang.IllegalStateException

class MainFragmentAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    override fun getItem(position: Int): Fragment {
        Log.d("MainFragmentAdapter", position.toString())
        return when(position) {
            0 -> TimelineFragment.instance()
            1 -> RankFragment.instance()
            2 -> UploadFragment.instance()
            3 -> MyPageFragment.instance()
            else -> throw IllegalStateException("Wrong tab state")
        }
    }

    override fun getCount(): Int = TAB_COUNT

    companion object {
        private const val TAB_COUNT = 4
    }
}