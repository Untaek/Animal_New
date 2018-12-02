package io.untaek.animal_new.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.untaek.animal_new.Reactive
import io.untaek.animal_new.list.timeline.TimelinePageDataSource
import io.untaek.animal_new.type.Dummy
import io.untaek.animal_new.type.Post

class TimelineViewModel: BaseViewModel() {

    val timeline = ObservableArrayList<Post>()
    var loading = false
    var lastSeen: DocumentSnapshot? = null

    init {
        loading = true
        val disposable = Reactive.loadFirstTimeline(5).subscribe {
            loading = false
            lastSeen = it.first
            timeline.addAll(it.second)
        }
    }

    fun refreshTimeline() {
        refreshState.postValue(true)
        timeline.clear()
        Reactive.loadFirstTimeline(10).subscribe {
            loading = false
            lastSeen = it.first

            timeline.addAll(it.second)
            refreshState.postValue(false)
        }
    }

    fun loadMore() {
        if(lastSeen != null) {
            refreshState.postValue(true)
            Reactive.loadTimelinePage(7, lastSeen!!).subscribe {
                lastSeen = it.first
                timeline.addAll(it.second)
                refreshState.postValue(false)
            }
        }
    }

    /**
     * Paging Library
     */
    private val dataSourceFactory = TimelinePageDataSource.TimelineSourceFactory()

    val pagedTimeline = LivePagedListBuilder(dataSourceFactory, TimelinePageDataSource.config)
        .build()

    val refreshState = MutableLiveData<Boolean>().apply { value = false }

    fun refresh() {
        refreshState.postValue(true)
        pagedTimeline.value?.dataSource?.invalidate()
    }
}