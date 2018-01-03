package com.androidbuffer.kotlinfilepicker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

/**
 * Created by AndroidBuffer on 28/12/17.
 */

public class KotlinFilePicker : AppCompatActivity() {

    private val TAG = KotlinFilePicker::class.java.canonicalName
    private val REQUEST_MEDIA_CAPTURE = 101
    private val REQUEST_MEDIA_FILE = 102
    private val REQUEST_MEDIA_GALLERY = 103
    private val PERMISSION_REQUEST_STORAGE = 100
    private var intentPick: Intent? = null

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

    private fun handleIntent(intent: Intent) {
        //handle the intent passed from the client
        val selection = intent.getStringExtra(KotConstants.EXTRA_FILE_SELECTION)
        val isMultipleEnabled = intent.getBooleanExtra(KotConstants.EXTRA_MULTIPLE_ENABLED, false)
        intentPick = getSelectedIntent(selection)
        if (intentPick != null) {
            startActivityForResult(intentPick, REQUEST_MEDIA_CAPTURE)
        } else {
            throwException(getString(R.string.exception_msg_illegal_))
        }
    }

    private fun getSelectedIntent(selection: String): Intent? {
        //returns a intent according to selection of file
        return when (selection) {
            KotConstants.SELECTION_TYPE_CAMERA -> KotUtil.getCameraIntent(this)
            else -> null
        }
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
                    throwException("Permission not granted for storage read and write ${permissions}")
                }
            }
        }
    }

    private fun throwException(msg: String) {
        //throws a exception in case of
        throw IllegalArgumentException(msg)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_MEDIA_CAPTURE == requestCode && resultCode == Activity.RESULT_OK) {
            //received the camera intent data
            val fileUri = intentPick?.clipData?.getItemAt(0)?.uri
            deliverResultSuccess(fileUri)
        } else if (REQUEST_MEDIA_FILE == requestCode && resultCode == Activity.RESULT_OK) {
            //do something
        } else if (REQUEST_MEDIA_GALLERY == requestCode && resultCode == Activity.RESULT_OK) {
            //do something
        } else {
            deliverResultFailed()
        }
    }

    private fun deliverResultSuccess(uri: Uri?) {
        //returns the result back to calling activity
        val intent = Intent()
        intent.putExtra(KotConstants.EXTRA_FILE_RESULTS, uri)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun deliverResultFailed() {
        //marks the unsuccessful results delivery to parent activity
        setResult(Activity.RESULT_CANCELED, Intent())
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        deliverResultFailed()
    }

}