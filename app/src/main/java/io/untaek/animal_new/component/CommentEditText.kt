package io.untaek.animal_new.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import io.untaek.animal_new.databinding.ComponentCommentEditTextBinding
import io.untaek.animal_new.viewmodel.PostDetailViewModel

class CommentEditText: LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var binding: ComponentCommentEditTextBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding = ComponentCommentEditTextBinding.inflate(inflater, this, true)
        binding.vm = ViewModelProviders.of(context as FragmentActivity).get(PostDetailViewModel::class.java)
        binding.vm?.loading?.addOnPropertyChangedCallback(object: Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if((sender as ObservableBoolean).get()){
                    /**
                     * Start loading
                     */

                } else {
                    /**
                     * Finish loading
                     */

                    inputManager.hideSoftInputFromWindow(this@CommentEditText.applicationWindowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                    binding.editText.clearFocus()
                }
            }
        })

    }
}