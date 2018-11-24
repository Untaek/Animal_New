package io.untaek.animal_new

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import io.untaek.animal_new.type.Post
import io.untaek.animal_new.type.UserDetail
import java.lang.Exception

object Fire {
    const val POSTS = "posts"
    const val USERS = "users"
    const val COMMENTS = "comments"

    const val TIME_STAMP = "time_stamp"
    const val TOTAL_LIKES = "total_likes"
    const val TOTAL_FOLLOWERS = "total_followers"
    const val LIKES = "likes"
    const val CONTENT = "content"
    const val MIME = "mime"
    const val PICTURE_URL = "picture_url"
    const val DESCRIPTION = "description"
    const val TEXT = "text"
    const val NAME = "name"

    interface Callback {
        fun onResult()
        fun onFail()
    }

    fun loadPosts(limit: Int, lastSeen: DocumentSnapshot?, success: (List<Post>, DocumentSnapshot?) -> Any, fail: (Exception) -> Any) {
        val task = FirebaseFirestore.getInstance()
            .collection(POSTS)
            .limit(limit.toLong())
            //.orderBy("time_stamp", Query.Direction.ASCENDING)

        if(lastSeen != null)
            task.startAfter(lastSeen)

        task.get()
            .addOnSuccessListener { qs ->
                val posts = qs.documents.map { it.toObject(Post::class.java) ?: throw Exception() }
                success(posts, qs.documents.lastOrNull())
            }
            .addOnFailureListener {
                fail(it)
            }
    }

    /**
     * working on
     */
    fun uploadContent() {
        val ref = FirebaseStorage.getInstance().getReference("image.jpg")
        ref.putBytes(ByteArray(1))
            .continueWith(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                return@Continuation ref.downloadUrl
            })
            .addOnSuccessListener {
                val downloadUri = it.result
            }
            .addOnFailureListener {

            }
    }

    fun getUserDetailById(id: String) {
        FirebaseFirestore.getInstance()
            .collection(USERS)
            .document(id)
            .get()
            .addOnSuccessListener {
                val userDetail = it.toObject(UserDetail::class.java)
            }
            .addOnFailureListener {

            }
    }

    /**
     * TODO
     */
    fun toggleLike() {}

    /**
     * TODO
     */
    fun toggleFollow() {}

    class Auth {

    }
}