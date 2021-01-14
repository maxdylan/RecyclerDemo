package com.lt.recyclerdemo.layoutmanager

import android.util.Log
import androidx.recyclerview.widget.RecyclerView

/**
 * 堆叠展示的layoutManager
 */
class StackLayoutManager : RecyclerView.LayoutManager() {
    override fun generateDefaultLayoutParams() = RecyclerView.LayoutParams(
        RecyclerView.LayoutParams.WRAP_CONTENT,
        RecyclerView.LayoutParams.WRAP_CONTENT
    )

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        // 第一次加载所有view的场景
        detachAndScrapAttachedViews(recycler)

        var totalWidth = width - paddingRight - paddingLeft
        var currentPosition = 0
        var bottom = 0
        var left = 0
        var right = 0
        while (currentPosition < itemCount && totalWidth > 0) {
            val v = recycler.getViewForPosition(currentPosition)
            addView(v)
            measureChildWithMargins(v, 0, 0)
            right = left + getDecoratedMeasuredWidth(v)
            bottom = getDecoratedMeasuredHeight(v)
            layoutDecoratedWithMargins(v, left, 0, right, bottom)
            left += getDecoratedMeasuredWidth(v)
            totalWidth -= getDecoratedMeasuredWidth(v)
            currentPosition++
        }
        printMsg(recycler)
    }

    private fun printMsg(recycler: RecyclerView.Recycler){
        Log.d("StackLayoutManager","$childCount --- ${recycler.scrapList.size}")
    }
}