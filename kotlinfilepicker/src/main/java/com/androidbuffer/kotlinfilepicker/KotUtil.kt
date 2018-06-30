package com.androidbuffer.kotlinfilepicker

import android.content.*
import android.database.Cursor
import android.database.CursorIndexOutOfBoundsException
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.webkit.MimeTypeMap
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.text.TextUtils
import java.util.regex.Pattern
import android.content.DialogInterface
import android.support.v4.content.ContextCompat.startActivity
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.content.Intent
import android.app.Activity
import android.provider.Settings
import android.support.v7.app.AlertDialog


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
            val intent: Intent
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            } else {
                intent = Intent(Intent.ACTION_GET_CONTENT)
            }
            intent.type = mimeType
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
            val intent: Intent
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            } else {
                intent = Intent(Intent.ACTION_GET_CONTENT)
            }
            intent.type = mimeType
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultiple)
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
            return SimpleDateFormat("ddMMyyyy_HHmmssSS", Locale.getDefault()).format(Date())
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
                intent.clipData = clip
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }

        fun getFileDetails(context: Context, uri: Uri): File? {
            //get the details from uri
            var fileToReturn: File? = null
            try {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    val tables = arrayOf(MediaStore.Images.Media.DATA)
                    val cursorLoader = CursorLoader(context, uri, tables, null, null, null)
                    val cursor = cursorLoader.loadInBackground()
                    val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)

                    if (cursor.moveToNext()) {
                        val result = cursor.getString(columnIndex)
                        fileToReturn = File(result)
                    }
                    cursor.close()

                } else {
                    fileToReturn = File(readPathFromUri(context, uri))
                }
            } catch (exp: CursorIndexOutOfBoundsException) {
                exp.printStackTrace()
                fileToReturn = File(uri.path)
            } catch (exp: NullPointerException) {
                exp.printStackTrace()
                fileToReturn = File(uri.path)
            } catch (exp: NumberFormatException) {
                exp.printStackTrace()
                fileToReturn = File(uri.path)
            }
            return fileToReturn
        }

        /**
         * get the extension from path of the file
         * @param url
         */
        fun getMimeType(url: String): String {
            val extension = getFileExtensionFromUrl(url)
            if (!extension.isEmpty()) {
                val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase())
                if (type.isNullOrBlank()) {
                    return "*/*"
                } else {
                    return type
                }
            } else {
                return "*/*"
            }
        }

        /**
         * get the date in format dd/MM/yyyy from long date
         * @param date
         * @return string date
         */
        fun getDateModified(date: Long): String {
            val simpleDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return simpleDate.format(date)
        }

        /**
         * get a file extension from the file path
         * @param url
         */
        fun getFileExtensionFromUrl(passedUrl: String): String {
            var url = passedUrl
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
                // consider it valid for our matching purposes except space:
                if (!filename.isEmpty() && Pattern.matches("[\\sa-zA-Z_0-9\\.\\-\\(\\)\\%]+", filename)) {
                    val dotPos = filename.lastIndexOf('.')
                    if (0 <= dotPos) {
                        return filename.substring(dotPos + 1)
                    }
                }
            }

            return ""
        }

        /**
         * returns the path from uri for API level 19 and up
         */
        @SuppressWarnings("NewApi")
        private fun readPathFromUri(context: Context, uri: Uri): String? {

            // DocumentProvider
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }
                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)!!)

                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    val contentUri = getContentUri(type)

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }// MediaProvider
                // DownloadsProvider
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                // Return the remote address
                return if (isGooglePhotosUri(uri)) {
                    uri.lastPathSegment
                } else getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }

            return null
        }

        /**
         * checks if the given uri is of type external storage
         */
        private fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        /**
         * checks if the given uri is of type downloads
         */
        private fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        /**
         * checks if the given uri is of type media document
         */
        private fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        /**
         * returns the column for specified uri
         */
        private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
            var cursor: Cursor? = null
            val column = MediaStore.Images.Media.DATA
            val projection = arrayOf(column)

            try {
                cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                if (cursor != null && cursor!!.moveToFirst()) {
                    val column_index = cursor!!.getColumnIndexOrThrow(column)
                    return cursor!!.getString(column_index)
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } finally {
                if (cursor != null) {
                    cursor!!.close()
                }
            }
            return null
        }

        /**
         * checks the type of uri
         */
        private fun getContentUri(type: String): Uri? {
            when (type) {
                "image" -> return MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                "video" -> return MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                "audio" -> return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            return null
        }

        /**
         * checks if the uri is of type google photos
         */
        private fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }

        /**
         * opens the settings activity for app
         * @param activity
         * @param
         */
        fun openSettingsDialog(activity: Activity, wantToFinishOnOk: Boolean) {
            val alertBuilder = AlertDialog.Builder(activity)

            alertBuilder.setPositiveButton(R.string.dialog_settings_button, { dialogInterface, i ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivity(intent)
            })
            alertBuilder.setNegativeButton(R.string.dialog_finish_button, { dialogInterface, i ->
                dialogInterface.dismiss()
                if (wantToFinishOnOk) {
                    activity.finish()
                }
            })
            alertBuilder.setMessage(R.string.dialog_permissions_message)
            alertBuilder.setCancelable(false)
            alertBuilder.create().show()
        }

    }

}