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

class PostDetailViewModel: BaseViewModel {
    constructor(post: Post) {
        this.post.set(post)
        loading.set(true)
        val _sub = Reactive.loadFirstComments(post, 15)
            .subscribe {
                lastSeen = it.first
                comments.addAll(it.second)
                loading.set(false)
            }
    }

    constructor(postId: String) {
        loading.set(true)
        val _sub1 = Reactive.loadPost(postId)
            .subscribe {
                this.post.set(it)
            }
        val _sub2 = Reactive
            .loadFirstComments(Post(postId), 15)
            .subscribe {
                lastSeen = it.first
                comments.addAll(it.second)
                loading.set(false)
            }
    }

    var post = ObservableField<Post>(Post())
    val commentText = ObservableField<String>()
    val comments = ObservableArrayList<Comment>()
    val loading = ObservableBoolean(false)
    private var lastSeen: DocumentSnapshot? = null

    @SuppressLint("CheckResult")
    fun sendNewComment() {
        val text = commentText.get() ?: ""
        if(text == "") {
            return
        }
        loading.set(true)
        Reactive.sendNewComment(post.get()!!, text)
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
            Reactive.loadCommentsPage(post.get()!!, limit, lastSeen!!)
                .subscribe {
                    lastSeen = it.first
                    comments.addAll(it.second)
                    loading.set(false)
                }
        }
    }

    @SuppressLint("CheckResult")
    fun loadPost(postId: String) {
        loading.set(true)
        Reactive.loadPost(postId)
            .subscribe {
                this.post.set(it)
            }
    }

    class PostDetailViewModelFactory: ViewModelProvider.Factory {
        var post: Post? = null
        var postId: String? = null

        constructor(post: Post) {
            this.post = post
        }

        constructor(postId: String) {
            this.postId = postId
        }

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if(post != null) {
                modelClass.getConstructor(Post::class.java).newInstance(post)
            } else {
                modelClass.getConstructor(String::class.java).newInstance(postId)
            }
        }
    }
}

