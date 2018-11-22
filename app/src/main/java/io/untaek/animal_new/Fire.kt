package io.untaek.animal_new

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.lang.Exception

class Fire {
    companion object {
        const val POSTS = "posts"
        const val USERS = "users"
        const val COMMENTS = "comments"
    }

    interface Callback {
        fun onResult()
        fun onFail()
    }

    fun loadDocs(limit: Int, success: (QuerySnapshot) -> Any, fail: (Exception) -> Any) {
        FirebaseFirestore.getInstance()
            .collection(POSTS)
            .limit(limit.toLong())
            .get()
            .addOnSuccessListener {
                success(it)
            }
            .addOnFailureListener {
                fail(it)
            }
    }

    class Auth {

    }
}