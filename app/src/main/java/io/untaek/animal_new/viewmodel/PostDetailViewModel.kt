package io.untaek.animal_new.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.untaek.animal_new.type.Post

class PostDetailViewModel(val post: Post): ViewModel() {
    companion object {
        const val TAG = "PostDetailViewModel"
    }
    init {
        Log.d(TAG, "Created ${this.hashCode()}")
    }
}

class PostDetailViewModelFactory(val post: Post): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Post::class.java).newInstance(post)
    }
}