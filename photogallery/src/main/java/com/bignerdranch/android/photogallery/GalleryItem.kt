package com.bignerdranch.android.photogallery

import com.google.gson.annotations.SerializedName

data class GalleryItem(
    // by default, gson maps json property to variable with same name
    var title: String = "",
    var id: String = "",
    // if different, use annotation
    @SerializedName("url_s") var url: String = ""
)