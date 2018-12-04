package io.untaek.animal_new.activity

import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import `in`.srain.cube.views.ptr.PtrHandler
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.untaek.animal_new.R
import io.untaek.animal_new.databinding.TabMyPageBinding
import io.untaek.animal_new.list.mypage.MyPageAdapter
import io.untaek.animal_new.type.User
import io.untaek.animal_new.type.UserDetail
import io.untaek.animal_new.viewmodel.MyPageViewModel

class UserDetailActivity: AppCompatActivity() {
    lateinit var binding : TabMyPageBinding
    lateinit var userdetail : UserDetail
    lateinit var user : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = intent.getSerializableExtra("user") as User

        binding = DataBindingUtil.setContentView(this, R.layout.tab_my_page)
        binding.vm = ViewModelProviders.of(this, MyPageViewModel.MyPageViewModelFactory(user)).get(MyPageViewModel::class.java)

        val pageAdapter = MyPageAdapter(this)
        val layoutManager = GridLayoutManager(this, 3)

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
}