package com.example.playground

import android.app.Application
import okhttp3.OkHttpClient
import org.conscrypt.Conscrypt
import java.security.Security

class PlaygroundApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Security.insertProviderAt(Conscrypt.newProvider(), 1)

        val okHttpClient = OkHttpClient()
        okHttpClient
    }
}