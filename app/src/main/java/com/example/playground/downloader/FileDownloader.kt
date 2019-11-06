package com.example.playground.downloader

import java.io.File


interface FileDownloader {

    fun enqueue(request: Request)

    data class Request(
        val url: String,
        val uniqueKey: String
    )

    interface Callback {

        fun onDownloadCompleted(file: File)

        fun onDownloadFailed()
    }
}