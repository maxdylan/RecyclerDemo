package com.lt.recyclerdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_slide_panel.*

class SlidePanelActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_panel)

        slidePanel.setOnSlidePanelMoveToEndListener {
            Log.d("SlidePanelActivity", "onSlidePanelMoveToEnd")

        }
    }
}