package io.untaek.animal_new

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.untaek.animal_new.list.TimelineAdapter
import io.untaek.animal_new.type.Post
import java.util.*

@BindingAdapter("binding")
fun bind(recyclerView: RecyclerView, items: ObservableArrayList<Post>){
    val adapter: TimelineAdapter = recyclerView.adapter as? TimelineAdapter ?: TimelineAdapter()
    adapter.setItems(items)
}

@BindingAdapter("url")
fun load(imageView: ImageView, url: String) {
    Picasso.get().load(url).into(imageView)
}

@BindingAdapter("time")
fun timeCalculateFunction(textView: TextView, time: Date) {
    val result: String
    val gc = GregorianCalendar(TimeZone.getTimeZone("Asia/Soul"))
    val gc_year = gc.get(GregorianCalendar.YEAR)
    val gc_month = gc.get(GregorianCalendar.MONTH)
    val gc_date = gc.get(GregorianCalendar.DATE)
    val gc_hour = gc.get(GregorianCalendar.HOUR)
    val gc_minute = gc.get(GregorianCalendar.MINUTE)

    val time_year = time.year
    val time_month = time.month
    val time_date = time.date
    val time_hour = time.hours
    val time_minute = time.minutes

    if (time_year - gc_year > 0) {
        result = "" + (time_year - gc_year).toString() + "년 전"
    } else if (time_month - gc_month > 0) {
        result = "" + (time_month - gc_month) + "달 전"
    } else if (time_date - gc_date > 0) {
        result = "" + (time_date - gc_date) + "일 전"
    } else if (time_hour - gc_hour > 0) {
        result = "" + (time_hour - gc_hour) + "시간 전"
    } else if (time_minute - gc_minute > 0) {
        result = "" + (time_minute - gc_minute) + "분 전"
    }else{
        result = "몇초 전 "
    }

    textView.text = result
}
