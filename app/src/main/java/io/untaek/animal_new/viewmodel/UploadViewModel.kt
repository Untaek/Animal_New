package io.untaek.animal_new.viewmodel

import android.graphics.Point
import android.net.Uri
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import io.untaek.animal_new.Fire
import io.untaek.animal_new.type.Uploading

class UploadViewModel: BaseViewModel() {
    val uploadingState = MutableLiveData<ArrayList<Uploading>>()
    var currentUri: Uri? = null
    var currentMime: String? = null
    var currentSize: Point? = null

    init {
        uploadingState.value = arrayListOf()
    }

    fun upload() {
        Fire.uploadContent(this)
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