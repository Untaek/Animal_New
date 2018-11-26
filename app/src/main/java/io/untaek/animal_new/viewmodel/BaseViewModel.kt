package io.untaek.animal_new.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import java.lang.Exception

open class BaseViewModel: ViewModel() {
    val TAG: String = this::class.java.simpleName
    init {
        Log.d(TAG, "ViewModel Created, hashcode: ${this.hashCode()}")
    }

    fun logException(e: Exception) {
        Log.d(TAG, "exception", e)
    }
}