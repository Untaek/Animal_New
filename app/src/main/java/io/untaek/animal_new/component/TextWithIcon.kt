package io.untaek.animal_new.component

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.Bindable
import androidx.databinding.ObservableInt
import io.reactivex.Observable
import io.untaek.animal_new.R
import io.untaek.animal_new.databinding.ComponentTextWithIconBinding
import kotlinx.android.synthetic.main.component_text_with_icon.view.*

class TextWithIcon: ConstraintLayout {
    lateinit var binding: ComponentTextWithIconBinding

    var count = 0
    set(value) {
        binding.textViewLike.text = value.toString()
    }

    var icon = context.getDrawable(R.drawable.ic_bone)
    set(value) {
        binding.iconContainer.background = value
    }

    var color = Color.BLACK

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        Log.d("TextWithIcon", "second constructor")
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ComponentTextWithIconBinding.inflate(inflater, this, true)

        context.obtainStyledAttributes(
            attrs,
            R.styleable.TextWithIcon,
            0, 0).apply {

            try {
                binding.count = getInteger(R.styleable.TextWithIcon_count, 0)
                binding.icon = getDrawable(R.styleable.TextWithIcon_icon)
                binding.color = getColor(R.styleable.TextWithIcon_color, Color.BLACK)
                Log.d("TextWithIcon", "second constructor${binding.count}")

            }
            finally {
                recycle()
            }
        }
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}