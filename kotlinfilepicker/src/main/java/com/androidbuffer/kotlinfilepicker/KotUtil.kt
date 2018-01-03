package com.androidbuffer.kotlinfilepicker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by AndroidBuffer on 3/1/18.
 */
public class KotUtil {

    companion object {

        private val authority = BuildConfig.APPLICATION_ID + ".fileprovider"

        fun getCameraIntent(context: Context): Intent {
            //returns a camera intent with temp file location
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val file = createImageFile(context)
            if (file != null) {
                val uri = getUriFromFile(context, file)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            return cameraIntent
        }

        fun getVideoIntent(context: Context): Intent {
            //returns a video recording intent with temp file location
            val cameraIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            val file = createVideoFile(context)
            if (file != null) {
                val uri = getUriFromFile(context, file)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            return cameraIntent
        }

        private fun createImageFile(context: Context): File {
            //this returns a temp file object
            val fileName = "image_" + createFileName()
            val file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(fileName, ".jpg", file)
        }

        private fun createVideoFile(context: Context): File {
            //this returns a temp file object
            val fileName = "video" + createFileName()
            val file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(fileName, ".mp4", file)
        }

        private fun createFileName(): String {
            //this returns a string file name
            return SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(Date())
        }

        private fun getUriFromFile(context: Context, file: File): Uri {
            //returns uri from file object
            return FileProvider.getUriForFile(context, authority, file)
        }
    }

}