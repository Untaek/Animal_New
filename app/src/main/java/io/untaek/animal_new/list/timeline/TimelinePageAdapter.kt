package io.untaek.animal_new.list.timeline

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import io.untaek.animal_new.activity.UserDetailActivity
import io.untaek.animal_new.activity.postdetail.PostDetailActivity
import io.untaek.animal_new.databinding.ItemListTimelineBinding
import io.untaek.animal_new.type.Post
import io.untaek.animal_new.viewmodel.TimelineViewModel

class TimelinePageAdapter(fragmentActivity: FragmentActivity): PagedListAdapter<Post, TimelinePageAdapter.ViewHolder>(
    DIFF_CALLBACK
) {
    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Post>(){
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }
    }

    val vm = ViewModelProviders.of(fragmentActivity).get(TimelineViewModel::class.java)

    init {
        vm.pagedTimeline.observe(fragmentActivity, Observer {
            vm.refreshState.postValue(false)
            submitList(it)
        })
    }

    fun refresh() {
        vm.refresh()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListTimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.handler = Handler(vm)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if(item != null) {
            holder.bind(item)
        }
        else {
            holder.clear()
        }
    }

    open class ViewHolder(val binding: ItemListTimelineBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(post: Post){
            binding.post = post
        }

        fun clear() {
            binding.post = Post()
        }
    }

    class Handler(val vm: TimelineViewModel) {
        fun onClickUserImageAndName(view: View, post: Post) {
            Toast.makeText(view.context, "onClickUserImageAndName", Toast.LENGTH_SHORT).show()
            val start = System.currentTimeMillis()

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(post.user.id)
                .get()
                .addOnSuccessListener {
                    val finish = System.currentTimeMillis() - start
                    Toast.makeText(view.context, "${it.id}, it takes $finish ms", Toast.LENGTH_SHORT).show()
                }

            val intent = Intent(view.context, UserDetailActivity::class.java).apply {
                putExtra("user", post.user)
            }
            view.context.startActivity(intent)
        }
        fun onClickLike(view: View, post: Post) {

            Toast.makeText(view.context, "onClickLike", Toast.LENGTH_SHORT).show()
        }
        fun onClickContent(view: View, post: Post) {
            Toast.makeText(view.context, "onClickContent", Toast.LENGTH_SHORT).show()

            val intent = Intent(view.context, PostDetailActivity::class.java).apply {
                putExtra("post", post)
            }
            view.context.startActivity(intent)
        }
    }
}