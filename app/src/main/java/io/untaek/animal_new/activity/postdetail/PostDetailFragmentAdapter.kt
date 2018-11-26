package io.untaek.animal_new.activity.postdetail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.lang.IllegalStateException

class PostDetailFragmentAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> ContentViewFragment.instance()
            1 -> CommentViewFragment.instance()
            else -> throw IllegalStateException("Wrong item")
        }
    }

    override fun getCount(): Int {
        return 2
    }
}