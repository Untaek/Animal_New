package io.untaek.animal_new.component

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import io.untaek.animal_new.R
import io.untaek.animal_new.databinding.ComponentTextWithTextBinding

class TextWithText : ConstraintLayout {
    lateinit var binding: ComponentTextWithTextBinding
   // val vm = ViewModelProviders.of(context as FragmentActivity).get(MyPageViewModel()::class.java)

    var count = 0
    set(value) {
        binding.count?.set(value)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        Log.d("TextWithText", "second constructor")
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        binding = ComponentTextWithTextBinding.inflate(inflater, this, false)
        binding.count = ObservableInt()
        binding.name = ObservableField<String>()

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TextWithText,
            0, 0).apply {
            try{
                binding.name?.set(getString(R.styleable.TextWithText_name))
                binding.count?.set(getInteger(R.styleable.TextWithText_count, 0))
                Log.d("TextWithText", "second constructor${binding.count}")
            }
            finally {
                recycle()
            }
        }
        addView(binding.root)
    }
}