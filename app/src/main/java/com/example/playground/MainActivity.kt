package com.example.playground

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playground.webview.MyAdapter
import com.example.playground.webview.WebViewActivity
import com.example.playground.webview.WebViewItem

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list: RecyclerView = findViewById(R.id.list)
        val myAdapter = MyAdapter { url ->
            openWebView(url)
        }
        list.adapter = myAdapter

        myAdapter.submitList(list())
    }

    private fun openWebView(url: String) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("url", url)

        startActivity(intent)
    }

    fun onClick(view: View) {
        try {
            runShellCommand("setprop debug.firebase.analytics.app kz.kolesa.dev")
        } catch (e: Exception) {
            Log.d(TAG, e.message, e)
        }
    }

    @Throws(Exception::class)
    private fun runShellCommand(command: String) {
        val process = Runtime.getRuntime().exec(command)
        process.waitFor()
    }

    private fun list(): List<WebViewItem> {
        return listOf(
            WebViewItem("https://www.google.com/", 16f, 9f),
            WebViewItem("https://github.com/madim/Playground", 16f, 9f),
            WebViewItem("https://github.com/open-toast/protokt", 16f, 9f),
            WebViewItem("https://github.com/google/truth", 16f, 9f),
            WebViewItem("https://github.com/atonamy/store4-issue", 16f, 9f),
            WebViewItem("https://github.com/felipecsl/ktnes", 16f, 9f),
            WebViewItem("https://github.com/budparr/gohugo-theme-ananke", 16f, 9f),
            WebViewItem("https://stackoverflow.com/", 1f, 1f),
            WebViewItem("https://github.com/", 2f, 1f),
            WebViewItem("https://www.wikipedia.org/", 3f, 1f),
            WebViewItem("https://en.wikipedia.org/wiki/Pacific_swift", 4f, 6f),
            WebViewItem("https://en.wikipedia.org/wiki/Bird_migration", 4f, 6f),
            WebViewItem("https://en.wikipedia.org/wiki/Flyway", 4f, 6f),
            WebViewItem("https://en.wikipedia.org/wiki/Ocean", 4f, 6f),
            WebViewItem("https://en.wikipedia.org/wiki/World_Ocean", 4f, 6f),
            WebViewItem("https://en.wikipedia.org/wiki/Continent", 4f, 6f),
            WebViewItem("https://en.wikipedia.org/wiki/Australia_(continent)", 4f, 6f),
            WebViewItem("https://www.instagram.com/", 1.85f, 1f)
        )
    }
}
