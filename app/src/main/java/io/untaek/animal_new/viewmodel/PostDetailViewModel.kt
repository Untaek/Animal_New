package io.untaek.animal_new.viewmodel

import android.Manifest
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.untaek.animal_new.list.comment.CommentsPageDataSource
import io.untaek.animal_new.list.timeline.TimelinePageDataSource
import io.untaek.animal_new.type.Comment
import io.untaek.animal_new.type.Content
import io.untaek.animal_new.type.Post
import io.untaek.animal_new.type.User
import io.untaek.animal_new.util.PermissionHelper
import java.io.File
import java.util.*

class PostDetailViewModel(val post: Post): BaseViewModel() {
    val comments = MutableLiveData<ArrayList<Comment>>()

    fun loadDummyComments() {
        comments.postValue(arrayListOf(
            Comment(User(), "comment comment comment this is comment", Date()),
            Comment(User(), "comment comment comment this is comment", Date()),
            Comment(User(), "comment comment comment this is comment", Date()),
            Comment(User(), "comment comment comment this is comment", Date()),
            Comment(User(), "comment comment comment this is comment", Date()),
            Comment(User(), "comment comment comment this is comment", Date()),
            Comment(User(), "comment comment comment this is comment", Date()),
            Comment(User(), "comment comment comment this is comment", Date()),
            Comment(User(), "comment comment comment this is comment", Date()),
            Comment(User(), "comment comment comment this is comment", Date()),
            Comment(User(), "comment comment comment this is comment", Date()),
            Comment(User(), "comment comment comment this is comment", Date()),
            Comment(User(), "comment comment comment this is comment", Date())
        ))
    }

    fun loadComments() {
        FirebaseFirestore.getInstance()
            .collection("posts")
            .document(post.id)
            .collection("comments")
            .orderBy("time_stamp", Query.Direction.ASCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener {
                comments.postValue(it.toObjects(Comment::class.java) as ArrayList<Comment>)
            }
            .addOnFailureListener {
                this.logException(it)
            }
    }

    /**
     * Paging
     */
    val comments2 = LivePagedListBuilder(
        CommentsPageDataSource.CommentsSourceFactory(),
        CommentsPageDataSource.config
    ).build()

    class PostDetailViewModelFactory(val post: Post): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(Post::class.java).newInstance(post)
        }
    }
}

