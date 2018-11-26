package io.untaek.animal_new.list

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import io.untaek.animal_new.activity.postdetail.PostDetailActivity
import io.untaek.animal_new.activity.UserDetailActivity
import io.untaek.animal_new.databinding.ItemListTimelineBinding
import io.untaek.animal_new.type.Post
import io.untaek.animal_new.viewmodel.TimelineViewModel
import kotlin.collections.ArrayList

class TimelineAdapter(fragmentActivity: FragmentActivity): RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {

    val vm = ViewModelProviders.of(fragmentActivity).get(TimelineViewModel::class.java)
    private var items = listOf<Post>()

    init {
         vm.loadPosts(20, null)
             .observe(fragmentActivity, Observer {
             setItems(it)
         })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineAdapter.ViewHolder {
        val binding = ItemListTimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            handler = Handler(vm)
        }
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TimelineAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(list: List<Post>) {
        items = list
        notifyDataSetChanged()
    }

    open class ViewHolder(private val binding: ItemListTimelineBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Post) {
            binding.post = item
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