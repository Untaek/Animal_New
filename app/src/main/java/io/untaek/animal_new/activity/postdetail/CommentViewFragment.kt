package io.untaek.animal_new.activity.postdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.untaek.animal_new.databinding.FragmentCommentViewBinding
import io.untaek.animal_new.list.CommentsAdapter
import io.untaek.animal_new.list.CommentsDecoration
import io.untaek.animal_new.viewmodel.PostDetailViewModel

class CommentViewFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentCommentViewBinding.inflate(inflater, container, false)
        binding.vm = ViewModelProviders.of(this.requireActivity()).get(PostDetailViewModel::class.java)
        binding.recyclerView.adapter = CommentsAdapter(this.requireActivity())
        binding.recyclerView.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager::VERTICAL.get(), false)
        binding.recyclerView.addItemDecoration(CommentsDecoration())
        return binding.root
    }

    companion object {
        fun instance(): Fragment {
            return CommentViewFragment()
        }
    }
}