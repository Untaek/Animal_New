package io.untaek.animal_new.util

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Point
import android.media.ExifInterface
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

object ContentUtil {
    private const val AUTHORITY = "io.untaek.animal_new.fileprovider"

    fun createTempUri(context: Context, prefix: String, suffix: String): Uri {
        val file = File.createTempFile(prefix, suffix)
        return FileProvider.getUriForFile(context, AUTHORITY, file)
    }

    fun getSize(context: Context, uri: Uri): Point {
        val mime = context.contentResolver.getType(uri)
        val type = mime.split("/")[0]

        if(type == "image") {
            val op = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri), null, op)

            val orientation = getOrientation(context, uri)

            val size = when(orientation){
                ExifInterface.ORIENTATION_NORMAL, ExifInterface.ORIENTATION_ROTATE_180 -> Point(op.outWidth, op.outHeight)
                ExifInterface.ORIENTATION_ROTATE_90, ExifInterface.ORIENTATION_ROTATE_270 -> Point(op.outHeight, op.outWidth)
                else -> Point(op.outWidth, op.outHeight)
            }

            return size
        }

        return Point()
    }

    private fun getOrientation(context: Context, uri: Uri): Int {
        val exif = androidx.exifinterface.media.ExifInterface(context.contentResolver.openInputStream(uri)!!)
        Log.d("ContentUtil", exif.getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED).toString())
        return exif.getAttributeInt(
            androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED)
    }

    fun getMime(context: Context, uri: Uri) = context.contentResolver.getType(uri)!!

    fun screenSize(context: Context) = Point().also { (context as Activity).windowManager.defaultDisplay.getSize(it) }
}