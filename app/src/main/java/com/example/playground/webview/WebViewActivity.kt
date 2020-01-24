package com.example.playground.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.playground.R

class WebViewActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val webView: WebView = findViewById(R.id.item_webview)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        val url = requireNotNull(intent.getStringExtra("url"))
        webView.loadUrl(url)
    }
}