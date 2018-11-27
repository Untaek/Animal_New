package io.untaek.animal_new.util

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.untaek.animal_new.component.PickContentButton
import io.untaek.animal_new.type.Content
import io.untaek.animal_new.viewmodel.UploadViewModel
import java.io.File
import java.util.*

/**
 * Does not work.
 */
//@BindingAdapter("binding")
//fun bind(recyclerView: RecyclerView, items: LiveData<List<Post>>){
//    val adapter: TimelineAdapter = recyclerView.adapter as? TimelineAdapter ?: TimelineAdapter()
//
//}

val userImageOptions = RequestOptions()
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .circleCrop()

fun contentImageOptions(x: Int, y: Int) = RequestOptions()
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .override(x, y)
    .centerCrop()

@BindingAdapter("user_image")
fun loadUserImage(imageView: ImageView, url: String) {
    val options= userImageOptions

    Glide.with(imageView)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply(options)
        //.thumbnail(0.1f)
        .into(imageView)
}

@BindingAdapter("content")
fun load(imageView: ImageView, content: Content) {
    /**
     * content.mime could be like a image/jpeg or image/png or image/gif etc.
     */
    val type = content.mime.split("/")[1]
    val screen = Point().also { (imageView.context as Activity).windowManager.defaultDisplay.getSize(it) }
    val options = contentImageOptions(screen.x, 800)

    Glide.with(imageView).also { if (type == "gif") it.asGif() }
        .load(content.url)
        .transition(DrawableTransitionOptions.withCrossFade(500))
        .apply(options)
        .thumbnail(0.1f)
        .into(imageView)
}

@BindingAdapter("time")
fun timeCalculateFunction(textView: TextView, time: Date) {
    val result: String
    val gc = GregorianCalendar(TimeZone.getTimeZone("Asia/Seoul"))
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

//@BindingAdapter("download")
//fun download(view: View) {
//    /**
//     * TODO implementation
//     */
//}