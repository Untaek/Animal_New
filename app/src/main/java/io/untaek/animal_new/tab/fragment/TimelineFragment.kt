package io.untaek.animal_new.tab.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.untaek.animal_new.databinding.TabTimelineBinding
import io.untaek.animal_new.list.timeline.TimelineDecorator
import io.untaek.animal_new.list.timeline.TimelinePageAdapter


class TimelineFragment: Fragment() {

    lateinit var binding: TabTimelineBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = TabTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val adapter = TimelineAdapter(this.requireActivity())
        val pagedAdapter = TimelinePageAdapter(this.requireActivity())
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager::VERTICAL.get(), false)

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = pagedAdapter
        binding.recyclerView.addItemDecoration(TimelineDecorator())
    }

    companion object {
        fun instance(): Fragment {
            return TimelineFragment()
        }
    }
}