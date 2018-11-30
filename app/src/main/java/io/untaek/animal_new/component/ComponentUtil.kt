package io.untaek.animal_new.component

import androidx.databinding.BindingAdapter
import io.untaek.animal_new.R

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