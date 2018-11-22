package io.untaek.animal_new.type

import androidx.databinding.Bindable
import java.util.*

data class Post(
    val id: String,
    val user_id: String,
    val description: String,
    val time_stamp: Date,
    val content: Content
)

data class Content(
    val mime: String = "",
    val fileName: String = "",
    val url: String = "",
    val width: Int = 0,
    val height: Int = 0
)
