package io.untaek.animal_new.viewmodel

import android.annotation.SuppressLint
import android.graphics.Point
import android.net.Uri
import android.util.Log
import android.util.SparseArray
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableField
import androidx.databinding.ObservableMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import io.untaek.animal_new.Fire
import io.untaek.animal_new.Reactive
import io.untaek.animal_new.type.Uploading

class UploadViewModel: BaseViewModel() {
    val uploadingState = MutableLiveData<ArrayList<Uploading>>()
    var currentUri: Uri? = null
    var currentMime: String? = null
    var currentSize: Point? = null
    var currentDescription: String? = null
    var currentTags: Map<Int, String>? = null

    val uploadingList = ObservableField<SparseArray<Reactive.UploadState>>().apply {
        set(SparseArray())
    }

    @SuppressLint("CheckResult")
    fun upload2(description: String, tags: Map<String, String>) {
        Reactive.uploadContent(this, description, tags)
            .subscribe {
                Log.d(TAG, it.toString())
                when(it.state) {
                    Reactive.State.Start -> uploadingList.get()?.append(it.id, it)
                    Reactive.State.Pending -> uploadingList.get()?.append(it.id, it)
                    Reactive.State.Finish -> uploadingList.get()?.append(it.id, it)
                }
                uploadingList.notifyChange()
            }
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

    fun addUploadState(uploading: Uploading) {
        uploadingState.value!!.add(uploading)
        uploadingState.notifyObserver()
    }

    fun updateUploadState(uploading: Uploading) {
        uploadingState.notifyObserver()
    }

    fun removeUploadState(uploading: Uploading) {
        uploadingState.value!!.remove(uploading)
        uploadingState.notifyObserver()
    }
}