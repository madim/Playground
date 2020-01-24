package com.example.playground

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

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
    private val webView: WebView = itemView.findViewById(R.id.webview)
    private val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraint_layout)

    fun bind(webViewItem: WebViewItem) {
        webView.loadUrl(webViewItem.url)
        setAspectRatio(webViewItem.ratio)
    }

    private fun setAspectRatio(ratio: String) {
        constraintSet.clone(constraintLayout)
        constraintSet.setDimensionRatio(webView.id, ratio)
        constraintSet.applyTo(constraintLayout)
    }
}

private object MyDiffCallback : DiffUtil.ItemCallback<WebViewItem>() {
    override fun areItemsTheSame(oldItem: WebViewItem, newItem: WebViewItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: WebViewItem, newItem: WebViewItem): Boolean {
        return oldItem == newItem
    }
}