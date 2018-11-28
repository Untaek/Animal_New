package io.untaek.animal_new.component

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import io.untaek.animal_new.R
import io.untaek.animal_new.util.PermissionHelper
import io.untaek.animal_new.viewmodel.UploadViewModel
import kotlinx.android.synthetic.main.component_pick_content_button.view.*
import java.io.File
import java.util.*

class PickContentButton: ConstraintLayout {
    companion object {
        private const val GALLERY = 0
        private const val CAMERA = 1
        const val REQUEST_GALLERY = 123
        const val REQUEST_CAMERA = 234
    }

    var icon = context.getDrawable(R.drawable.ic_photo_camera_black_24dp)
    var text = "Camera"
    var type = GALLERY

    val vm = ViewModelProviders.of(context as FragmentActivity).get(UploadViewModel::class.java)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PickContentButton,
            0, 0).apply {

            try {
                icon = getDrawable(R.styleable.PickContentButton_icon)!!
                text = getString(R.styleable.PickContentButton_text)!!
                type = getInteger(R.styleable.PickContentButton_type, GALLERY)
            }
            finally {
                recycle()
            }
        }

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.component_pick_content_button, this, false)
        view.setOnClickListener {
            when(type){
                GALLERY -> method1()
                CAMERA -> method2()
            }
        }
        view.icon.background = icon
        view.text.text = text
        addView(view)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun createTempUri(prefix: String, suffix: String): Uri {
        val file = File.createTempFile(prefix, suffix)
        return FileProvider.getUriForFile(context, "io.untaek.animal_new.fileprovider", file)
    }

    private fun method1() {
        val activity = context as AppCompatActivity
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }

        activity.startActivityForResult(intent, REQUEST_GALLERY)
    }

    private fun method2() {
        val activity = context as AppCompatActivity
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            vm.currentUri = createTempUri("USERID@${Date().time}", ".jpg")
            it.putExtra(MediaStore.EXTRA_OUTPUT, vm.currentUri)
            if(PermissionHelper.checkAndRequestPermission(context, Manifest.permission.CAMERA, REQUEST_CAMERA)) {
                activity.startActivityForResult(it, REQUEST_CAMERA)
            }
        }
    }
}