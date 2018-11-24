package io.untaek.animal_new

import android.app.Activity
import android.graphics.Point
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import io.untaek.animal_new.list.TimelineAdapter
import io.untaek.animal_new.type.Content
import io.untaek.animal_new.type.Post
import java.lang.Exception
import java.util.*

/**
 * Does not work.
 */
//@BindingAdapter("binding")
//fun bind(recyclerView: RecyclerView, items: LiveData<List<Post>>){
//    val adapter: TimelineAdapter = recyclerView.adapter as? TimelineAdapter ?: TimelineAdapter()
//
//}

@BindingAdapter("user_image")
fun loadUserImage(imageView: ImageView, url: String) {
    Glide.with(imageView)
        .load(url)
        .apply(RequestOptions().circleCrop())
        .thumbnail(0.1f)
        .into(imageView)
}

@BindingAdapter("content")
fun load(imageView: ImageView, content: Content) {
    val screen = Point().also { (imageView.context as Activity).windowManager.defaultDisplay.getSize(it) }
    val start = System.currentTimeMillis()
    Picasso.get()
        .load(content.url)
        .resize(screen.x, 800)
        .centerCrop()
        .error(R.drawable.ic_launcher_foreground)
        .into(imageView, object : Callback{
            override fun onSuccess() {
                Log.d("Utils", "image Load finished ${System.currentTimeMillis() - start}")
            }

            override fun onError(e: Exception?) {

            }
        })
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
