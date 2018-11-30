package io.untaek.animal_new.list.timeline

import android.util.Log
import androidx.paging.*
import com.google.firebase.firestore.DocumentSnapshot
import io.untaek.animal_new.Reactive
import io.untaek.animal_new.type.Post

class TimelinePageDataSource {
    companion object {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(5)
            .setPageSize(5)
            .setPrefetchDistance(5)
            .build()
    }

    class TimelineSourceFactory: DataSource.Factory<DocumentSnapshot, Post>(){
        override fun create(): DataSource<DocumentSnapshot, Post> {
            return TimelineSource()
        }
    }

    class TimelineSource: PageKeyedDataSource<DocumentSnapshot, Post>(){

        override fun loadInitial(
            params: LoadInitialParams<DocumentSnapshot>,
            callback: LoadInitialCallback<DocumentSnapshot, Post>
        ) {
            Log.d("TimelineSource", "loadInitial")

            Reactive.loadFirstTimeline(params.requestedLoadSize).also {
                it.subscribe { pair ->
                    Log.d("TimelineSource", pair.second.toString())
                    callback.onResult(pair.second, null, pair.first)
                }
            }
        }

        override fun loadAfter(
            params: LoadParams<DocumentSnapshot>,
            callback: LoadCallback<DocumentSnapshot, Post>
        ) {
            Log.d("TimelineSource", "loadAfter")
            Reactive.loadTimelinePage(params.requestedLoadSize, params.key).also {
                it.subscribe { pair ->
                    Log.d("TimelineSource", pair.second.toString())
                    callback.onResult(pair.second, pair.first)
                }
            }
        }

        override fun loadBefore(params: LoadParams<DocumentSnapshot>, callback: LoadCallback<DocumentSnapshot, Post>) {}
    }
}