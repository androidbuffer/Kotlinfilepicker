package com.androidbuffer.kotlinfilepicker

/**
 * Created by AndroidBuffer on 28/12/17.
 */

//extra intent constants
public const val EXTRA_MULTIPLE_ENABLED = "extraMultipleEnabled"
public const val EXTRA_FILE_SELECTION = "extraFileSelection"

//file selection type
public const val SELECTION_TYPE_CAMERA = "Gallery"
public const val SELECTION_TYPE_GALLERY = "Camera"
public const val SELECTION_TYPE_FILE = "File"

//files types supported
val FILE_TYPE_IMAGE_ALL = "image/*"
val FILE_TYPE_FILE_ALL = "*/*"
val FILE_TYPE_PDF = "application/pdf"
val FILE_TYPE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
val FILE_TYPE_EXCEL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"