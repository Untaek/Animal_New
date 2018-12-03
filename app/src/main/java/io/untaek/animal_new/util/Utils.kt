package io.untaek.animal_new.util

import android.Manifest
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.ObservableArrayList
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import io.untaek.animal_new.component.PickContentButton
import io.untaek.animal_new.list.comment.CommentsAdapter
import io.untaek.animal_new.list.timeline.TimelineAdapter
import io.untaek.animal_new.type.Comment
import io.untaek.animal_new.type.Content
import io.untaek.animal_new.type.Post
import io.untaek.animal_new.viewmodel.UploadViewModel
import java.io.File
import java.util.*

@BindingAdapter("binding")
fun bind(recyclerView: RecyclerView, items: ObservableArrayList<Post>){
    val adapter: TimelineAdapter = recyclerView.adapter as? TimelineAdapter ?: TimelineAdapter(recyclerView.context as FragmentActivity)
    adapter.notifyDataSetChanged()
}

@BindingAdapter("bind_comment")
fun bind2(recyclerView: RecyclerView, items: ObservableArrayList<Comment>){
    val adapter: CommentsAdapter = recyclerView.adapter as? CommentsAdapter ?: CommentsAdapter(recyclerView.context as FragmentActivity)
    adapter.notifyDataSetChanged()
}

val userImageOptions = RequestOptions()
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .override(80)
    .circleCrop()

fun contentImageOptions(x: Int, y: Int) = RequestOptions()
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .override(x, y)
    .centerCrop()

fun fullContentImageOptions(x: Int, y: Int) = RequestOptions()
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .override(x, y)
    .fitCenter()

@BindingAdapter("user_image")
fun loadUserImage(imageView: ImageView, url: String) {
    val options= userImageOptions

    Glide.with(imageView)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply(options)
        .into(imageView)
}

@BindingAdapter("content")
fun load1(imageView: ImageView, content: Content) {
    /**
     * content.mime could be like a image/jpeg or image/png or image/gif etc.
     */
    val type = content.mime.split("/")[1]
    val screen = ContentUtil.screenSize(imageView.context)

    val width: Int = screen.x
    var height: Int = content.height * screen.x / content.width

    height = when(height > screen.y * 0.65) {
        true -> (height * 0.65).toInt()
        false -> height
    }

    if(content.width < content.height) {
        height = when(height > screen.y * 0.65) {
            true -> (height * 0.75).toInt()
            false -> height
        }
    }

    val options = contentImageOptions(width, height)

    imageView.layoutParams = FrameLayout.LayoutParams(width, height).apply { gravity = Gravity.CENTER }

    Glide.with(imageView).also { if (type == "gif") it.asGif() }
        .load(content.url)
        .transition(DrawableTransitionOptions.withCrossFade(500))
        .apply(options)
        .thumbnail(0.1f)
        .into(imageView)
}

@BindingAdapter("full_content")
fun load2(imageView: ImageView, content: Content) {
    val type = content.mime.split("/")[1]

    val options = fullContentImageOptions(content.width, content.height)

    Glide.with(imageView).also { if (type == "gif") it.asGif() }
        .load(content.url)
        .transition(DrawableTransitionOptions.withCrossFade(400))
        .apply(options)
        .thumbnail(0.1f)
        .into(imageView)
}

@BindingAdapter("time")
fun timeCalculateFunction(textView: TextView, time: Date) {
    val currentTime = Calendar.getInstance().apply { this.time = Date() }
    val postTime = Calendar.getInstance().apply { this.time = time }
    val diff = (currentTime.timeInMillis - postTime.timeInMillis) / 1000

    val minutes = (diff / 60) % 60
    val hours = (diff / 60 / 60) % 24
    val days = (diff / 24 / 60 / 60) % 30
    val months = (diff / 30 / 24 / 60 / 60) % 12
    val years = (diff / 12 / 30 / 24 / 60 / 60)

    textView.text = when {
        (years > 0) -> "$years 년 전"
        (months > 0) -> "$months 달 전"
        (days > 0) -> "$days 일 전"
        (hours > 0) -> "$hours 시간 전"
        (minutes > 0) -> "$minutes 분 전"
        else -> "방금 전"
    }
}

fun download(view: View, content: Content) {
    Glide.with(view)
        .asFile()
        .load(content.url)
        .listener(object : RequestListener<File> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<File>?,
                isFirstResource: Boolean
            ): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResourceReady(
                resource: File?,
                model: Any?,
                target: Target<File>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                if(!PermissionHelper
                        .checkAndRequestPermission(
                            view.context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            PermissionHelper.REQUEST_READ_EXTERNAL_STORAGE)
                ) { return false }

                val dir = File(Environment.getExternalStorageDirectory(), "Animal")
                val file = File(dir, content.file_name)

                if(dir.exists() || dir.mkdirs()) {
                    Log.d("Download", "Directory created ${dir.absolutePath}")
                }

                try{
                    resource!!.copyTo(file)
                    Log.d("Download", "to ${file.absolutePath}")
                    Toast.makeText(view.context, "Saved", Toast.LENGTH_SHORT).show()
                }catch (e: FileAlreadyExistsException) {
                    Log.d("Download", "File already exists")
                }

                return false
            }

        })
        .submit()
}


