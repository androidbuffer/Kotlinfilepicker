package com.androidbuffer.kotlinfilepickersample

import android.app.DialogFragment
import com.androidbuffer.kotlinfilepicker.KotResult

/**
 * Created by AndroidBuffer on 13/4/18.
 */
class DetailsDialog:DialogFragment(){

    companion object {
        fun getInstance(kotResult: KotResult){
            val fragment= DialogFragment()
        }
    }

}