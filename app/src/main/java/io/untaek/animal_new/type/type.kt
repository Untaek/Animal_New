package io.untaek.animal_new.type

import androidx.databinding.Bindable
import java.util.*

data class Post(
    val id: String,
    val user: User  = User(),
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

data class User(
    val userId: String = "dbsdlswp",
    val userName : String = "inje",
    val userImage : String = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQA9SOXW-VUdPL1Ot0DoYxgxqZIKjMqnTbgKH25VGpqBQ8LR19XliyGGhbv"
)
