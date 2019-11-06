package com.example.playground.downloader

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import com.example.playground.cache.DataCacheWriter
import com.example.playground.cache.DiskCache
import com.example.playground.cache.StreamEncoder
import okhttp3.*
import java.io.IOException

class DefaultFileDownloader(
    private val callback: FileDownloader.Callback,
    private val diskCache: DiskCache
) : FileDownloader {

    private val okHttpClient = OkHttpClient()
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    @SuppressLint("VisibleForTests")
    private val encoder = StreamEncoder()

    override fun enqueue(request: FileDownloader.Request) {
        val (downloadUrl, uniqueKey) = request
        val httpRequest = Request.Builder()
            .url(downloadUrl)
            .build()

        diskCache.get(uniqueKey)?.let {
            callback.onDownloadCompleted(it)

            return
        }

        okHttpClient.newCall(httpRequest).enqueue(object : Callback {

            override fun onFailure(
                call: Call,
                e: IOException
            ) {
                mainThreadHandler.post { callback.onDownloadFailed() }
            }

            override fun onResponse(
                call: Call,
                response: Response
            ) {
                response.use { handleResponse(it, uniqueKey) }
            }
        })
    }

    private fun handleResponse(
        response: Response,
        uniqueKey: String
    ) {
        val responseBody = response.body() ?: return

        val writer = DataCacheWriter(encoder, responseBody.byteStream())
        diskCache.put(uniqueKey, writer)

        val file = diskCache.get(uniqueKey)
        mainThreadHandler.post {
            if (file == null) {
                callback.onDownloadFailed()
            } else {
                callback.onDownloadCompleted(file)
            }
        }
    }
}
