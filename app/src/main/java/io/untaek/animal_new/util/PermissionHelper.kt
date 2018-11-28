package io.untaek.animal_new.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionHelper {
    companion object {
        private val PERMISSIONS = arrayListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        const val REQUEST_READ_EXTERNAL_STORAGE = 596

        fun allPermissions() = PERMISSIONS

        fun deniedPermissions(context: Context) = PERMISSIONS.filter {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_DENIED
        }.toTypedArray()

        fun grantedPermissions(context: Context) = PERMISSIONS.filter {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int)
                = ActivityCompat.requestPermissions(activity, permissions, requestCode)

        fun requestPermission(activity: Activity, permission: String, requestCode: Int)
                = ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)

        fun handleRequestResult(permissions: Array<out String>, grantResults: IntArray, success: (() -> Any)?, fail: (() -> Any)?) {
            if(grantResults.all { it == PackageManager.PERMISSION_GRANTED }) { success?.invoke() }
            else { fail?.invoke() }
        }

        fun checkIsPermissionDenied(context: Context, permission: String) = ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED

        fun checkAndRequestPermission(context: Context, permission: String, requestCode: Int): Boolean{
            if(!checkIsPermissionDenied(context, permission)) {
                return true
            }
            requestPermission(context as Activity, permission, requestCode)
            return false
        }
    }
}