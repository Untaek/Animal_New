package io.untaek.animal_new.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.untaek.animal_new.list.comment.CommentsPageDataSource
import io.untaek.animal_new.list.timeline.TimelinePageDataSource
import io.untaek.animal_new.type.Comment
import io.untaek.animal_new.type.Post
import io.untaek.animal_new.type.User
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

