package io.untaek.animal_new.type

import androidx.databinding.Bindable
import java.util.*
import kotlin.collections.HashMap

data class Post(
    val id: String = "",
    val user: User = User(),
    val description: String = "",
    val time_stamp: Date = Date(),
    val content: Content = Content(),
    val total_likes: Int = 0,
    val total_comments: Int = 0
)

data class Content(
    val mime: String = "",
    val file_name: String = "",
    val url: String = "https://firebasestorage.googleapis.com/v0/b/animal-f6c09/o/dg2.jpg?alt=media&token=ad76a8af-6f74-4eaa-bf0a-7b946f4551a2",
    val width: Int = 0,
    val height: Int = 0
)

data class User(
    val id: String = "dbsdlswp",
    val name : String = "inje",
    val picture_url : String = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQA9SOXW-VUdPL1Ot0DoYxgxqZIKjMqnTbgKH25VGpqBQ8LR19XliyGGhbv"
)

data class UserDetail(
    val user: User = User(),
    val total_likes: Long = 0,
    val total_followers: Int = 0,
    val follows: HashMap<String, String> = hashMapOf()
)