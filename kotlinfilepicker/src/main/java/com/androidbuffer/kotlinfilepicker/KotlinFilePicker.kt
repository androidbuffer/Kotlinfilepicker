package com.androidbuffer.kotlinfilepicker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

/**
 * Created by AndroidBuffer on 28/12/17.
 */

public class KotlinFilePicker : AppCompatActivity() {

    private val TAG = KotlinFilePicker().javaClass.canonicalName;
    private val REQUEST_MEDIA_CAPTURE = 101
    private val REQUEST_MEDIA_FILE = 102
    private val REQUEST_MEDIA_GALLERY = 103
    private val PERMISSION_REQUEST_STORAGE = 100
    private lateinit var intentPick: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handlePermissionCheck()
        handleIntent(intent)
    }

    private fun handlePermissionCheck() {
        //check for the permission before accessing storage
        val permissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionGranted == PackageManager.PERMISSION_GRANTED) {
            return
        }
        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_STORAGE)
    }

    private fun handleIntent(intent: Intent) : Intent?{
        //handle the intent passed from the client
        val selection = intent.getStringExtra(EXTRA_FILE_SELECTION)
        val isMultipleEnabled = intent.getBooleanExtra(EXTRA_MULTIPLE_ENABLED,false)
        when (selection) {
            SELECTION_TYPE_CAMERA-> KotUtil.getCameraIntent()
        }
        return null
    }

    private fun showToast(msg: String) {
        //show a toast
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (PERMISSION_REQUEST_STORAGE == requestCode) {
            for (permission in grantResults) {
                if (permission == PackageManager.PERMISSION_DENIED) {
                    throw IllegalArgumentException("Permission not granted for storage read and write ${permissions}")
                }
            }
        }
    }
}