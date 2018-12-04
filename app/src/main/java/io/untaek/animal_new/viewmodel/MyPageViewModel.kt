package io.untaek.animal_new.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.DocumentSnapshot
import io.untaek.animal_new.Reactive
import io.untaek.animal_new.type.Post
import io.untaek.animal_new.type.User
import io.untaek.animal_new.type.UserDetail

class MyPageViewModel(val user : User = User()) : BaseViewModel(){

    val postList = ObservableArrayList<Post>()
    var loading = false
    var lastSeen: DocumentSnapshot? = null

    var userDetail: ObservableField<UserDetail> = ObservableField(UserDetail())
    init {
        Log.d("ㅋㅋㅋ", user.toString())

        reactiveGetUserDetail(user.id)
        loading = true
        Reactive.loadFirstMyPage(15, user.id).subscribe {
            loading = false
            lastSeen = it.first
            postList.addAll(it.second)
        }
    }

    fun refreshMyPage() {
        Log.d("ㅋㅋㅋ","ViewModel : refreshMyPage")
        refreshState.postValue(true)
        postList.clear()
        Reactive.loadFirstMyPage(15, user.id).subscribe {
            loading = false
            lastSeen = it.first
            postList.addAll(it.second)
            refreshState.postValue(false)
        }

        reactiveGetUserDetail(user.id)


    }
    fun loadMore() {
        if(lastSeen != null) {
            refreshState.postValue(true)
            Reactive.loadMyPage(9, lastSeen!!, user.id).subscribe {
                lastSeen = it.first
                postList.addAll(it.second)
                refreshState.postValue(false)
            }
        }
        reactiveGetUserDetail(user.id)
    }

    fun reactiveGetUserDetail(userId : String){
        Reactive.getUserDetail(user.id)
            .subscribe {
                userDetail.set(it)
                userDetail.get()?.user = user
                userDetail.notifyChange()
            }
    }

    /**
     * Paging Library
     */

    class MyPageViewModelFactory(val user: User): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(User::class.java).newInstance(user)
        }
    }
//    private val dataSourceFactory = MyPageDataSource.MyPageSourceFactory()
//
//    val pagedMyPage = LivePagedListBuilder(dataSourceFactory, MyPageDataSource.config)
//        .build()

    val refreshState = MutableLiveData<Boolean>().apply { value = false }

//    fun refresh() {
//        refreshState.postValue(true)
//        pagedMyPage.value?.dataSource?.invalidate()
//    }

//    val pagedMyPage = LivePagedListBuilder<DocumentSnapshot, Post>(
//        MyPageDataSource.MyPageSourceFactory()
//        , MyPageDataSource.config
//    ).build()

}