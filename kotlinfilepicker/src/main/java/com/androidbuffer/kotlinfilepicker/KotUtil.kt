package com.androidbuffer.kotlinfilepicker

import android.content.ClipData
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.webkit.MimeTypeMap
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.text.TextUtils
import java.util.regex.Pattern


/**
 * Created by AndroidBuffer on 3/1/18.
 */
class KotUtil {

    companion object {

        private val authority = BuildConfig.APPLICATION_ID + ".fileprovider"

        /**
         * @return {@link Intent}
         * @param context
         * returns a intent for camera
         */
        fun getCameraIntent(context: Context): Intent? {
            //returns a camera intent with temp file location
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(context.packageManager) == null) {
                return null
            }
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
        fun getVideoIntent(context: Context): Intent? {
            //returns a video recording intent with temp file location
            val videoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            if (videoIntent.resolveActivity(context.packageManager) == null) {
                return null
            }
            val file = createVideoFile(context)
            if (file != null) {
                val uri = getUriFromFile(context, file)
                grantUriPermission(context, videoIntent, uri)
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
        fun getGalleryIntent(mimeType: String, isMultiple: Boolean): Intent {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType(mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultiple)
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            return intent
        }

        /**
         * @return {@link Intent}
         * @param mimeType
         * returns a intent for file
         */
        fun getFileIntent(mimeType: String, isMultiple: Boolean): Intent {
            val intent = Intent()
            intent.setType(mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultiple)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.action = Intent.ACTION_OPEN_DOCUMENT
            } else {
                intent.action = Intent.ACTION_GET_CONTENT
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
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
            //grant the uri permission to all the api versions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                val clip = ClipData.newUri(context.contentResolver, "camera", uri)
                intent.setClipData(clip)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }

        fun getFileDetails(context: Context, uri: Uri): File? {
            //get the details from uri
            var fileToReturn: File? = null
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            val tables = arrayOf(MediaStore.Images.Media.DATA)
            val cursorLoader = CursorLoader(context, uri, tables, null, null, null)
            val cursor = cursorLoader.loadInBackground()
            val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            if (cursor.moveToNext()) {
                val result = cursor.getString(columnIndex)
                fileToReturn = File(result)
            }
            cursor.close()
//            } else {
//                val documentId = DocumentsContract.getDocumentId(uri)
//                val tables = arrayOf(MediaStore.Images.Media.DATA)
//                val selection = MediaStore.Images.Media._ID + "=?"
//                val cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, tables, selection, arrayOf(documentId), null)
//                val columnIndex = cursor.getColumnIndex(tables[0])
//                if (cursor.moveToNext()) {
//                    val result = cursor.getString(columnIndex)
//                    fileToReturn = File(result)
//                }
//                cursor.close()
//            }
            return fileToReturn
        }

        /**
         * get the extension from path of the file
         * @param url
         */
        fun getMimeType(url: String): String {
            val extension = getFileExtensionFromUrl(url)
            if (!extension.isEmpty()) {
                return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase())
            } else {
                return "*/*";
            }
        }

        /**
         * get the date in format dd/MM/yyyy from long date
         */
        fun getDateModified(date: Long): String {
            val simpleDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return simpleDate.format(date)
        }

        fun getFileExtensionFromUrl(url: String): String {
            var url = url
            if (!TextUtils.isEmpty(url)) {
                val fragment = url.lastIndexOf('#')
                if (fragment > 0) {
                    url = url.substring(0, fragment)
                }

                val query = url.lastIndexOf('?')
                if (query > 0) {
                    url = url.substring(0, query)
                }

                val filenamePos = url.lastIndexOf('/')
                val filename = if (0 <= filenamePos) url.substring(filenamePos + 1) else url

                // if the filename contains special characters, we don't
                // consider it valid for our matching purposes:
                if (!filename.isEmpty() && Pattern.matches("[\\sa-zA-Z_0-9\\.\\-\\(\\)\\%]+", filename)) {
                    val dotPos = filename.lastIndexOf('.')
                    if (0 <= dotPos) {
                        return filename.substring(dotPos + 1)
                    }
                }
            }

            return ""
        }

    }

}