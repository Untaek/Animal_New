package io.untaek.animal_new.list.comment

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.untaek.animal_new.type.Comment
import io.untaek.animal_new.type.Post

class CommentsPageDataSource {
    companion object {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(15)
            .setPageSize(10)
            .setPrefetchDistance(10)
            .build()
    }

    class CommentsSourceFactory: DataSource.Factory<DocumentSnapshot, Comment>(){
        override fun create(): DataSource<DocumentSnapshot, Comment> {
            return CommentsSource()
        }
    }

    class CommentsSource: PageKeyedDataSource<DocumentSnapshot, Comment>(){

        override fun loadInitial(
            params: LoadInitialParams<DocumentSnapshot>,
            callback: LoadInitialCallback<DocumentSnapshot, Comment>
        ) {
            FirebaseFirestore.getInstance()
                .collection("posts")
                .orderBy("time_stamp", Query.Direction.DESCENDING)
                .limit(params.requestedLoadSize.toLong())
                .get()
                .addOnSuccessListener {
                    callback.onResult(it.toObjects(Comment::class.java), null, it.documents.lastOrNull())
                }
        }

        override fun loadAfter(
            params: LoadParams<DocumentSnapshot>,
            callback: LoadCallback<DocumentSnapshot, Comment>
        ) {
            FirebaseFirestore.getInstance()
                .collection("posts")
                .orderBy("time_stamp", Query.Direction.DESCENDING)
                .startAfter(params.key)
                .limit(params.requestedLoadSize.toLong())
                .get()
                .addOnSuccessListener {
                    callback.onResult(it.toObjects(Comment::class.java), it.documents.lastOrNull())
                }
        }

        override fun loadBefore(params: LoadParams<DocumentSnapshot>, callback: LoadCallback<DocumentSnapshot, Comment>) {}
    }
}