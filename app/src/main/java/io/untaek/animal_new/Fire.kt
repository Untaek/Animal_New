package io.untaek.animal_new

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import io.untaek.animal_new.type.*
import io.untaek.animal_new.util.ContentUtil
import io.untaek.animal_new.viewmodel.UploadViewModel
import java.lang.Exception
import java.util.*

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

    /**
     * working on
     */
    fun uploadContent(vm: UploadViewModel) {
        val uri = vm.currentUri!!
        val size = vm.currentSize!!
        val mime = vm.currentMime!!

        val uploading = Uploading(uri = uri)
        vm.addUploadState(uploading)

        val fileName = "USER_ID@${Date().time}.jpg"

        val ref = FirebaseStorage.getInstance("gs://animal-f6c09").getReference(fileName)
        val userRef = FirebaseFirestore.getInstance().collection(POSTS).document()
        ref.putFile(uri)
            .addOnProgressListener {
                val progress = it.bytesTransferred.toFloat() / it.totalByteCount.toFloat()
                uploading.size = it.totalByteCount
                uploading.progress = (progress * 100).toInt()
                vm.updateUploadState(uploading)
                Log.d("FireFire", "upload progress $progress")
            }
            .continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if(!task.isSuccessful){
                    throw Exception()
                }
                return@Continuation ref.downloadUrl
            })
            .addOnSuccessListener {
                val downloadUri = it

                val content = Content(mime, fileName, downloadUri.toString(), size.x, size.y)

                userRef
                    .set(Post(content = content))
                    .addOnSuccessListener {
                    Log.d("FireFire", "upload finished $downloadUri")
                }
            }
            .addOnFailureListener {
                //vm.removeUploadState(uploading)
            }

    }

    fun getUserDetailById(id: String) : UserDetail? {
        var userDetail :UserDetail = UserDetail()
        FirebaseFirestore.getInstance()
            .collection(USERS)
            .document(id)
            .get()
            .addOnSuccessListener {
                userDetail = it.toObject(UserDetail::class.java) as UserDetail
            }
            .addOnFailureListener {
            }
        return userDetail
    }

    /**
     * TODO
     */
    fun toggleLike() {}

    /**
     * TODO
     */
    fun toggleFollow() {}


    /**
     * Toggling like, follow
     *
     * fun like(postId: String)
     * fun dislike(postId: String)
     *
     * fun follow(postId: String)
     * fun unFollow(postId: String)
     */
    fun toggleLike(state: Boolean, postId: String, ownerId: String, callback: (Boolean, Long) -> Any) {
        when(state) {
            false -> like(postId, ownerId, callback)
            true -> dislike(postId, ownerId, callback)
        }
    }


    private fun like(postId: String, ownerId: String, callback: (Boolean, Long) -> Any) {
        val fs = FirebaseFirestore.getInstance()

        val postRef = fs.collection(POSTS)
            .document(postId)

        val ownerRef = fs.collection(USERS).document(ownerId)

        val userLikesRef = fs.collection(USERS)
            .document(FirebaseAuth.getInstance().uid!!)
            .collection(LIKES)
            .document(postId)

        FirebaseAuth.getInstance().currentUser?.let { _ ->
            fs.runTransaction { t ->
                val newPostTotalLikesValue = t.get(postRef).getLong(TOTAL_LIKES)?.plus(1L)
                val newUserTotalLikesValue = t.get(ownerRef).getLong(TOTAL_LIKES)?.plus(1L)

                t.update(postRef, TOTAL_LIKES, newPostTotalLikesValue)
                t.update(ownerRef, TOTAL_LIKES, newUserTotalLikesValue)
                t.set(userLikesRef, HashMap())

                newPostTotalLikesValue
            }.addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true, it.result!!)
                }
                else {
                    callback(false, -1)
                    Log.d("Fire", "like exception", it.exception)
                }
            }
        }
    }

    private fun dislike(postId: String, ownerId: String, callback: (Boolean, Long) -> Any) {
        val fs = FirebaseFirestore.getInstance()

        val postRef = fs.collection(POSTS)
            .document(postId)

        val ownerRef = fs.collection(USERS).document(ownerId)

        val userLikesRef = fs.collection(USERS)
            .document(FirebaseAuth.getInstance().uid!!)
            .collection(LIKES)
            .document(postId)

        FirebaseAuth.getInstance().currentUser?.let { _ ->
            fs.runTransaction { t ->
                val newPostTotalLikesValue = t.get(postRef).getLong(TOTAL_LIKES)?.minus(1L)
                val newUserTotalLikesValue = t.get(ownerRef).getLong(TOTAL_LIKES)?.minus(1L)

                t.update(postRef, TOTAL_LIKES, newPostTotalLikesValue)
                t.update(ownerRef, TOTAL_LIKES, newUserTotalLikesValue)
                t.delete(userLikesRef)

                newPostTotalLikesValue
            }.addOnCompleteListener {
                if(it.isSuccessful){
                    callback(false, it.result!!)
                }
                else {
                    callback(true, -1)
                    Log.d("Fire", "like exception", it.exception)
                }
            }
        }
    }

    private fun checkLike(postId: String, callback: (Boolean) -> Any) {
        FirebaseFirestore.getInstance()
            .collection(USERS)
            .document(FirebaseAuth.getInstance().uid!!)
            .collection(LIKES)
            .document(postId)
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    callback(it.result?.exists()!!)
                }
                else {
                    callback(false)
                }
            }
    }

//
//    fun checkFollow(myId:String, userId:String, callback: Callback<Boolean>){
//        fs().collection(USERS).document(myId).get().addOnSuccessListener {
//            val follow : MutableMap<String,Boolean> = it[FOLLOW] as MutableMap<String,Boolean>
//            val followFlag = follow[userId]
//            if(followFlag== null){
//                callback.onResult(false)
//            }else{
//                callback.onResult(true)
//            }
//            Log.e("ㅋㅋㅋ","flag = "+ followFlag)
//        }
//    }
//
//    fun follow(myId: String, userId: String) {          // myId 가 userId 를 follow
//        val fs = fs()
//        val myReference = fs.collection(USERS).document(myId)
//        val userReference = fs.collection(USERS).document(userId)
//        var follow: MutableMap<String, Boolean> = mutableMapOf()
//
//
//        FirebaseAuth.getInstance().let {
//            fs.runTransaction { t ->
//                fs.collection(USERS).document(myId).get().addOnSuccessListener {
//                    follow = it[FOLLOW] as MutableMap<String, Boolean>
//                    Log.e("ㅋㅋㅋ", "firebase = follow "+follow["aaaa"])
//                    follow.put(userId, true)            // myId 의 follows (map) 에 {userId, true} 추가.
//                }
//
//                val targetPost = t.get(userReference)
//                val newTotalFollowersCount = targetPost.getLong(TOTAL_FOLLOWERS)?.plus(1L)     // userId의 totalFollwers +1
//                t.update(userReference, TOTAL_FOLLOWERS, newTotalFollowersCount)
//                t.update(myReference, FOLLOW, follow)
//            }
//        }
//    }
//
//    fun unfollow(myId: String, userId: String) {
//        val fs = fs()
//        val myReference = fs.collection(USERS).document(myId)
//        val userReference = fs.collection(USERS).document(userId)
//        var follow: MutableMap<String, Boolean> = mutableMapOf()
//        //var followers: MutableMap<String, Boolean> = mutableMapOf()
//
//
//        FirebaseAuth.getInstance().let {
//            fs.runTransaction { t ->
//                fs.collection(USERS).document(myId).get().addOnSuccessListener {
//                    follow = it[FOLLOW] as MutableMap<String, Boolean>
//                    follow.remove(userId)          // myId 의 follows (map) 에 {userId, true} 추가.
//                }
//
//                fs.collection(USERS).document(userId).get().addOnSuccessListener {
//                    //followers = it[FOLLOWERS] as MutableMap<String, Boolean>
//                    //followers.put(myId, true)
//                }
//
//                val targetPost = t.get(userReference)
//                val newTotalFollowersCount = targetPost.getLong(TOTAL_FOLLOWERS)?.minus(1L)     // userId의 totalFollwers +1
//                t.update(userReference, TOTAL_FOLLOWERS, newTotalFollowersCount)
//                t.update(myReference, FOLLOW, follow)
//                //t.update(userReference, FOLLOWERS, followers)
//            }
//        }
//    }


    class Auth {

    }
}