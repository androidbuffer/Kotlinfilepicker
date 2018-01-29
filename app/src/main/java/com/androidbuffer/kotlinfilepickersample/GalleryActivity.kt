package com.androidbuffer.kotlinfilepickersample

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout


/**
 * Created by AndroidBuffer on 26/1/18.
 */
class GalleryActivity : AppCompatActivity() {

    val EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI"
    lateinit var viewPager: ViewPager
    lateinit var listOfImages: ArrayList<Uri>
    lateinit var adapter: AdapterImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        adapter = AdapterImage(this)
        viewPager = findViewById(R.id.vpImages)
        listOfImages = intent.getParcelableArrayListExtra<Uri>(EXTRA_IMAGE_URI)
        viewPager.adapter = adapter
    }

    inner class AdapterImage(appContext: Context) : PagerAdapter() {

        val inflater = LayoutInflater.from(appContext)

        override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
            return view === `object`
        }

        override fun getCount(): Int {
            return listOfImages.size
        }

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            val view = inflater.inflate(R.layout.item_row_gallery, container, false)
            val imageView = view.findViewById<ImageView>(R.id.ivFullScreen)
            imageView.setImageURI(listOfImages[position])
            container?.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            if (`object` is RelativeLayout) {
                container?.removeView(`object`)
            } else {
                container?.removeView(`object` as LinearLayout)
            }
        }
    }
}