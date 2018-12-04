package io.untaek.animal_new

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.observables.ConnectableObservable
import io.untaek.animal_new.type.*
import io.untaek.animal_new.viewmodel.UploadViewModel
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object Reactive {
    private const val STORAGE_BUCKET_ASIA = "gs://animal-f6c09"

    const val POSTS = "posts"
    const val USERS = "users"
    const val COMMENTS = "comments"

    const val TIME_STAMP = "time_stamp"
    const val TOTAL_LIKES = "total_likes"
    const val TOTAL_FOLLOWERS = "total_followers"
    const val TOTAL_COMMENTS = "total_comments"
    const val TOTAL_POSTS = "total_posts"
    const val LIKES = "likes"
    const val CONTENT = "content"
    const val MIME = "mime"
    const val PICTURE_URL = "picture_url"
    const val DESCRIPTION = "description"
    const val TEXT = "text"
    const val NAME = "name"

    enum class State{
        Start,
        Pending,
        Finish
    }

    data class LastSeen(val documentSnapshot: DocumentSnapshot?)
    data class LikeState(val post: Post, val loading: Boolean)
    data class UploadState(val id: Int, val state: State, val progress: Int, val url: String?, val content: Content?, val post: Post?)

    private fun currentUser(): User {
        val user = FirebaseAuth.getInstance().currentUser

        return user?.let {
            User(it.uid, it.displayName!!, it.photoUrl.toString())
        } ?: User()
    }

    private fun fileUploadObservable(vm: UploadViewModel): Observable<UploadState> =
        Observable.create { sub ->
            val user = FirebaseAuth.getInstance().currentUser!!

            val uri = vm.currentUri!!
            val size = vm.currentSize!!
            val mime = vm.currentMime!!
            val fileName = "${user.uid}@${Date().time}.jpg"

            val stateId = (System.currentTimeMillis() % 100_000_000).toInt()
            sub.onNext(UploadState(stateId, State.Start,0, uri.toString(), null, null))

            val ref = FirebaseStorage
                .getInstance(STORAGE_BUCKET_ASIA)
                .getReference(fileName)

            ref.putFile(uri)
                .addOnProgressListener {
                    val progress = ((it.bytesTransferred.toFloat() / it.totalByteCount.toFloat()) * 100).toInt()
                    sub.onNext(UploadState(stateId, State.Pending, progress, uri.toString(), null, null))
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

                    sub.onNext(UploadState(stateId, State.Pending, 100, uri.toString(), content, null))
                    sub.onComplete()
                }
                .addOnFailureListener {
                    sub.onError(it)
                }
        }

    private fun writePostObservable(uploadState: UploadState, description: String, tags: Map<String, String>): Observable<UploadState> =
        Observable.create { sub ->
            val post = Post("", currentUser(), description, uploadState.content!!, tags)

            val userRef = FirebaseFirestore
                .getInstance()
                .collection(USERS)
                .document(currentUser().id)

            val postRef = FirebaseFirestore
                .getInstance()
                .collection(POSTS)
                .document()

            FirebaseFirestore
                .getInstance()
                .runTransaction { t ->
                    val newPostCount = t.get(userRef).getLong(TOTAL_POSTS)!!.plus(1)

                    t.update(userRef, TOTAL_POSTS, newPostCount)
                    t.set(postRef, post)
                }
                .addOnSuccessListener {
                    sub.onNext(uploadState.copy(state = State.Finish, post = post.copy(id = postRef.id)))
                    sub.onComplete()
                }
                .addOnFailureListener {
                    sub.onError(it)
                }
        }

    private fun loadSinglePostObservable(postId: String): Observable<Post> =
        Observable.create { sub ->
            FirebaseFirestore.getInstance()
                .collection(POSTS)
                .document(postId)
                .get()
                .addOnSuccessListener { doc ->
                    val post = doc.toObject(Post::class.java)!!.apply { id = doc.id }
                    sub.onNext(post)
                    sub.onComplete()
                }
                .addOnFailureListener {
                    sub.onError(it)
                }
        }

    private fun loadFirstTimelineObservable(limit: Int) =
        Observable.create(ObservableOnSubscribe<Pair<LastSeen, List<Post>>> { sub ->
            FirebaseFirestore.getInstance()
                .collection(POSTS)
                .orderBy(TIME_STAMP, Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .addOnSuccessListener {
                    Log.d("Reactive", "call loadFirstTimelineObservable")
                    val posts = it.documents.map { doc ->
                        doc.toObject(Post::class.java)!!.apply { id = doc.id }
                    }
                    sub.onNext(Pair(LastSeen(it.documents.lastOrNull()), posts))
                    sub.onComplete()
                }
    })

    private fun loadTimelinePageObservable(limit: Int, lastSeen: DocumentSnapshot) =
        Observable.create(ObservableOnSubscribe<Pair<LastSeen, List<Post>>> { sub ->
            FirebaseFirestore.getInstance()
                .collection(POSTS)
                .orderBy(TIME_STAMP, Query.Direction.DESCENDING)
                .startAfter(lastSeen)
                .limit(limit.toLong())
                .get()
                .addOnSuccessListener {
                    val posts = it.documents.map { doc ->
                        doc.toObject(Post::class.java)!!.apply { id = doc.id }
                    }
                    sub.onNext(Pair(LastSeen(it.documents.lastOrNull()), posts))
                    sub.onComplete()
                }
    })

    private fun getLikeObservable(post: Post): Observable<Post> {
        return Observable.create { sub ->
            FirebaseFirestore.getInstance()
                .collection(USERS)
                .document(currentUser().id)
                .collection(LIKES)
                .document(post.id)
                .get()
                .addOnSuccessListener {
                    sub.onNext(post.copy(like = it.exists()))
                    sub.onComplete()
                }
        }
    }

    private fun getPopularCommentsObservable(post: Post): Observable<Post> {
        return Observable.create { sub ->
            FirebaseFirestore.getInstance()
                .collection(POSTS)
                .document(post.id)
                .collection(COMMENTS)
                .orderBy(TIME_STAMP, Query.Direction.DESCENDING)
                .limit(2)
                .get()
                .addOnSuccessListener {
                    val comments = it.map {
                            comment -> comment.toObject(Comment::class.java).apply { id = comment.id }
                    } as ArrayList<Comment>

                    sub.onNext(post.copy(comments = comments))
                    sub.onComplete()
                }
                .addOnFailureListener {
                    sub.onError(it)
                }
        }
    }

    private fun loadFirstCommentsObservable(post: Post, limit: Int): Observable<Pair<LastSeen, ArrayList<Comment>>> {
        return Observable.create { sub ->
            FirebaseFirestore.getInstance()
                .collection(POSTS)
                .document(post.id)
                .collection(COMMENTS)
                .orderBy(TIME_STAMP, Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .addOnSuccessListener {
                    val comments = it.map {
                            comment -> comment.toObject(Comment::class.java).apply { id = comment.id }
                    } as ArrayList<Comment>

                    sub.onNext(Pair(LastSeen(it.lastOrNull()), comments))
                    sub.onComplete()
                }
                .addOnFailureListener {
                    sub.onError(it)
                }
        }
    }

    private fun loadCommentsPageObservable(post: Post, limit: Int, lastSeen: DocumentSnapshot): Observable<Pair<LastSeen, ArrayList<Comment>>> {
        return Observable.create { sub ->
            FirebaseFirestore.getInstance()
                .collection(POSTS)
                .document(post.id)
                .collection(COMMENTS)
                .orderBy(TIME_STAMP, Query.Direction.DESCENDING)
                .startAfter(lastSeen)
                .limit(limit.toLong())
                .get()
                .addOnSuccessListener {
                    val comments = it.map {
                            comment -> comment.toObject(Comment::class.java).apply { id = comment.id }
                    } as ArrayList<Comment>

                    sub.onNext(Pair(LastSeen(it.lastOrNull()), comments))
                    sub.onComplete()
                }
                .addOnFailureListener {
                    sub.onError(it)
                }
        }
    }

    private fun likeObservable(post: Post): Observable<LikeState> {
        return Observable.create { sub ->
            /**
             * Fake state
             */
            val localLike = !post.like

            val likeOrDislike = if (localLike) 1 else -1

            val localTotalLikes = post.total_likes.plus(likeOrDislike)
            val localState = LikeState(post.copy(like = localLike, total_likes = localTotalLikes), true)

            sub.onNext(localState)

            val fs = FirebaseFirestore.getInstance()
            val postRef = fs
                .collection(POSTS)
                .document(post.id)

            val ownerRef = fs.collection(USERS).document(post.user.id)

            val userLikesRef = fs.collection(USERS)
                .document(currentUser().id)
                .collection(Fire.LIKES)
                .document(post.id)

            FirebaseAuth.getInstance().currentUser?.let { _ ->
                fs.runTransaction { t ->
                    val newPostTotalLikesValue = t.get(postRef).getLong(TOTAL_LIKES)?.plus(likeOrDislike)
                    val newUserTotalLikesValue = t.get(ownerRef).getLong(TOTAL_LIKES)?.plus(likeOrDislike)

                    if(localLike) {
                        t.set(userLikesRef, HashMap())
                    }
                    else {
                        t.delete(userLikesRef)
                    }
                    t.update(postRef, TOTAL_LIKES, newPostTotalLikesValue)
                    t.update(ownerRef, TOTAL_LIKES, newUserTotalLikesValue)


                    newPostTotalLikesValue
                }.addOnCompleteListener {
                    if(it.isSuccessful){
                        /**
                         * Real State
                         */
                        val newState = LikeState(post.copy(like = localLike, total_likes = it.result!!.toInt()), false)
                        sub.onNext(newState)
                        sub.onComplete()
                    }
                    else {
                        sub.onError(Exception())
                        Log.d("Fire", "like exception", it.exception)
                    }
                }
            }
        }
    }

    private fun newCommentObservable(post: Post, text: String): Observable<Comment> {
        return Observable.create { sub ->
            val postRef = FirebaseFirestore
                .getInstance()
                .collection(POSTS)
                .document(post.id)

            val commentRef = postRef
                .collection(COMMENTS)
                .document()

            val comment = Comment(commentRef.id, currentUser(), text)

            FirebaseFirestore
                .getInstance()
                .runTransaction { t ->
                    val newCommentsAmount = t.get(postRef).getLong(TOTAL_COMMENTS)!!.plus(1)

                    t.update(postRef, TOTAL_COMMENTS, newCommentsAmount)
                    t.set(commentRef, comment)
                }
                .addOnSuccessListener {
                    sub.onNext(comment)
                    sub.onComplete()
                }
                .addOnFailureListener {
                    sub.onError(it)
                }
        }
    }

    /************************
     * Exposed functions
     ************************/

    fun uploadContent(vm: UploadViewModel, description: String, tags: Map<String, String>): Observable<UploadState> {
        val observer= fileUploadObservable(vm).publish()

        val progressObserver = observer
            .map { it }

        val resultObserver = progressObserver
            .lastElement()
            .toObservable()
            .flatMap { writePostObservable(it, description, tags) }

        val result = ConnectableObservable
            .merge(progressObserver, resultObserver)
            .subscribeOn(AndroidSchedulers.mainThread())

        observer.connect()

        return result
    }

    fun loadPost(postId: String): Observable<Post> {
        return loadSinglePostObservable(postId)
            .flatMap(this::getLikeObservable)
            .subscribeOn(AndroidSchedulers.mainThread())
    }

    fun loadFirstTimeline(limit: Int): Observable<Pair<DocumentSnapshot?, List<Post>>> {
        Log.d("Reactive", "call loadFirstTimeline")
        val observer = loadFirstTimelineObservable(limit).publish()
        val ob1 = observer
            .map { it.first }
        val ob2 = observer.flatMapIterable { it.second }
            .flatMap(this::getLikeObservable)
            .flatMap(this::getPopularCommentsObservable)
            .toList()
            .map { it.sortedByDescending { p -> p.time_stamp }}
            .toObservable()

        val result= ConnectableObservable
            .zip(ob1, ob2, BiFunction<LastSeen, List<Post>, Pair<DocumentSnapshot?, List<Post>>>{ t1, t2 ->
                Pair(t1.documentSnapshot, t2)
            })
            .subscribeOn(AndroidSchedulers.mainThread())

        observer.connect()
        return result
    }

    fun loadTimelinePage(limit: Int, lastSeen: DocumentSnapshot): Observable<Pair<DocumentSnapshot?, List<Post>>> {
        Log.d("Reactive", "call loadTimelinePage")
        val observer = loadTimelinePageObservable(limit, lastSeen).publish()
        val ob1 = observer.map { it.first }
        val ob2 = observer.flatMapIterable { it.second }
            .flatMap(this::getLikeObservable)
            .toList()
            .map { it.sortedByDescending { p -> p.time_stamp }}
            .toObservable()

        val result= ConnectableObservable
            .zip(ob1, ob2, BiFunction<LastSeen, List<Post>, Pair<DocumentSnapshot?, List<Post>>>{ t1, t2 ->
                Pair(t1.documentSnapshot, t2)
            })

        observer.connect()
        return result
    }

    fun loadFirstComments(post: Post, limit: Int): Observable<Pair<DocumentSnapshot?, ArrayList<Comment>>> {
        return loadFirstCommentsObservable(post, limit)
            .map {
                Pair(it.first.documentSnapshot, it.second)
            }
    }

    fun loadCommentsPage(post: Post, limit: Int, lastSeen: DocumentSnapshot): Observable<Pair<DocumentSnapshot?, ArrayList<Comment>>> {
        return loadCommentsPageObservable(post, limit, lastSeen)
            .map {
                Pair(it.first.documentSnapshot, it.second)
            }
    }

    fun sendNewComment(post: Post, text: String): Observable<Comment> {
        return newCommentObservable(post, text)
    }

    fun like(post: Post): Observable<LikeState> {
        return likeObservable(post)
    }
}