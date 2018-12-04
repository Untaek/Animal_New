package io.untaek.animal_new.tab.fragment

import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import `in`.srain.cube.views.ptr.PtrHandler
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.untaek.animal_new.Reactive
import io.untaek.animal_new.databinding.TabMyPageBinding
import io.untaek.animal_new.list.mypage.MyPageAdapter
import io.untaek.animal_new.type.User
import io.untaek.animal_new.type.UserDetail
import io.untaek.animal_new.viewmodel.MyPageViewModel

class MyPageFragment: Fragment() {

    lateinit var binding: TabMyPageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = TabMyPageBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = ViewModelProviders
            .of(this.requireActivity(), MyPageViewModel.MyPageViewModelFactory(Reactive.currentUser()))
            .get(MyPageViewModel::class.java)


        val pageAdapter = MyPageAdapter(this.requireActivity())             // my page
        val layoutManager = GridLayoutManager(context, 3)

        binding.recyclerViewMyPage.layoutManager = layoutManager
        binding.recyclerViewMyPage.adapter = pageAdapter
        binding.recyclerViewMyPage.addOnScrollListener(scrollListener)

        binding.ptrLayoutMypage.setPtrHandler(refreshHandler)
        binding.vm!!.refreshState.observe(this, Observer { loading ->
            if(loading == false)
                binding.ptrLayoutMypage.refreshComplete()
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

    private val refreshHandler = object : PtrHandler {
        override fun onRefreshBegin(frame: PtrFrameLayout?) {
            binding.vm?.refreshMyPage()
        }
        override fun checkCanDoRefresh(frame: PtrFrameLayout?, content: View?, header: View?): Boolean {
            return PtrDefaultHandler.checkContentCanBePulledDown(frame, binding.recyclerViewMyPage, header)
        }
    }


    companion object {
        fun instance(): Fragment {
            return MyPageFragment()
        }
    }
}