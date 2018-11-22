package io.untaek.animal_new.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.untaek.animal_new.databinding.TabTimelineBinding


class TimelineFragment: Fragment() {

    lateinit var binding: TabTimelineBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TabTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun instance(): Fragment {
            return TimelineFragment()
        }
    }
}