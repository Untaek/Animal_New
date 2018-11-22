package io.untaek.animal_new

import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import io.untaek.animal_new.list.TimelineAdapter
import io.untaek.animal_new.type.Post

@BindingAdapter("binding")
fun bind(recyclerView: RecyclerView, items: ObservableArrayList<Post>){
    val adapter: TimelineAdapter = recyclerView.adapter as? TimelineAdapter ?: TimelineAdapter()
    adapter.setItems(items)
}
