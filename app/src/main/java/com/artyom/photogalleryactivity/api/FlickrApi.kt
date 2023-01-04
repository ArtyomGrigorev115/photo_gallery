package com.artyom.photogalleryactivity.api

import retrofit2.Call
import retrofit2.http.GET

interface FlickrApi {
   /* @GET("/")
    fun fetchContents(): Call<String>*/

    @GET(
        "services/rest/?method=flickr.interestingness.getList" +
                "&api_key=f0101cbb25a12009b9b0840c0fbd6fe3" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&extras=url_s"
    )
    fun fetchPhotos(): Call<FlickrResponse>
}