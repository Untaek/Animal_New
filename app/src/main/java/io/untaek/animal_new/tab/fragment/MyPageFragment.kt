package io.untaek.animal_new.tab.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import io.untaek.animal_new.databinding.TabMyPageBinding
import io.untaek.animal_new.list.mypage.MyPageAdapter
import io.untaek.animal_new.type.User
import io.untaek.animal_new.type.UserDetail

class MyPageFragment: Fragment() {

    lateinit var binding: TabMyPageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = TabMyPageBinding.inflate(inflater, container,false)
        binding.user = UserDetail(User())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pageAdapter = MyPageAdapter(this.requireActivity())
        val layoutManager = GridLayoutManager(context, 3)
        binding.recyclerViewMyPage.layoutManager = layoutManager
        binding.recyclerViewMyPage.adapter = pageAdapter

    }

    companion object {
        fun instance(): Fragment {
            return MyPageFragment()
        }
    }
}