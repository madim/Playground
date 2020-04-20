package com.example.playground

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

class CustomView(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(
    context,
    attrs
), View.OnClickListener {

    private val expandListText: TextView
    private var listIsExpanded: Boolean = false

    init {
        isSaveEnabled = true
        LayoutInflater.from(context).inflate(
            R.layout.layout_custom_view,
            this,
            true
        )

        expandListText = rootView.findViewById(R.id.textView)
        expandListText.setOnClickListener(this)
        updateButtonState()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val listState = ListState(super.onSaveInstanceState())
        listState.listIsExpanded = listIsExpanded

        return listState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val listState = state as? ListState
        super.onRestoreInstanceState(listState?.superState)

        listIsExpanded = listState?.listIsExpanded ?: false
        updateButtonState()
    }

    override fun onClick(v: View?) {
        if (v?.id == expandListText.id) {
            listIsExpanded = !listIsExpanded
            updateButtonState()
        }
    }

    private fun updateButtonState() {
        val text = if (listIsExpanded) "Скрыть" else "Показать все"
        expandListText.text = text
    }
}