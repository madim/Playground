package com.example.playground.webview

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.playground.R

class MyAdapter(private val root: View) : ListAdapter<WebViewItem, MyViewHolder>(MyDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_webview, parent, false)

        return MyViewHolder(root, itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewRecycled(holder: MyViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }
}

class MyViewHolder(root: View, itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val placeholder: FrameLayout = itemView.findViewById(R.id.placeholder)
    private val webView: WebView = itemView.findViewById(R.id.webview)
    private val scrollBounds = Rect()

    init {
        root.getHitRect(scrollBounds)
        enableWebViewCache()
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                webView.visibility = View.VISIBLE
                placeholder.visibility = View.INVISIBLE
            }
        }

        webView.viewTreeObserver.addOnScrollChangedListener {
            if (adapterPosition == 1 && isViewCompletelyVisible()) {
            }
        }
    }

    private fun isViewCompletelyVisible(): Boolean {
        return webView.getLocalVisibleRect(scrollBounds) && scrollBounds.height() >= webView.height
    }

    fun bind(webViewItem: WebViewItem) {
        webView.visibility = View.INVISIBLE
        placeholder.visibility = View.VISIBLE

        val (url, width, height) = webViewItem
        webView.loadUrl(url)
    }

    fun unbind() {
        webView.loadUrl("about:blank")
    }

    private fun enableWebViewCache() {
        val cachePath = itemView.context.cacheDir.absolutePath
        webView.settings.setAppCachePath(cachePath)
        webView.settings.setAppCacheEnabled(true)
    }
}

private object MyDiffCallback : DiffUtil.ItemCallback<WebViewItem>() {
    override fun areItemsTheSame(oldItem: WebViewItem, newItem: WebViewItem): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: WebViewItem, newItem: WebViewItem): Boolean {
        return oldItem == newItem
    }
}