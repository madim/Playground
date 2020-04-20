package com.example.imageloader

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.GlideModule
import okhttp3.OkHttpClient
import java.io.InputStream

class AppGlideModule : GlideModule {
    override fun applyOptions(context: Context?, builder: GlideBuilder?) {

    }

    override fun registerComponents(context: Context?, glide: Glide?) {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(SSLHandshakeInterceptor())
            .build()

        glide?.register(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(okHttpClient))
    }
}