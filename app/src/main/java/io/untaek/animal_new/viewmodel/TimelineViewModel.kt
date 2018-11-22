package io.untaek.animal_new.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import io.untaek.animal_new.type.Content
import io.untaek.animal_new.type.Post
import java.util.*

class TimelineViewModel: ViewModel() {

    init {
        Log.d("TimelineViewModel", "Created ${this.hashCode()}")
    }
    val list = ObservableArrayList<Post>().apply {
        add(Post("haha", "1sd23", "hello", Date(), Content(url = "https://img.purch.com/w/660/aHR0cDovL3d3dy5saXZlc2NpZW5jZS5jb20vaW1hZ2VzL2kvMDAwLzA4OC85MTEvb3JpZ2luYWwvZ29sZGVuLXJldHJpZXZlci1wdXBweS5qcGVn")))
        add(Post("haha", "1sd23", "hello", Date(), Content(url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSqK9wO9Xt8vYKdLlI2ZnYUjbLLicNwAIEaZEt47joX09DwWL5r")))
        add(Post("haha", "1sd23", "hello", Date(), Content(url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSqK9wO9Xt8vYKdLlI2ZnYUjbLLicNwAIEaZEt47joX09DwWL5r")))
        add(Post("haha", "1sd23", "hello", Date(), Content(url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSqK9wO9Xt8vYKdLlI2ZnYUjbLLicNwAIEaZEt47joX09DwWL5r")))
    }
}