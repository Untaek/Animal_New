package io.untaek.animal_new.util

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object ContentUtil {
    private const val AUTHORITY = "io.untaek.animal_new.fileprovider"

    fun createTempUri(context: Context, prefix: String, suffix: String): Uri {
        val file = File.createTempFile(prefix, suffix)
        return FileProvider.getUriForFile(context, AUTHORITY, file)
    }

    fun getSize(context: Context, uri: Uri): Pair<Int, Int> {
        val mime = context.contentResolver.getType(uri)
        val type = mime.split("/")[0]

        var width = -1
        var height = -1

        if(type == "image") {
            val op = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri), null, op)

            width = op.outWidth
            height = op.outHeight
        }

        return Pair(width, height)
    }

    fun getMime(context: Context, uri: Uri) = context.contentResolver.getType(uri)!!
}