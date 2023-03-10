package com.artyom.photogalleryactivity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artyom.photogalleryactivity.api.FlickrApi
import com.artyom.photogalleryactivity.api.FlickrResponse
import com.artyom.photogalleryactivity.api.PhotoInterceptor
import com.artyom.photogalleryactivity.api.PhotoResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "FlickrFetchr"

class FlickrFetchr {
    private val flickrApi: FlickrApi

    init {

        val client = OkHttpClient.Builder()
            .addInterceptor(PhotoInterceptor())
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    fun fetchPhotos(): LiveData<List<GalleryItem>> {
        return fetchPhotoMetadata(flickrApi.fetchPhotos())
    }
    fun searchPhotos(query: String): LiveData<List<GalleryItem>> {
        return fetchPhotoMetadata(flickrApi.searchPhotos(query))
    }

   /* fun fetchContents(): LiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val flickrRequest: Call<String> = flickrApi.fetchContents()

        flickrRequest.enqueue(object : Callback<String> {

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d(TAG, "Response received")
                responseLiveData.value = response.body()
            }
        })
        return responseLiveData
    }*/


    private fun fetchPhotoMetadata(flickrRequest: Call<FlickrResponse>): LiveData<List<GalleryItem>> {

       val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()

       //val flickrRequest: Call<FlickrResponse> = flickrApi.fetchPhotos()

       flickrRequest.enqueue(object : Callback<FlickrResponse> {

           override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
               Log.e(TAG, "Failed to fetch photos", t)
           }

           override fun onResponse(call: Call<FlickrResponse>, response: Response<FlickrResponse>) {
               Log.d(TAG, "Response received")
              // responseLiveData.value = response.body()
               val flickrResponse: FlickrResponse? = response.body()
               val photoResponse: PhotoResponse? = flickrResponse?.photos
               var galleryItems: List<GalleryItem> = photoResponse?.galleryItems ?: mutableListOf()
               galleryItems = galleryItems.filterNot {
                   it.url.isBlank()
               }
               responseLiveData.value = galleryItems
           }
       })
       return responseLiveData
   }

    @WorkerThread
    fun fetchPhoto(url: String): Bitmap? {
        val response: Response<ResponseBody> = flickrApi.fetchUrlBytes(url).execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG, "Decoded bitmap=$bitmap from Response=$response")
        return bitmap
    }
}