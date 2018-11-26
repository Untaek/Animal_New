package io.untaek.animal_new.activity.postdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.untaek.animal_new.R
import io.untaek.animal_new.databinding.FragmentContentViewBinding
import io.untaek.animal_new.viewmodel.PostDetailViewModel

class ContentViewFragment: Fragment() {
    lateinit var binding: FragmentContentViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_content_view, container, false)
        binding.vm = ViewModelProviders.of(this.requireActivity()).get(PostDetailViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun instance(): Fragment {
            return ContentViewFragment()
        }
    }
}