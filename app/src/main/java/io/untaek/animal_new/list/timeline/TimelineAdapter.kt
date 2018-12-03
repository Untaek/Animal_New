package io.untaek.animal_new.list.timeline

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import io.untaek.animal_new.Reactive
import io.untaek.animal_new.activity.postdetail.PostDetailActivity
import io.untaek.animal_new.activity.UserDetailActivity
import io.untaek.animal_new.databinding.ItemListTimelineBinding
import io.untaek.animal_new.tab.fragment.MyPageFragment
import io.untaek.animal_new.tab.fragment.TimelineFragment
import io.untaek.animal_new.type.Post
import io.untaek.animal_new.viewmodel.TimelineViewModel

class TimelineAdapter(fragmentActivity: FragmentActivity): RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {
    val vm = ViewModelProviders.of(fragmentActivity).get(TimelineViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListTimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            handler = Handler(vm)
        }
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return vm.timeline.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(vm.timeline[position])
    }

    /**
     * ViewHolder
     */
    open class ViewHolder(private val binding: ItemListTimelineBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Post) {
            binding.post = item
        }
    }

    /**
     * Handler
     */
    open class Handler(val vm: TimelineViewModel) {

        fun onClickUserImageAndName(view: View, post: Post) {
            Toast.makeText(view.context, "onClickUserImageAndName", Toast.LENGTH_SHORT).show()
            val intent = Intent(view.context, UserDetailActivity::class.java).apply {
                putExtra("user", post.user)
            }
            view.context.startActivity(intent)
        }
        fun onClickLike(view: View, post: Post) {
            Toast.makeText(view.context, "onClickLike", Toast.LENGTH_SHORT).show()
            val i = vm.timeline.indexOf(post)
            Reactive.like(post).subscribe {
                vm.timeline[i] = it.post
            }
        }
        fun onClickContent(view: View, post: Post) {
            Toast.makeText(view.context, "onClickContent", Toast.LENGTH_SHORT).show()
            val intent = Intent(view.context, PostDetailActivity::class.java).apply {
                putExtra("post", post)
            }
            view.context.startActivity(intent)
        }
        fun onClickComment(view: View, post: Post) {

        }
    }

}