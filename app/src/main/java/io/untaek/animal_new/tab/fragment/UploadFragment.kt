package io.untaek.animal_new.tab.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.untaek.animal_new.databinding.TabUploadBinding
import io.untaek.animal_new.list.upload.UploadAdapter
import io.untaek.animal_new.viewmodel.UploadViewModel

class UploadFragment: Fragment() {

    lateinit var vm: UploadViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(this.requireActivity()).get(UploadViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = TabUploadBinding.inflate(inflater, container, false)
        binding.uploadStatusContainer.adapter = UploadAdapter(this.requireActivity())
        binding.uploadStatusContainer.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    companion object {
        fun instance(): Fragment {
            return UploadFragment()
        }
    }
}