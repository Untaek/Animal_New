package io.untaek.animal_new.tab.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.untaek.animal_new.databinding.TabUploadBinding
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.uploadingState.observeForever {
            Log.d("UploadViewModel", "Observing")
            Log.d("UploadViewModel", it.toString())


        }
    }

    companion object {
        fun instance(): Fragment {
            return UploadFragment()
        }
    }
}