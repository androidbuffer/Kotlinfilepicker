package com.androidbuffer.kotlinfilepickersample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.androidbuffer.kotlinfilepicker.KotResult
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView


/**
 * Created by AndroidBuffer on 26/1/18.
 */
class GalleryActivity : AppCompatActivity(), ThumbnailAdapter.OnThumbnailListener {

    private val EXTRA_IMAGE_RESULT = "EXTRA_IMAGE_RESULT"
    lateinit var rvThumbnailsImages: RecyclerView
    lateinit var listOfImages: ArrayList<KotResult>
    lateinit var adapter: ThumbnailAdapter
    lateinit var imageViewFullScreen: ImageView
    lateinit var adViewBottom: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        listOfImages = intent.getParcelableArrayListExtra<KotResult>(EXTRA_IMAGE_RESULT)

        if (listOfImages.size > 1) {
            setupRecyclerView()
        }

        //image view
        imageViewFullScreen = findViewById(R.id.ivFullScreenImage)

        //by default image
        imageViewFullScreen.setImageURI(listOfImages.get(0).uri)

        setupAdView()
    }

    fun setupAdView() {
        //setup the adview
        adViewBottom = findViewById(R.id.adViewBottom)
        val adRequest = AdRequest.Builder().build()
        adViewBottom.loadAd(adRequest)
    }

    private fun setupRecyclerView() {
        //adapter for thumbnail
        adapter = ThumbnailAdapter(listOfImages, this)
        rvThumbnailsImages = findViewById(R.id.rvThumbnailsImages)
        rvThumbnailsImages.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false)
        rvThumbnailsImages.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menuClose) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * on click for image thumbnail
     */
    override fun onThumbnailClick(position: Int) {
        imageViewFullScreen.setImageURI(listOfImages.get(position).uri)
        Log.d("TAG", listOfImages.get(position).uri.toString())
    }
}