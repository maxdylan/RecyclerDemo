package com.lt.recyclerdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.lang.reflect.Field;

/**
 * 带边框的textView
 *
 * @author tongchenfei
 */
public class StrokeTextView extends AppCompatTextView {
    public StrokeTextView(Context context) {
        super(context);
    }

    public StrokeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StrokeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = getMeasuredWidth();
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize += 20;
            Layout mLayout = getLayout();
            if (mLayout != null) {
                mLayout.increaseWidthTo(widthSize);
            }
            setMeasuredDimension(widthSize, getMeasuredHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int oriColor = getCurrentTextColor();
        // 先画边框
        TextPaint paint = getPaint();
        setCurTextColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setShadowLayer(10F, 0F, 0F, Color.WHITE);
        float b = getTextSize() / 20;
        float shadowWidth = Math.max(b, 2f);
        paint.setStrokeWidth(shadowWidth);
        super.onDraw(canvas);
        // 再画文字
        setCurTextColor(oriColor);
        paint.setStyle(Paint.Style.FILL);
        super.onDraw(canvas);
    }

    /**
     * 通过反射直接设置mCurTextColor这个变量，直接调用{@link #setTextColor(int)}会出现重复递归的问题
     *
     * @param color 要设置的颜色值
     */
    private void setCurTextColor(int color) {
        try {
            Field mCurTextColor = TextView.class.getDeclaredField("mCurTextColor");
            mCurTextColor.setAccessible(true);
            mCurTextColor.set(this,color);
            mCurTextColor.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
