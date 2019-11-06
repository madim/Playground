package com.example.playground

import android.media.MediaPlayer
import java.io.File

class SimpleMediaPlayer {

    private val mediaPlayer = MediaPlayer().apply {
        setOnPreparedListener { start() }
        setOnCompletionListener { reset() }
    }

    fun play(file: File) {
        mediaPlayer.run {
            reset()
            setDataSource(file.absolutePath)
            prepareAsync()
        }
    }

    fun release() {
        mediaPlayer.reset()
        mediaPlayer.release()
    }
}