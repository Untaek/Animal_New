package io.untaek.animal_new.component

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ScrollView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.ObservableField
import io.untaek.animal_new.Reactive
import io.untaek.animal_new.activity.MainActivity
import io.untaek.animal_new.type.Content
import io.untaek.animal_new.type.Post
import io.untaek.animal_new.viewmodel.PostDetailViewModel

@BindingAdapter("onActionClicked")
fun method3(editText: EditText, vm: PostDetailViewModel) {
    val inputManager = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    editText.setOnEditorActionListener { _, _, _ ->
        vm.sendNewComment()
        true
    }
}

fun back(view: View) {
    (view.context as Activity).finish()
}

fun scrollBottom(scrollView: ScrollView) {
    scrollView.fullScroll(ScrollView.FOCUS_DOWN)
}

fun makeUploadIntent(view: View, content: Content, description: ObservableField<String>, tags: ObservableField<List<String>>) {
    Log.d("ComponentUtil", "makeUploadIntent ${description.get()} ${tags.get().toString()}")
    Intent().also {
        it.putExtra("content", content)
        it.putExtra("description", description.get())
        it.putExtra("tags", tags.get()?.toTypedArray())

        (view.context as Activity).setResult(Activity.RESULT_OK, it)
        (view.context as Activity).finish()
    }
}