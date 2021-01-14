package com.lt.recyclerdemo.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

/**
 * 滑块滑动面板
 *
 * @author tongchenfei
 */
public class SlidePanelView extends View {

    public SlidePanelView(Context context) {
        this(context, null);
    }

    public SlidePanelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidePanelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private final Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint blockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private static final int BLOCK_START_X = 5;
    private static final int BLOCK_START_Y = 5;

    private OnSlidePanelMoveToEndListener moveToEndListener;

    private int blockWidth = 0;

    private int blockOffset = 0;

    public void setOnSlidePanelMoveToEndListener(OnSlidePanelMoveToEndListener moveToEndListener) {
        this.moveToEndListener = moveToEndListener;
    }

    private void setBlockOffset(int blockOffset) {
        this.blockOffset = blockOffset;
        invalidate();
    }

    private void init() {
        bgPaint.setColor(Color.RED);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(BLOCK_START_X * 2);

        blockPaint.setColor(Color.GREEN);
        blockPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        blockWidth = w / 5;
    }

    private float lastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (x > BLOCK_START_X + blockOffset && x < blockWidth + BLOCK_START_X + blockOffset && y > BLOCK_START_Y && y < getHeight() - BLOCK_START_Y) {
                    lastX = x;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (lastX != 0) {
                    blockOffset = (int) (x - lastX);
                    if (blockOffset < 0) {
                        blockOffset = 0;
                    }
                    if (blockWidth + BLOCK_START_X + blockOffset > getWidth() - BLOCK_START_X) {
                        // 超出右边界
                        blockOffset = getWidth() - BLOCK_START_X * 2 - blockWidth;
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                lastX = 0;
                if (blockOffset != 0) {
                    // 执行滑块回归动画
                    ObjectAnimator animator = ObjectAnimator.ofInt(this, "blockOffset", blockOffset, 0);
                    animator.setInterpolator(new DecelerateInterpolator());
                    animator.setDuration(1000 * blockOffset / getWidth());
                    animator.start();
                }
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画背景
        canvas.drawRect(0, 0, getWidth(), getHeight(), bgPaint);
        // 画滑块
        canvas.drawRect(BLOCK_START_X, BLOCK_START_Y, blockWidth + BLOCK_START_X + blockOffset, getHeight() - BLOCK_START_Y, blockPaint);
    }

    public interface OnSlidePanelMoveToEndListener{
        /**
         * 滑块滑到了末尾
         */
        void moveToEnd();
    }
}
