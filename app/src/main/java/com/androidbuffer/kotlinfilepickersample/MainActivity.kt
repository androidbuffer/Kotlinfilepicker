package com.androidbuffer.kotlinfilepickersample

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.androidbuffer.kotlinfilepicker.KotConstants
import com.androidbuffer.kotlinfilepicker.KotlinFilePicker

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var tvDetails: TextView
    lateinit var btnCamera: Button
    lateinit var btnGallery: Button
    lateinit var btnFile: Button
    lateinit var ivPicture: ImageView
    private val REQUEST_CAMERA = 101
    private val REQUEST_GALLERY = 102
    private val REQUEST_FILE = 103

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setting the view elements
        tvDetails = findViewById(R.id.tvDetails)
        btnCamera = findViewById(R.id.btnCamera)
        btnFile = findViewById(R.id.btnFile)
        btnGallery = findViewById(R.id.btnGallery)
        ivPicture = findViewById(R.id.ivPicture)

        //setting the click listener
        btnGallery.setOnClickListener(this)
        btnCamera.setOnClickListener(this)
        btnFile.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (p0 is Button) {
            when (p0.text) {
                getString(R.string.button_camera) -> openCamera()
                getString(R.string.button_file) -> openFile()
                getString(R.string.button_gallery) -> openGallery()
            }
        }
    }

    private fun openCamera() {
        //opens a camera intent
        var cameraIntent = Intent(this, KotlinFilePicker::class.java)
        cameraIntent.putExtra(KotConstants.EXTRA_FILE_SELECTION, KotConstants.SELECTION_TYPE_CAMERA)
        startActivityForResult(cameraIntent, REQUEST_CAMERA)
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
        } else if (REQUEST_FILE == requestCode && resultCode == Activity.RESULT_OK) {
            val uri = data?.getParcelableArrayListExtra<Uri?>(KotConstants.EXTRA_FILE_RESULTS)
            ivPicture.setImageURI(uri?.get(0))
        } else if (REQUEST_GALLERY == requestCode && resultCode == Activity.RESULT_OK) {
            val uri = data?.getParcelableArrayListExtra<Uri?>(KotConstants.EXTRA_FILE_RESULTS)
            ivPicture.setImageURI(uri?.get(0))
        }
    }
}
