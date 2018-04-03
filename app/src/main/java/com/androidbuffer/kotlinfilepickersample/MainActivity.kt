package com.androidbuffer.kotlinfilepickersample

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.androidbuffer.kotlinfilepicker.KotConstants
import com.androidbuffer.kotlinfilepicker.KotRequest
import com.androidbuffer.kotlinfilepicker.KotlinFilePicker

class MainActivity : AppCompatActivity(), Adapter.OnClickItemListener {

    val EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI"
    lateinit var rvFilePickerMain: RecyclerView
    private val REQUEST_CAMERA = 101
    private val REQUEST_GALLERY = 102
    private val REQUEST_FILE = 103
    private val REQUEST_VIDEO = 104
    private var adapter: Adapter? = null
    lateinit var titleArray: Array<String>
    private val drawableArray = arrayOf(R.drawable.ic_action_gallery,
            R.drawable.ic_action_gallery,
            R.drawable.ic_action_file,
            R.drawable.ic_action_file,
            R.drawable.ic_action_photo_camera,
            R.drawable.ic_action_camera)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //init the view elements
        rvFilePickerMain = findViewById(R.id.rvFilePickerMain);
        titleArray = resources.getStringArray(R.array.arrayOptions)
        setRecyclerView()
    }

    private fun setRecyclerView() {
        //here set the recycler view
        adapter = Adapter(titleArray, drawableArray, this)
        rvFilePickerMain.layoutManager = LinearLayoutManager(this)
        rvFilePickerMain.adapter = adapter
    }

    private fun openCamera() {
        //opens camera from camera class
        KotRequest.Camera(this, REQUEST_CAMERA).pick()
    }

    private fun openVideo() {
        //opens a camera intent
        KotRequest.Video(this, REQUEST_VIDEO).getVideoIntent()
    }

    private fun openGallery(isMultiple: Boolean) {
        //opens a gallery intent
        KotRequest.Gallery(this, REQUEST_GALLERY).isMultiple(isMultiple).pick()
    }

    private fun openFile() {
        //opens a file intent
       KotRequest.File(this,REQUEST_FILE).isMultiple(true).pick()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_CAMERA == requestCode && resultCode == Activity.RESULT_OK) {
            val uri = data?.getParcelableArrayListExtra<Uri?>(KotConstants.EXTRA_FILE_RESULTS)
            val intent = Intent(this, GalleryActivity::class.java)
            intent.putExtra(EXTRA_IMAGE_URI, uri)
            startActivity(intent)
        } else if (REQUEST_FILE == requestCode && resultCode == Activity.RESULT_OK) {
            val uri = data?.getParcelableArrayListExtra<Uri?>(KotConstants.EXTRA_FILE_RESULTS)
            val intent = Intent(this, GalleryActivity::class.java)
            intent.putExtra(EXTRA_IMAGE_URI, uri)
            startActivity(intent)
        } else if (REQUEST_GALLERY == requestCode && resultCode == Activity.RESULT_OK) {
            val uri = data?.getParcelableArrayListExtra<Uri?>(KotConstants.EXTRA_FILE_RESULTS)
            val intent = Intent(this, GalleryActivity::class.java)
            intent.putExtra(EXTRA_IMAGE_URI, uri)
            startActivity(intent)
        } else if (REQUEST_VIDEO == requestCode && resultCode == Activity.RESULT_OK) {
            val uri = data?.getParcelableArrayListExtra<Uri?>(KotConstants.EXTRA_FILE_RESULTS)
        }
    }

    override fun onItemClick(position: Int) {
        //listener for items in adapter
        when (position) {
            0 -> openGallery(false)
            1 -> openGallery(true)
            2 -> openFile()
            3 -> openFile()
            4 -> openCamera()
            5 -> openVideo()
        }
    }
}
