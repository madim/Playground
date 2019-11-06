package com.example.playground

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.playground.cache.DefaultDiskCache
import com.example.playground.downloader.DefaultFileDownloader
import com.example.playground.downloader.FileDownloader
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.io.File

private const val DOWNLOAD_URL = "https://dl.espressif.com/dl/audio/ff-16b-2c-44100hz.mp4"
private const val MAX_CACHE_SIZE: Long = 4 * 1024 * 1024

class MainActivity : AppCompatActivity(), FileDownloader.Callback {

    private val scope = MainScope()
    private val simpleMediaPlayer = SimpleMediaPlayer()

    private lateinit var downloader: FileDownloader

    private lateinit var downloadButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val diskCache = DefaultDiskCache.create(File(cacheDir, "temp_cache"), MAX_CACHE_SIZE)
        downloader = DefaultFileDownloader(this, diskCache)


        downloadButton = findViewById(R.id.downloadButton)
        downloadButton.setOnClickListener {
            downloadFile()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleMediaPlayer.release()
        scope.cancel()
    }

    override fun onDownloadCompleted(file: File) {
        simpleMediaPlayer.play(file)
    }

    override fun onDownloadFailed() {
        Toast.makeText(this, "Download failed", Toast.LENGTH_LONG).show()
    }

    private fun downloadFile() {
        val request = FileDownloader.Request(DOWNLOAD_URL, "ff-16b-2c-44100hz")

        downloader.enqueue(request)
    }
}
