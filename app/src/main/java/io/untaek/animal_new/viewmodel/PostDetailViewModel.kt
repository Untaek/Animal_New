package io.untaek.animal_new.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.DocumentSnapshot
import io.untaek.animal_new.Reactive
import io.untaek.animal_new.type.Comment
import io.untaek.animal_new.type.Post

class PostDetailViewModel(val post: Post): BaseViewModel() {
    val commentText = ObservableField<String>()
    val comments = ObservableArrayList<Comment>()
    val loading = ObservableBoolean(false)
    var lastSeen: DocumentSnapshot? = null

    init {
        loading.set(true)
        val _sub = Reactive.loadFirstComments(post, 15)
            .subscribe {
                lastSeen = it.first
                comments.addAll(it.second)
                loading.set(false)
            }
    }

    @SuppressLint("CheckResult")
    fun sendNewComment() {
        val text = commentText.get() ?: ""
        if(text == "") {
            return
        }
        loading.set(true)
        Reactive.sendNewComment(post, text)
            .subscribe {
                Log.d(TAG, it.toString())
                comments.add(0, it)
                commentText.set("")
                loading.set(false)
            }
    }

    @SuppressLint("CheckResult")
    fun loadMoreComments(limit: Int) {
        loading.set(true)
        if (lastSeen != null){
            Reactive.loadCommentsPage(post, limit, lastSeen!!)
                .subscribe {
                    lastSeen = it.first
                    comments.addAll(it.second)
                    loading.set(false)
                }
        }
    }

//    /**
//     * Paging
//     */
//    val comments2 = LivePagedListBuilder(
//        CommentsPageDataSource.CommentsSourceFactory(),
//        CommentsPageDataSource.config
//    ).build()

    class PostDetailViewModelFactory(val post: Post): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(Post::class.java).newInstance(post)
        }
    }
}

