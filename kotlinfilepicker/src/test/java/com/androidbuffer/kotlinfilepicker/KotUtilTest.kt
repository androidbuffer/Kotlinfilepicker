package com.androidbuffer.kotlinfilepicker

import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class KotUtilTest {

    val extension = "pdf"
    val url = "/storage/emulated/0/Download/3c45fcac801adcd03c00391e9dbb119b.${extension}"

    @Test
    fun isExtensionCorrect() {
        val extension = "pdf"
        val url = "/storage/emulated/0/Download/3c45fcac801adcd03c00391e9dbb119b.${extension}"
        val extensionExpected = KotUtil.getFileExtensionFromUrl(url)
        Assert.assertEquals(extension, extensionExpected)
    }

    @Test
    fun isDateFormatCorrect(){
        val currentDate = System.currentTimeMillis()
        val formattedDate = KotUtil.getDateModified(currentDate)
        Assert.assertNotNull(formattedDate)
    }

}