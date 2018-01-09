package com.androidbuffer.kotlinfilepicker

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.RequiresApi
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

        /**
         * @return {@link Intent}
         * @param context
         * returns a intent for camera
         */
        fun getCameraIntent(context: Context): Intent {
            //returns a camera intent with temp file location
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val file = createImageFile(context)
            if (file != null) {
                val uri = getUriFromFile(context, file)
                grantUriPermission(context, cameraIntent, uri)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            return cameraIntent
        }

        /**
         * @return {@link Intent}
         * @param context
         * returns a intent for video
         */
        fun getVideoIntent(context: Context): Intent {
            //returns a video recording intent with temp file location
            val videoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            val file = createVideoFile(context)
            if (file != null) {
                val uri = getUriFromFile(context, file)
                videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            videoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            return videoIntent
        }

        /**
         * @return {@link Intent}
         * @param isMultiple
         * @param mimeType
         * multiple select works for only API level 18 and above
         */
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        fun getGalleryIntent(mimeType: String, isMultiple: Boolean): Intent {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType(mimeType)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultiple)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            return intent
        }

        /**
         * @return {@link Intent}
         * @param mimeType
         * returns a intent for gallery
         */
        fun getGalleryIntent(mimeType: String): Intent {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType(mimeType)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            return intent
        }

        /**
         * @return {@link Intent}
         * @param mimeType
         * returns a intent for file
         */
        fun getFileIntent(mimeType: String): Intent {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType(mimeType)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            return intent
        }

        private fun createImageFile(context: Context): File? {
            //this returns a temp file object
            val fileName = "image_" + createFileName()
            val file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return if (file == null) return null else File.createTempFile(fileName, ".jpg", file)
        }

        private fun createVideoFile(context: Context): File? {
            //this returns a temp file object
            val fileName = "video" + createFileName()
            val file = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
            return if (file == null) return null else File.createTempFile(fileName, ".mp4", file)
        }

        private fun createFileName(): String {
            //this returns a string file name
            return SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(Date())
        }

        private fun getUriFromFile(context: Context, file: File): Uri {
            //returns uri from file object
            return FileProvider.getUriForFile(context, authority, file)
        }

        private fun grantUriPermission(context: Context, intent: Intent, uri: Uri) {
            //grant the uri permission to all the packages
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                val clip = ClipData.newUri(context.contentResolver, "camera", uri)
                intent.setClipData(clip)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            } else {
                val resInfoList = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                for (resolveInfo in resInfoList) {
                    val packageName = resolveInfo.activityInfo.packageName
                    context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                }
            }
        }

    }

}