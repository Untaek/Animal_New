package io.untaek.animal_new.list.upload

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import io.untaek.animal_new.Reactive
import io.untaek.animal_new.databinding.ItemListUploadBinding
import io.untaek.animal_new.type.Uploading
import io.untaek.animal_new.viewmodel.UploadViewModel

class UploadAdapter(fragmentActivity: FragmentActivity): RecyclerView.Adapter<UploadAdapter.ViewHolder>() {
    private val vm: UploadViewModel = ViewModelProviders.of(fragmentActivity).get(UploadViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =  ItemListUploadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return vm.uploadingList.get()?.size() ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(vm.uploadingList.get()?.valueAt(position)!!)
    }

    open class ViewHolder(val binding: ItemListUploadBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(uploadState: Reactive.UploadState) {
            binding.uploadState = uploadState
        }
    }
}