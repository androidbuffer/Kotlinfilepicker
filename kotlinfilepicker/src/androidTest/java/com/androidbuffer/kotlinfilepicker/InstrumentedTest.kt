package com.androidbuffer.kotlinfilepicker

import android.content.Context
import android.net.Uri
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTest {

    val extension = "pdf"
    val url = "/storage/emulated/0/Download/3c45fcac801adcd03c00391e9dbb119b.${extension}"
    lateinit var appContext: Context

    @Before
    fun inItValues() {
        appContext = InstrumentationRegistry.getTargetContext()
    }

    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        assertEquals("com.androidbuffer.kotlinfilepicker.test", appContext.packageName)
    }

    @Test
    fun isMimeCorrect() {
        val correct = "application/pdf"
        val result = KotUtil.getMimeType(url)
        assertEquals(correct, result)
    }

    @Test
    fun fileIntentNotNull() {
        val mimeType = "image/*"
        val intent = KotUtil.getFileIntent(mimeType, true)
        assertNotNull(intent)
    }

    @Test
    fun galleryIntentNotNull() {
        val mimeType = "application/pdf"
        val intent = KotUtil.getGalleryIntent(mimeType, true)
        assertNotNull(intent)
    }

    @Test
    fun videoIntentNotNull(){
        assertNotNull(KotUtil.getVideoIntent(appContext))
    }

    @Test
    fun cameraIntentNotNull(){
        assertNotNull(KotUtil.getCameraIntent(appContext))
    }
}
