package io.untaek.animal_new.component

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.BindingAdapter
import io.untaek.animal_new.R
import io.untaek.animal_new.viewmodel.PostDetailViewModel

@BindingAdapter("status_count")
fun method1(textWithIcon: TextWithIcon, count: Int) {
    textWithIcon.binding.count = count
}

@BindingAdapter("status_icon")
fun method2(textWithIcon: TextWithIcon, status: Boolean) {
    if(status) {
        textWithIcon.binding.icon = textWithIcon.context.getDrawable(R.drawable.ic_fill_bone)
    }
    else{
        textWithIcon.binding.icon = textWithIcon.context.getDrawable(R.drawable.ic_bone)
    }
}

@BindingAdapter("onActionClicked")
fun method3(editText: EditText, vm: PostDetailViewModel) {
    val inputManager = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    editText.setOnEditorActionListener { _, _, _ ->
        vm.sendNewComment()
        true
    }
}
