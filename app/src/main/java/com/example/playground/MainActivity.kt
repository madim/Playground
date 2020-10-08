package com.example.playground

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var number: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val fragment = TempFragment()
            fragment.arguments = bundleOf("number" to 0)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    fun onClick(view: View) {
        addFragment(++number)
    }

    private fun addFragment(number: Int) {
        val fragment = TempFragment()
        fragment.arguments = bundleOf("number" to number)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
