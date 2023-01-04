package com.artyom.photogalleryactivity.api

import com.artyom.photogalleryactivity.GalleryItem
import com.google.gson.annotations.SerializedName

class PhotoResponse {
    @SerializedName("photo")
    lateinit var galleryItems: List<GalleryItem>
}