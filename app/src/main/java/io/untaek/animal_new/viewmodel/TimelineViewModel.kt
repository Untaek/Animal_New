package io.untaek.animal_new.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import io.untaek.animal_new.Fire
import io.untaek.animal_new.type.Dummy
import io.untaek.animal_new.type.Post
import java.lang.Exception
import kotlin.collections.ArrayList

class TimelineViewModel: BaseViewModel() {

    val list = ObservableArrayList<Post>().apply {
        add(Dummy.post1)
        add(Dummy.post2)
        add(Dummy.post1)
        add(Dummy.post1)
        add(Dummy.post2)
        add(Dummy.post2)
        add(Dummy.post1)
        add(Dummy.post2)
        add(Dummy.post1)
        add(Dummy.post1)
    }

    val timeline = MutableLiveData<List<Post>>()
    private val lastSeen = MutableLiveData<DocumentSnapshot>()

    var selectedPost: Post? = null

    fun selectPost(post: Post) {
        selectedPost = post
        Log.d(TAG, selectedPost.toString())
    }

    fun likePost(post: Post) {

    }

    fun loadPosts(limit: Int, lastSeen: DocumentSnapshot?): LiveData<List<Post>> {
        Fire.loadPosts(limit, lastSeen, { posts, last ->
            this@TimelineViewModel.lastSeen.postValue(last)
            timeline.postValue(posts)
        }, {

        })

        return timeline
    }
}