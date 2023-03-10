package com.artyom.photogalleryactivity.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface FlickrApi {
   /* @GET("/")
    fun fetchContents(): Call<String>*/

   /* @GET(
        "services/rest/?method=flickr.interestingness.getList" +
                "&api_key=f0101cbb25a12009b9b0840c0fbd6fe3" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&extras=url_s"
    )*/
   @GET("services/rest?method=flickr.interestingness.getList")
    fun fetchPhotos(): Call<FlickrResponse>

    @GET
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody>

    @GET("services/rest?method=flickr.photos.search")
    fun searchPhotos(@Query("text") query: String): Call<FlickrResponse>


}