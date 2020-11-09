package com.example.playground

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

interface PermissionManager {
    fun isPermissionGranted(permissionName: String): Boolean
    fun askForPermission(permissionName: String, requestCode: Int)
    fun isPermissionPermanentlyDenied(permission: String): Boolean
}

class DefaultPermissionManager(private val activity: Activity) : PermissionManager {

    override fun isPermissionGranted(permissionName: String): Boolean =
        ActivityCompat.checkSelfPermission(
            activity,
            permissionName
        ) == PackageManager.PERMISSION_GRANTED

    override fun askForPermission(permissionName: String, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, arrayOf(permissionName), requestCode);
    }

    override fun isPermissionPermanentlyDenied(permission: String): Boolean {
        return !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }
}