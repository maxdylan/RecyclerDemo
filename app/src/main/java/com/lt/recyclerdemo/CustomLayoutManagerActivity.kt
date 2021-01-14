package com.lt.recyclerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lt.recyclerdemo.adapter.SimpleImageAdapter
import com.lt.recyclerdemo.layoutmanager.StackLayoutManager
import kotlinx.android.synthetic.main.activity_custom_layout_manager.*

class CustomLayoutManagerActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_layout_manager)
        rvStack.layoutManager = StackLayoutManager()
        rvStack.adapter = SimpleImageAdapter(this)
    }
}