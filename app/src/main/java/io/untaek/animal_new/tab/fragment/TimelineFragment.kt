package io.untaek.animal_new.tab.fragment

import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import `in`.srain.cube.views.ptr.PtrHandler
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.untaek.animal_new.databinding.TabTimelineBinding
import io.untaek.animal_new.list.timeline.TimelineAdapter
import io.untaek.animal_new.list.timeline.TimelineDecorator
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
        val adapter = TimelineAdapter(this.requireActivity())
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager::VERTICAL.get(), false)

        binding.vm = ViewModelProviders
            .of(this.requireActivity())
            .get(TimelineViewModel::class.java)

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(TimelineDecorator())
        binding.recyclerView.addOnScrollListener(scrollListener)

        binding.ptrLayout.setPtrHandler(refreshHandler)
        binding.vm!!.refreshState.observe(this, Observer { loading ->
                if(loading == false)
                    binding.ptrLayout.refreshComplete()
        })
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        var visibleItemCount = 0
        var totalItemCount = 0
        var pastVisibleItems = 0
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if(dy > 0)
            {
                visibleItemCount = recyclerView.layoutManager?.childCount!!
                totalItemCount = recyclerView.layoutManager?.itemCount!!
                pastVisibleItems = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                if(binding.vm?.refreshState?.value == false){
                    if(visibleItemCount + pastVisibleItems >= totalItemCount - 5) {
                        binding.vm!!.loadMore()
                    }
                }
            }
        }
    }

    private val refreshHandler = object :PtrHandler{
        override fun onRefreshBegin(frame: PtrFrameLayout?) {
            binding.vm?.refreshTimeline()
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