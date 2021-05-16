package com.github.utn.frba.mobile.dextracker

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object Permisos {
    private fun hasPermissions(context: Context, permissionCode: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permissionCode) == PackageManager.PERMISSION_GRANTED
    }

    /// Retorna true si ya tiene permiso
    fun checkForPermissions(activity: Activity, permissionCode: String, requestCode: Int, reason: String): Boolean {
        if (hasPermissions(activity, permissionCode)) {
            return true
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionCode)) {
            showStoragePermissionExplanation(activity, permissionCode, reason, requestCode)
        }
        else {
            dispatchCameraPermissionRequest(activity, permissionCode, requestCode)
        }
        return false
    }

    private fun dispatchCameraPermissionRequest(activity: Activity, permissionCode: String, requestCode: Int) {
        activity.requestPermissions(arrayOf(permissionCode), requestCode)
    }

    private fun showStoragePermissionExplanation(activity: Activity, permissionCode: String, reason: String, requestCode: Int) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Necesitamos tu permiso")
        builder.setCancelable(true)
        builder.setMessage(reason)
        builder.setPositiveButton("OK") { _, _ ->
            dispatchCameraPermissionRequest(activity, permissionCode, requestCode)
        }
        builder.show()
    }
}