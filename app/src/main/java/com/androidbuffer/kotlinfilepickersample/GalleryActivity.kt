package com.androidbuffer.kotlinfilepickersample

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.androidbuffer.kotlinfilepicker.KotResult


/**
 * Created by AndroidBuffer on 26/1/18.
 */
class GalleryActivity : AppCompatActivity() {

    private val EXTRA_IMAGE_RESULT = "EXTRA_IMAGE_RESULT"
    lateinit var viewPager: ViewPager
    lateinit var listOfImages: ArrayList<KotResult>
    lateinit var adapter: AdapterImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        adapter = AdapterImage(this)
        viewPager = findViewById(R.id.vpImages)
        listOfImages = intent.getParcelableArrayListExtra<KotResult>(EXTRA_IMAGE_RESULT)
        viewPager.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId==R.id.menuClose){
            finish()
        }
        return super.onOptionsItemSelected(item)
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
            imageView.setImageURI(listOfImages[position].uri)
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