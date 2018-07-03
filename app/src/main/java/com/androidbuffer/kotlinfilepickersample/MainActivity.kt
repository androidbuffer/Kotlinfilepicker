package com.androidbuffer.kotlinfilepickersample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.androidbuffer.kotlinfilepicker.KotConstants
import com.androidbuffer.kotlinfilepicker.KotRequest
import com.androidbuffer.kotlinfilepicker.KotResult
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity(), PickerAdapter.OnClickItemListener {

    private val EXTRA_IMAGE_RESULT = "EXTRA_IMAGE_RESULT"
    lateinit var rvFilePickerMain: RecyclerView
    private val REQUEST_CAMERA = 101
    private val REQUEST_GALLERY = 102
    private val REQUEST_FILE = 103
    private val REQUEST_VIDEO = 104
    private var adapter: PickerAdapter? = null
    lateinit var titleArray: Array<String>
    lateinit var adBottom: AdView

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
        rvFilePickerMain = findViewById(R.id.rvFilePickerMain)
        adBottom = findViewById(R.id.adViewBottom)
        titleArray = resources.getStringArray(R.array.arrayOptions)
        setRecyclerView()
        setupAdView()
    }

    private fun setupAdView() {
        //here setup the adview
        MobileAds.initialize(this, BuildConfig.AD_MOB_APP_ID)
        val adRequest = AdRequest.Builder().addTestDevice("D50CED5C7D63D1D9B4DE1A5251B16354").build()
        adBottom.loadAd(adRequest)
    }

    private fun setRecyclerView() {
        //here set the recycler view
        adapter = PickerAdapter(titleArray, drawableArray, this)
        rvFilePickerMain.layoutManager = LinearLayoutManager(this)
        rvFilePickerMain.adapter = adapter
    }

    private fun openCamera() {
        //opens camera from camera class
        KotRequest.Camera(this, REQUEST_CAMERA).pick()
    }

    private fun openVideo() {
        //opens a camera intent
        KotRequest.Video(this, REQUEST_VIDEO).pick()
    }

    private fun openGallery(isMultiple: Boolean) {
        //opens a gallery intent
        KotRequest.Gallery(this, REQUEST_GALLERY).isMultiple(isMultiple).pick()
    }

    private fun openFile(isMultiple: Boolean) {
        //opens a file intent
        KotRequest.File(this, REQUEST_FILE).isMultiple(isMultiple).setMimeType(KotConstants.FILE_TYPE_FILE_ALL).pick()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (REQUEST_CAMERA == requestCode && resultCode == Activity.RESULT_OK) {

            val result = data?.getParcelableArrayListExtra<KotResult>(KotConstants.EXTRA_FILE_RESULTS)
            startGalleryView(result!!)

        } else if (REQUEST_FILE == requestCode && resultCode == Activity.RESULT_OK) {

            val result = data?.getParcelableArrayListExtra<KotResult>(KotConstants.EXTRA_FILE_RESULTS)
            createDetailsFromResult(result!!.get(0))

        } else if (REQUEST_GALLERY == requestCode && resultCode == Activity.RESULT_OK) {

            val result = data?.getParcelableArrayListExtra<KotResult>(KotConstants.EXTRA_FILE_RESULTS)
            startGalleryView(result!!)

        } else if (REQUEST_VIDEO == requestCode && resultCode == Activity.RESULT_OK) {

            val result = data?.getParcelableArrayListExtra<KotResult>(KotConstants.EXTRA_FILE_RESULTS)
            createDetailsFromResult(result!!.get(0))
        }
    }

    fun startGalleryView(kotResultList: ArrayList<KotResult>) {
        val intent = Intent(this, GalleryActivity::class.java)
        intent.putExtra(EXTRA_IMAGE_RESULT, kotResultList)
        startActivity(intent)
    }

    fun createDetailsFromResult(kotResult: KotResult) {
        //this function creates the details dialog from the result
        val detailsDialog = DetailsDialog.getInstance(kotResult)
        detailsDialog.show(fragmentManager, "DetailsDialog")
    }

    override fun onItemClick(position: Int) {
        //listener for items in adapter
        when (position) {
            0 -> openGallery(false)
            1 -> openGallery(true)
            2 -> openFile(false)
            3 -> openFile(true)
            4 -> openCamera()
            5 -> openVideo()
        }
    }
}
