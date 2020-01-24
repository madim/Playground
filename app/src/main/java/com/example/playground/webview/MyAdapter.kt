package com.example.playground.webview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.playground.R

class MyAdapter(
    private val onClick: (url: String) -> Unit
) : ListAdapter<WebViewItem, MyViewHolder>(MyDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_webview, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onClick(getItem(holder.adapterPosition).url)
        }
    }

    override fun onViewRecycled(holder: MyViewHolder) {
        super.onViewRecycled(holder)
        holder.itemView.setOnClickListener(null)
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val constraintSet = ConstraintSet()
    private val webView: WebView = itemView.findViewById(R.id.item_webview)
    private val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.item_constraint_layout)

    private val accentColor = ContextCompat.getColor(itemView.context, R.color.colorAccent)

    fun bind(webViewItem: WebViewItem) {
        val (url, width, height) = webViewItem
        webView.loadUrl(url)
        webView.webViewClient = webViewClient
        setAspectRatio(ratio = "$width:$height")
    }

    private fun setAspectRatio(ratio: String) {
        constraintSet.clone(constraintLayout)
        constraintSet.setDimensionRatio(webView.id, ratio)
        constraintSet.applyTo(constraintLayout)
    }

    private val webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            constraintLayout.setBackgroundColor(accentColor)
        }
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