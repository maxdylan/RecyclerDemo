package com.lt.recyclerdemo.layoutmanager

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.absoluteValue

/**
 * 堆叠展示的layoutManager
 */
class StackLayoutManager : RecyclerView.LayoutManager() {
    override fun generateDefaultLayoutParams() = RecyclerView.LayoutParams(
        RecyclerView.LayoutParams.WRAP_CONTENT,
        RecyclerView.LayoutParams.WRAP_CONTENT
    )

    private var currentPosition = 0
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        // 第一次加载所有view的场景
        detachAndScrapAttachedViews(recycler)

        var totalWidth = width - paddingRight - paddingLeft
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
            left += getDecoratedMeasuredWidth(v) - 50
            totalWidth -= getDecoratedMeasuredWidth(v) - 50
            currentPosition++
        }
        printMsg(recycler)
    }

    override fun canScrollHorizontally() = true

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        // fill
        val realDx = fillChild(dx, recycler)

        // move
        offsetChildrenHorizontal(-dx)

        // recycle
        recyclerViewOutOfScreen(dx, recycler)

        printMsg(recycler)
        return realDx
    }

    private fun fillChild(dx: Int, recycler: RecyclerView.Recycler): Int {
        var fillPos = 0
        var left = 0
        var right = 0
        // 先考虑特殊情况，优先处理边界，此时只涉及移动，不涉及增加view
        if (dx > 0) {
            // 从右往左
            val anchor = getChildAt(childCount - 1)!!
            val anchorPos = getPosition(anchor)
            val anchorRight = getDecoratedRight(anchor)

            if (anchorPos >= itemCount - 1 && anchorRight - dx.absoluteValue < width) {
                // 最后一个值，且滑动会超过右边框
                return if (anchorRight - width > 0) anchorRight - width else 0
            }

            if (anchorRight - dx.absoluteValue > width) {
                return dx
            }
            fillPos = anchorPos + 1
            left = anchorRight - 50
        }
        if (dx < 0) {
            // 从左往右
            val anchor = getChildAt(0)!!
            val anchorPos = getPosition(anchor)
            val anchorLeft = getDecoratedLeft(anchor)
            if (anchorPos == 0 && anchorLeft + dx.absoluteValue > 0) {
                return if (anchorLeft < 0) anchorLeft else 0
            }
            if (anchorLeft + dx.absoluteValue < 0) {
                return dx
            }
            fillPos = anchorPos - 1
            right = anchorLeft + 50
        }

        // 由于滑动导致需要新增view
        val view = recycler.getViewForPosition(fillPos)
        if (dx > 0) {
            addView(view)
        } else {
            addView(view, 0)
        }
        measureChild(view, 0, 0)

        if (dx > 0) {
            right = left + getDecoratedMeasuredWidth(view)
        } else {
            left = right - getDecoratedMeasuredWidth(view)
        }

        layoutDecorated(view, left, 0, right, getDecoratedMeasuredHeight(view))

        return dx
    }

    private fun recyclerViewOutOfScreen(dx: Int, recycler: RecyclerView.Recycler) {
        val willRecycleView = hashSetOf<View>()
        if (dx > 0) {
            // 从右向左滑动，检查最左边的view是否出了屏幕
            for (i in 0 until childCount) {
                val child = getChildAt(i)!!
                val r = getDecoratedRight(child)
                if (r < 0) {
                    willRecycleView.add(child)
                } else {
                    break;
                }
            }
        } else if (dx < 0) {
            // 从左往右划
            for (i in childCount - 1 downTo 0) {
                val child = getChildAt(i)!!
                val l = getDecoratedLeft(child)
                if (l > width) {
                    willRecycleView.add(child)
                } else {
                    break
                }
            }
        }

        for (v in willRecycleView) {
            removeAndRecycleView(v, recycler)
        }
        willRecycleView.clear()
    }

    private fun printMsg(recycler: RecyclerView.Recycler) {
        Log.d("StackLayoutManager", "$childCount --- ${recycler.scrapList.size}")
    }
}