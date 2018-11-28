package io.untaek.animal_new.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import com.google.firebase.firestore.DocumentSnapshot
import io.untaek.animal_new.list.timeline.TimelinePageDataSource
import io.untaek.animal_new.type.Dummy
import io.untaek.animal_new.type.Post

class TimelineViewModel: BaseViewModel() {

    val list = ObservableArrayList<Post>().apply {
        add(Dummy.post1)
        add(Dummy.post2)
        add(Dummy.post1)
        add(Dummy.post1)
        add(Dummy.post2)
        add(Dummy.post2)
        add(Dummy.post1)
        add(Dummy.post2)
        add(Dummy.post1)
        add(Dummy.post1)
    }

    /**
     * Paging
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