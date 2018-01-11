package com.androidbuffer.kotlinfilepicker

import android.app.Activity
import android.content.Intent

/**
 * Created by AndroidBuffer on 11/1/18.
 */
public class KotRequest {

    /**
     * inner class for building the camera request
     */
    public class Camera(val context: Activity) {

        var requestCode = 101
        var intent: Intent
        var activity: Activity

        init {
            this.activity = context
            intent = Intent(context, KotlinFilePicker::class.java)
            intent.putExtra(KotConstants.EXTRA_FILE_SELECTION, KotConstants.SELECTION_TYPE_CAMERA)
        }

        /**
         * secondary constructor pass for initialization of camera class
         * @param requestCode by default it is 101 give a value to change it
         * @param context
         */
        constructor(context: Activity, requestCode: Int = 101) : this(context) {
            this.activity = context
            this.requestCode = requestCode
        }

        /**
         * set the request code & returns object of Camera class for launching the camera intent
         * @param requestCode for tracking in onActivityResult(...)
         * @return {@link Camera}
         */
        fun setRequestCode(requestCode: Int): Camera {
            this.requestCode = requestCode
            return this
        }

        /**
         * starts intent to capture image from camera KotlinFilePicker.class
         */
        fun pick() {
            activity.startActivityForResult(intent, requestCode)
        }
    }
}