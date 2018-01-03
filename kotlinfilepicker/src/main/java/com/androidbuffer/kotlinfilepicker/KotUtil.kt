package com.androidbuffer.kotlinfilepicker

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by AndroidBuffer on 3/1/18.
 */
public class KotUtil {


    companion object {
        fun getCameraIntent(): Intent {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            return cameraIntent
        }
    }

    private fun createImageFile(context: Context): File {
        val fileName = "image_" + createFileName()
        val file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", file)
    }

    private fun createFileName(): String {
        return SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(Date())
    }
}