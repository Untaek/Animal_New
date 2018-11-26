package io.untaek.animal_new.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import io.untaek.animal_new.databinding.ItemListCommentBinding
import io.untaek.animal_new.type.Comment
import io.untaek.animal_new.viewmodel.PostDetailViewModel

class CommentsAdapter(fragmentActivity: FragmentActivity): RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    val vm: PostDetailViewModel = ViewModelProviders.of(fragmentActivity).get(PostDetailViewModel::class.java)
    private val items: ArrayList<Comment> = arrayListOf()

    init {
        vm.comments.observeForever {
            items.addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    open class ViewHolder(val binding: ItemListCommentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            binding.comment = comment
        }
    }

    class Handler {
        fun onNameClicked(){}
        fun onLikeClicked(){}
    }
}