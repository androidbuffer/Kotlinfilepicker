package com.androidbuffer.kotlinfilepickersample

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.androidbuffer.kotlinfilepicker.KotConstants
import com.androidbuffer.kotlinfilepicker.KotRequest
import com.androidbuffer.kotlinfilepicker.KotlinFilePicker

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var tvDetails: TextView
    lateinit var btnCamera: Button
    lateinit var btnGallery: Button
    lateinit var btnFile: Button
    lateinit var btnVideo: Button
    lateinit var ivPicture: ImageView
    lateinit var vvMovie: VideoView
    private val REQUEST_CAMERA = 101
    private val REQUEST_GALLERY = 102
    private val REQUEST_FILE = 103
    private val REQUEST_VIDEO = 104

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setting the view elements
        tvDetails = findViewById(R.id.tvDetails)
        btnCamera = findViewById(R.id.btnCamera)
        btnFile = findViewById(R.id.btnFile)
        btnGallery = findViewById(R.id.btnGallery)
        ivPicture = findViewById(R.id.ivPicture)
        btnVideo = findViewById(R.id.btnVideo)
        vvMovie = findViewById(R.id.vvMovie)

        //setting the click listener
        btnGallery.setOnClickListener(this)
        btnCamera.setOnClickListener(this)
        btnFile.setOnClickListener(this)
        btnVideo.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        vvMovie.visibility = View.GONE
        if (p0 is Button) {
            when (p0.text) {
                getString(R.string.button_camera) -> openCamera()
                getString(R.string.button_file) -> openFile()
                getString(R.string.button_gallery) -> openGallery()
                getString(R.string.button_video) -> openVideo()
            }
        }
    }

    private fun openCameraFromClass() {
        //opens camera from camera class
        KotRequest.Camera(this).setRequestCode(REQUEST_CAMERA).pick()
    }

    private fun openCamera() {
        //opens a camera intent
        var cameraIntent = Intent(this, KotlinFilePicker::class.java)
        cameraIntent.putExtra(KotConstants.EXTRA_FILE_SELECTION, KotConstants.SELECTION_TYPE_CAMERA)
        startActivityForResult(cameraIntent, REQUEST_CAMERA)
    }

    private fun openVideo() {
        //opens a camera intent
        var videoIntent = Intent(this, KotlinFilePicker::class.java)
        videoIntent.putExtra(KotConstants.EXTRA_FILE_SELECTION, KotConstants.SELECTION_TYPE_VIDEO)
        startActivityForResult(videoIntent, REQUEST_VIDEO)
    }

    private fun openGallery() {
        //opens a gallery intent
        var galleryIntent = Intent(this, KotlinFilePicker::class.java)
        galleryIntent.putExtra(KotConstants.EXTRA_FILE_SELECTION, KotConstants.SELECTION_TYPE_GALLERY)
        galleryIntent.putExtra(KotConstants.EXTRA_MULTIPLE_ENABLED, true)
        startActivityForResult(galleryIntent, REQUEST_GALLERY)
    }

    private fun openFile() {
        //opens a file intent
        var galleryIntent = Intent(this, KotlinFilePicker::class.java)
        galleryIntent.putExtra(KotConstants.EXTRA_FILE_SELECTION, KotConstants.SELECTION_TYPE_FILE)
        galleryIntent.putExtra(KotConstants.EXTRA_MULTIPLE_ENABLED, true)
        startActivityForResult(galleryIntent, REQUEST_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_CAMERA == requestCode && resultCode == Activity.RESULT_OK) {
            val uri = data?.getParcelableArrayListExtra<Uri?>(KotConstants.EXTRA_FILE_RESULTS)
            ivPicture.setImageURI(uri?.get(0))
            tvDetails.setText(uri.toString())
        } else if (REQUEST_FILE == requestCode && resultCode == Activity.RESULT_OK) {
            val uri = data?.getParcelableArrayListExtra<Uri?>(KotConstants.EXTRA_FILE_RESULTS)
            ivPicture.setImageURI(uri?.get(0))
            tvDetails.setText(uri.toString())
        } else if (REQUEST_GALLERY == requestCode && resultCode == Activity.RESULT_OK) {
            val uri = data?.getParcelableArrayListExtra<Uri?>(KotConstants.EXTRA_FILE_RESULTS)
            ivPicture.setImageURI(uri?.get(0))
            tvDetails.setText(uri.toString())
        } else if (REQUEST_VIDEO == requestCode && resultCode == Activity.RESULT_OK) {
            val uri = data?.getParcelableArrayListExtra<Uri?>(KotConstants.EXTRA_FILE_RESULTS)
            tvDetails.setText(uri.toString())
            vvMovie.setMediaController(MediaController(this));
            vvMovie.setVideoURI(uri?.get(0));
            vvMovie.requestFocus();
            vvMovie.start();
            vvMovie.visibility = View.VISIBLE
        }
    }
}
