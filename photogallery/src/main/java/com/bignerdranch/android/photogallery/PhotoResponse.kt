package com.bignerdranch.android.photogallery

import com.google.gson.annotations.SerializedName

class PhotoResponse {
    // gson will create list of "photo" prop populated with GalleryItem
    @SerializedName("photo")
    lateinit var galleryItems: List<GalleryItem>
}