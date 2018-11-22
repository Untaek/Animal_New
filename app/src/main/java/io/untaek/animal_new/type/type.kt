package io.untaek.animal_new.type

import androidx.databinding.Bindable
import java.util.*

data class Post(
    val id: String,
    val user_id: String,
    val description: String,
    val time_stamp: Date
)

data class Content(
    val mime: String,
    val fileName: String,
    val url: String,
    val width: Int,
    val height: Int
)
