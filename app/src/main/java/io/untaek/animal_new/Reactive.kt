package io.untaek.animal_new

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.observables.ConnectableObservable
import io.untaek.animal_new.type.Post
import org.w3c.dom.Document
import java.lang.Exception
import java.util.*

object Reactive {
    data class LastSeen(val documentSnapshot: DocumentSnapshot?)

    private fun loadFirstTimelineObservable(limit: Int) =
        Observable.create(ObservableOnSubscribe<Pair<LastSeen, List<Post>>> { sub ->
            FirebaseFirestore.getInstance()
                .collection("posts")
                .orderBy("time_stamp", Query.Direction.DESCENDING)
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
                .collection("posts")
                .orderBy("time_stamp", Query.Direction.DESCENDING)
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

    private fun getLike(post: Post): Observable<Post> {
        return Observable.create { sub ->
            FirebaseFirestore.getInstance()
                .collection("users")
                .document("dbsdlswp")
                .collection("likes")
                .document(post.id)
                .get()
                .addOnSuccessListener {
                    Log.d("Reactive", "call getLike ${it.exists()}")
                    post.like = it.exists()
                    sub.onNext(post)
                    sub.onComplete()
                }
        }
    }

    @SuppressLint("CheckResult")
    fun loadFirstTimeline(limit: Int): Observable<Pair<DocumentSnapshot?, List<Post>>> {
        Log.d("Reactive", "call loadFirstTimeline")
        val observer = loadFirstTimelineObservable(limit).publish()
        val ob1 = observer.map { it.first }
        val ob2 = observer.flatMapIterable { it.second }
            .flatMap(this::getLike)
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

    @SuppressLint("CheckResult")
    fun loadTimelinePage(limit: Int, lastSeen: DocumentSnapshot): Observable<Pair<DocumentSnapshot?, List<Post>>> {
        Log.d("Reactive", "call loadTimelinePage")
        val observer = loadTimelinePageObservable(limit, lastSeen).publish()
        val ob1 = observer.map { it.first }
        val ob2 = observer.flatMapIterable { it.second }
            .flatMap(this::getLike)
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

    fun like(post: Post): Observable<Post> {
        return Observable.create { sub ->
            val fs = FirebaseFirestore.getInstance()
            val postRef = fs
                .collection(Fire.POSTS)
                .document(post.id)

            val ownerRef = fs.collection(Fire.USERS).document(post.user.id)

            val userLikesRef = fs.collection(Fire.USERS)
                .document(FirebaseAuth.getInstance().uid!!)
                .collection(Fire.LIKES)
                .document(post.id)

            FirebaseAuth.getInstance().currentUser?.let { _ ->
                fs.runTransaction { t ->
                    val newPostTotalLikesValue = t.get(postRef).getLong(Fire.TOTAL_LIKES)?.plus(1L)
                    val newUserTotalLikesValue = t.get(ownerRef).getLong(Fire.TOTAL_LIKES)?.plus(1L)

                    t.update(postRef, Fire.TOTAL_LIKES, newPostTotalLikesValue)
                    t.update(ownerRef, Fire.TOTAL_LIKES, newUserTotalLikesValue)
                    t.set(userLikesRef, HashMap())

                    newPostTotalLikesValue
                }.addOnCompleteListener {
                    if(it.isSuccessful){
                        sub.onNext(post)
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

}