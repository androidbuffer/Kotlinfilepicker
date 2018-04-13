package com.androidbuffer.kotlinfilepicker

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by AndroidBuffer on 13/4/18.
 */
data class KotResult(val uri: Uri
                     , val name: String?
                     , val size: String?
                     , val location: String?
                     , val type: String?
                     , val modified: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Uri::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(uri, flags)
        parcel.writeString(name)
        parcel.writeString(size)
        parcel.writeString(location)
        parcel.writeString(type)
        parcel.writeString(modified)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<KotResult> {
        override fun createFromParcel(parcel: Parcel): KotResult {
            return KotResult(parcel)
        }

        override fun newArray(size: Int): Array<KotResult?> {
            return arrayOfNulls(size)
        }
    }
}