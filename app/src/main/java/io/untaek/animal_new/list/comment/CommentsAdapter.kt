package io.untaek.animal_new.list.comment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import io.untaek.animal_new.activity.UserDetailActivity
import io.untaek.animal_new.databinding.ItemListCommentBinding
import io.untaek.animal_new.type.Comment
import io.untaek.animal_new.viewmodel.PostDetailViewModel

class CommentsAdapter(fragmentActivity: FragmentActivity): RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    val vm: PostDetailViewModel = ViewModelProviders.of(fragmentActivity).get(PostDetailViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.handler = Handler()
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return vm.comments.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(vm.comments[position])
    }

    open class ViewHolder(val binding: ItemListCommentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            binding.comment = comment
        }
    }

    class Handler {
        fun onClickUserImageAndName(view: View, comment: Comment) {
            Toast.makeText(view.context, "onClickUserImageAndName", Toast.LENGTH_SHORT).show()

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(comment.user.id)
                .get()
                .addOnSuccessListener {
                    Toast.makeText(view.context, it.id, Toast.LENGTH_SHORT).show()
                }

            val intent = Intent(view.context, UserDetailActivity::class.java).apply {
                putExtra("userDetail", comment.user)
            }
            view.context.startActivity(intent)
        }
        fun onClickLike(view: View, comment: Comment) {
            Toast.makeText(view.context, "onClickLike", Toast.LENGTH_SHORT).show()
        }
    }
}