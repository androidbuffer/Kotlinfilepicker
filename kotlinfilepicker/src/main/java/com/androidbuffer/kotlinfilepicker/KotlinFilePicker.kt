package com.androidbuffer.kotlinfilepicker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
    private val REQUEST_MEDIA_VIDEO = 104
    private val PERMISSION_REQUEST_STORAGE = 100
    private var intentPick: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (handlePermissionCheck()) {
                handleIntent(intent)
            } else {
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_STORAGE)
            }
        } else {
            handleIntent(intent)
        }
    }

    private fun handlePermissionCheck(): Boolean {
        //check for the permission before accessing storage
        val permissionGranted = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionGranted == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    private fun handleIntent(intent: Intent) {
        //handle the intent passed from the client
        val selection = intent.getStringExtra(KotConstants.EXTRA_FILE_SELECTION)
        val isMultipleEnabled = intent.getBooleanExtra(KotConstants.EXTRA_MULTIPLE_ENABLED, false)
        when (selection) {
            KotConstants.SELECTION_TYPE_CAMERA -> {
                //camera intent
                val cameraIntent = KotUtil.getCameraIntent(this)
                if (cameraIntent == null) {
                    throwException(getString(R.string.exception_msg_no_activity))
                    return
                }
                intentPick = cameraIntent
                startActivityForResult(cameraIntent, REQUEST_MEDIA_CAPTURE)
            }
            KotConstants.SELECTION_TYPE_GALLERY -> {
                //gallery intent
                val galleryIntent = KotUtil.getGalleryIntent(KotConstants.FILE_TYPE_IMAGE_ALL, isMultipleEnabled)
                startActivityForResult(galleryIntent, REQUEST_MEDIA_GALLERY)
            }
            KotConstants.SELECTION_TYPE_FILE -> {
                //file intent
                val fileIntent = KotUtil.getFileIntent(KotConstants.FILE_TYPE_FILE_ALL, isMultipleEnabled)
                startActivityForResult(fileIntent, REQUEST_MEDIA_FILE)
            }
            KotConstants.SELECTION_TYPE_VIDEO -> {
                val videoIntent = KotUtil.getVideoIntent(this)
                if (videoIntent == null) {
                    throwException(getString(R.string.exception_msg_no_activity))
                    return
                }
                intentPick = videoIntent
                startActivityForResult(videoIntent, REQUEST_MEDIA_VIDEO)
            }
            else -> {
                throwException(getString(R.string.exception_msg_illegal_))
            }
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
                    return
                }
            }
            handleIntent(intent)
        }
    }

    private fun throwException(msg: String) {
        //throws a exception in case of
        throw IllegalArgumentException(msg)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_MEDIA_CAPTURE == requestCode && resultCode == Activity.RESULT_OK) {
            //received the camera intent data
            val cameraUri = getUriList(intentPick)
            deliverResultSuccess(cameraUri)
        } else if (REQUEST_MEDIA_FILE == requestCode && resultCode == Activity.RESULT_OK) {
            //do something
            val fileUri = getUriList(data)
            deliverResultSuccess(fileUri)
        } else if (REQUEST_MEDIA_GALLERY == requestCode && resultCode == Activity.RESULT_OK) {
            //do something
            val galleryUri = getUriList(data)
            deliverResultSuccess(galleryUri)
        } else {
            deliverResultFailed()
        }
    }

    private fun deliverResultSuccess(uri: ArrayList<Uri?>) {
        //returns the result back to calling activity
        val intent = Intent()
        intent.putParcelableArrayListExtra(KotConstants.EXTRA_FILE_RESULTS, uri)
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

    private fun getUriList(intent: Intent?): ArrayList<Uri?> {
        //this returns a list of uri for passing back to parent intent
        val listUri = ArrayList<Uri?>()
        if (intent?.data == null) {
            //that means we may have data in clipdata
            val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                listUri.add(intent?.clipData?.getItemAt(0)?.uri)
            } else {
                intent?.data
            }
        } else {
            listUri.add(intent.data)
        }
        return listUri
    }

}