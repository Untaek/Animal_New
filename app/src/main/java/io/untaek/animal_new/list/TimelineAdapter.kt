package io.untaek.animal_new.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import io.untaek.animal_new.databinding.ItemListTimelineBinding
import io.untaek.animal_new.type.Post

class TimelineAdapter: RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {
    private lateinit var binding: ItemListTimelineBinding
    private var items: ObservableArrayList<Post> = ObservableArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineAdapter.ViewHolder {
        binding = ItemListTimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TimelineAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(list: ObservableArrayList<Post>) {
        items = list
        notifyDataSetChanged()
    }

    open class ViewHolder(private val binding: ItemListTimelineBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Post) {
            binding.post = item
        }
    }
}