package com.example.playground

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class TempFragment : Fragment(R.layout.fragment_temp) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val number = arguments!!.getInt("number")
        val textview: TextView = view.findViewById(R.id.textview)
        textview.text = "$number"
    }
}