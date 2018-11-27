package io.untaek.animal_new.list.timeline

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TimelineDecorator: RecyclerView.ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val left = parent.paddingLeft
        val right = parent.right - parent.paddingRight

    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val last = parent.adapter?.itemCount!! - 1

        if(last > parent.getChildAdapterPosition(view)){
            outRect.set(outRect.left, outRect.top, outRect.right, outRect.bottom + 80)
        }
    }
}