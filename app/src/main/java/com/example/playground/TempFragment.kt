package com.example.playground

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

class TempFragment : Fragment(R.layout.fragment_temp) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayout: LinearLayout = view.findViewById(R.id.linear_layout)
        val customView = CustomView(requireContext())
        customView.id = R.id.custom_view
        linearLayout.addView(customView)
    }
}