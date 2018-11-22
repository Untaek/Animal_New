package io.untaek.animal_new.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import io.untaek.animal_new.type.Post
import java.util.*

class TimelineViewModel: ViewModel() {

    init {
        Log.d("TimelineViewModel", "Created ${this.hashCode()}")
    }
    val list = ObservableArrayList<Post>().apply {
        add(Post("haha", "1sd23", "hello", Date()))
        add(Post("haha", "1sd23", "hello", Date()))
        add(Post("haha", "1sd23", "hello", Date()))
        add(Post("haha", "1sd23", "hello", Date()))
    }
}