package io.untaek.animal_new.component

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.Bindable
import androidx.databinding.ObservableInt
import io.untaek.animal_new.R
import kotlinx.android.synthetic.main.component_text_with_icon.view.*

class TextWithIcon: ConstraintLayout {
    var count: Int = 0
    var icon: Drawable = context.getDrawable(R.drawable.ic_bone)!!
    var color: Int = android.R.color.black

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        Log.d("TextWithIcon", "second constructor")
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TextWithIcon,
            0, 0).apply {

            try {
                count = getInteger(R.styleable.TextWithIcon_count, 0)
                icon = getDrawable(R.styleable.TextWithIcon_icon)!!
                color = getColor(R.styleable.TextWithIcon_color, 0)
                Log.d("TextWithIcon", "second constructor$count")
            }
            finally {
                recycle()
            }
        }

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.component_text_with_icon, this, false)
        view.textView_like.text = count.toString()
        view.icon.background = icon
        if(color != 0) {
            view.textView_like.setTextColor(color)
            view.icon.background.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }

        addView(view)
    }
}