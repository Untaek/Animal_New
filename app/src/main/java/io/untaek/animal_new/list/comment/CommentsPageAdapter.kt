package io.untaek.animal_new.list.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.untaek.animal_new.databinding.ItemListCommentBinding
import io.untaek.animal_new.type.Comment
import io.untaek.animal_new.type.Post
import io.untaek.animal_new.viewmodel.PostDetailViewModel
import io.untaek.animal_new.viewmodel.TimelineViewModel

@Deprecated("Updating Specific item is not working")
class CommentsPageAdapter(fragmentActivity: FragmentActivity): PagedListAdapter<Comment, CommentsPageAdapter.ViewHolder>(DIFF_CALLBACK) {

    val vm = ViewModelProviders.of(fragmentActivity).get(PostDetailViewModel::class.java)

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Comment>(){
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.user == newItem.user && oldItem.time_stamp == newItem.time_stamp
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    open class ViewHolder(val binding: ItemListCommentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            binding.comment = comment
        }
    }
}