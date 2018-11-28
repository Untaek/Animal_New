package io.untaek.animal_new.tab.fragment

import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import `in`.srain.cube.views.ptr.PtrHandler
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.untaek.animal_new.databinding.TabTimelineBinding
import io.untaek.animal_new.list.timeline.TimelineDecorator
import io.untaek.animal_new.list.timeline.TimelinePageAdapter
import io.untaek.animal_new.viewmodel.TimelineViewModel


class TimelineFragment: Fragment() {

    lateinit var binding: TabTimelineBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = TabTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagedAdapter = TimelinePageAdapter(this.requireActivity())
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager::VERTICAL.get(), false)

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = pagedAdapter
        binding.recyclerView.addItemDecoration(TimelineDecorator())
        binding.ptrLayout.setPtrHandler(refreshHandler)

        ViewModelProviders
            .of(this.requireActivity())
            .get(TimelineViewModel::class.java)
            .refreshState.observe(this, Observer { loading ->
                if(loading == false)
                    binding.ptrLayout.refreshComplete()
        })
    }

    private val refreshHandler = object :PtrHandler{
        override fun onRefreshBegin(frame: PtrFrameLayout?) {
            (binding.recyclerView.adapter as TimelinePageAdapter).refresh()
        }
        override fun checkCanDoRefresh(frame: PtrFrameLayout?, content: View?, header: View?): Boolean {
            return PtrDefaultHandler.checkContentCanBePulledDown(frame, binding.recyclerView, header)
        }
    }

    companion object {
        fun instance(): Fragment {
            return TimelineFragment()
        }
    }
}