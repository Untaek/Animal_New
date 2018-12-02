package io.untaek.animal_new.type

import android.net.Uri
import androidx.databinding.Bindable
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class Post(
    var id: String = "",
    val user: User = User(),
    val description: String = "",
    val time_stamp: Date = Date(),
    val content: Content = Content(),
    val total_likes: Int = 0,
    val total_comments: Int = 0,
    var like: Boolean = false,

    var comments: ArrayList<Comment> = arrayListOf()
): Serializable

data class Content(
    val mime: String = "",
    val file_name: String = "",
    val url: String = "https://firebasestorage.googleapis.com/v0/b/animal-f6c09/o/dg2.jpg?alt=media&token=ad76a8af-6f74-4eaa-bf0a-7b946f4551a2",
    val width: Int = 0,
    val height: Int = 0
): Serializable

data class User(
    val id: String = "UNTUTNTUTUNT",
    val name : String = "Untaek Lim",
    val picture_url : String = "https://scontent-sea1-1.cdninstagram.com/vp/3f8a5fd40e783db96306d09e87f1f5fa/5CADED2C/t51.2885-15/e35/15802351_328244704242314_237747850245570560_n.jpg?se=8&ig_cache_key=MTQxNzQzOTA5NDM5MjUxNDA5Nw%3D%3D.2"
): Serializable

data class UserDetail(
    val user: User = User(),
    val total_likes: Long = 0,
    val total_followers: Int = 0,
    val follows: HashMap<String, String> = hashMapOf()
): Serializable

data class Comment(
    var id: String = "",
    val user: User = User(),
    val text: String = "",
    val time_stamp: Date = Date(),
    val total_likes: Long = 0
): Serializable

data class Uploading(
    var progress: Int = 0,
    val type: String = "image",
    var size: Long = 0,
    val uri: Uri
)