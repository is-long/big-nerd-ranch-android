package com.bignerdranch.android.photogallery.api

import com.bignerdranch.android.photogallery.FlickrResponse
import retrofit2.Call
import retrofit2.http.GET

interface FlickrApi {


//    private val API_KEY: String = "f9ca368a5f717a0866174c37c03a8959"

    @GET("services/rest/?method=flickr.interestingness.getList" +
    "&api_key=f9ca368a5f717a0866174c37c03a8959" +
    "&format=json" +
    "&nojsoncallback=1" +
    "&extras=url_s")
    fun fetchPhotos(): Call<FlickrResponse>
}