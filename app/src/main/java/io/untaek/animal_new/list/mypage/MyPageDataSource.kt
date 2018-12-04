package io.untaek.animal_new.list.mypage

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.untaek.animal_new.type.Post

class MyPageDataSource {
    companion object {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(7)
            .setPageSize(6)
            .setPrefetchDistance(6)
            .build()
    }

    class MyPageSourceFactory: DataSource.Factory<DocumentSnapshot, Post>(){
        override fun create(): DataSource<DocumentSnapshot, Post> {
            return MyPageSource()
        }
    }

    class MyPageSource: PageKeyedDataSource<DocumentSnapshot, Post>(){

        override fun loadInitial(
            params: LoadInitialParams<DocumentSnapshot>,
            callback: LoadInitialCallback<DocumentSnapshot, Post>
        ) {
            FirebaseFirestore.getInstance()
                .collection("posts")
                .whereEqualTo("userDetail.id", "dbsdlswp")
                .limit(params.requestedLoadSize.toLong())
                .get()
                .addOnSuccessListener {
                    callback.onResult(it.toObjects(Post::class.java), null, it.documents.lastOrNull())
                }
        }

        override fun loadAfter(
            params: LoadParams<DocumentSnapshot>,
            callback: LoadCallback<DocumentSnapshot, Post>
        ) {
            FirebaseFirestore.getInstance()
                .collection("posts")
                .whereEqualTo("userDetail.id", "dbsdlswp")
                .startAfter(params.key)
                .limit(params.requestedLoadSize.toLong())
                .get()
                .addOnSuccessListener {
                    callback.onResult(it.toObjects(Post::class.java), it.documents.lastOrNull())
                }
        }

        override fun loadBefore(params: LoadParams<DocumentSnapshot>, callback: LoadCallback<DocumentSnapshot, Post>) {}
    }
}