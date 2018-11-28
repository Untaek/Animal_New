package io.untaek.animal_new.viewmodel

import androidx.databinding.ObservableArrayList
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
    val pagedTimeline = LivePagedListBuilder<DocumentSnapshot, Post>(
        TimelinePageDataSource.TimelineSourceFactory()
        , TimelinePageDataSource.config
    ).build()
}