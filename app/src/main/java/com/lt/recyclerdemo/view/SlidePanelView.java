package com.lt.recyclerdemo.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import com.lt.recyclerdemo.R;


/**
 * 滑块滑动面板
 *
 * @author tongchenfei
 */
public class SlidePanelView extends View {
    private static final String TAG = "SlidePanelView";

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
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private static final int BLOCK_START_X = 10;
    private static final int BLOCK_START_Y = 10;
    private static final int TEXT_MARGIN_LEFT = 20;
    private static final int TEXT_MARGIN_RIGHT = 30;

    private OnSlidePanelMoveToEndListener moveToEndListener;

    private int blockWidth = 0;
    private int blockOffset = 0;

    private float lastX;
    private boolean isToEnd = false;

    private static final String STRING_SLIDE_TO_RIGHT = "向右滑动";
    private RectF bgRectF;
    private Bitmap bmBlock;

    private final Matrix gradientMatrix = new Matrix();
    private float matrixTranslate;
    private final Rect textRect = new Rect();
    private LinearGradient textGradient;

    private ObjectAnimator matrixAnim;

    private String blockText = STRING_SLIDE_TO_RIGHT;
    private Paint.FontMetrics blockTextMetrics = new Paint.FontMetrics();

    public void setOnSlidePanelMoveToEndListener(OnSlidePanelMoveToEndListener moveToEndListener) {
        this.moveToEndListener = moveToEndListener;
    }

    private void setBlockOffset(int blockOffset) {
        this.blockOffset = blockOffset;
        invalidate();
    }

    private void setMatrixTranslate(float matrixTranslate) {
        this.matrixTranslate = matrixTranslate;
        invalidate();
    }

    public void setText(String text) {
        this.blockText = text;
        requestLayout();
        invalidate();
    }

    private void init() {
        bgPaint.setColor(Color.RED);
        bgPaint.setStyle(Paint.Style.FILL);

        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textGradient = new LinearGradient(-80, 0, 0, 0, new int[]{0xffffffff, 0xff000000, 0xffffffff}, new float[]{0, 0.5f, 1f}, Shader.TileMode.CLAMP);
        textGradient.setLocalMatrix(gradientMatrix);
        textPaint.setShader(textGradient);
        textPaint.getFontMetrics(blockTextMetrics);

        bmBlock = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        blockWidth = bmBlock.getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Log.d(TAG, "onMeasure:\r\n widthMode: " + widthMode + "  widthSize: " + widthSize + " \r\n heightMode: " + heightMode + " heightSize: " + heightSize);

        Log.d(TAG, "textMetrics: ascent: " + blockTextMetrics.ascent + " descent: " + blockTextMetrics.descent + " top: " + blockTextMetrics.top + " bottom: " + blockTextMetrics.bottom);
        if (widthMode == MeasureSpec.AT_MOST) {
            // 宽度根据图片大小，字符串长度，各种间隔确定
            // 高度根据图片大小和上下间隔确定
            textPaint.getTextBounds(blockText, 0, blockText.length(), textRect);
            widthSize = BLOCK_START_X * 2 + bmBlock.getWidth() + TEXT_MARGIN_LEFT + TEXT_MARGIN_RIGHT + textRect.width();
            heightSize = BLOCK_START_Y * 2 + bmBlock.getHeight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private float textOffset = 0;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bgRectF = new RectF(0, 0, w, h);

        if (matrixAnim != null) {
            matrixAnim.cancel();
        }
        textOffset = (getHeight() - blockTextMetrics.ascent - blockTextMetrics.descent) / 2;
        matrixAnim = ObjectAnimator.ofFloat(this, "matrixTranslate", 0, w + 80).setDuration(1000);
        matrixAnim.setRepeatCount(ValueAnimator.INFINITE);
        matrixAnim.start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (matrixAnim != null) {
            matrixAnim.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (matrixAnim != null) {
            matrixAnim.cancel();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (x > BLOCK_START_X + blockOffset && x < blockWidth + BLOCK_START_X + blockOffset && y > BLOCK_START_Y && y < getHeight() - BLOCK_START_Y) {
                    isToEnd = false;
                    lastX = x;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (lastX != 0) {
                    blockOffset = (int) (x - lastX);
                    if (blockOffset < 0) {
                        blockOffset = 0;
                    }
                    if (blockOffset + BLOCK_START_X + blockWidth > getWidth()) {
                        // 超出右边界
                        blockOffset = getWidth() - BLOCK_START_X - blockWidth;
                        if (!isToEnd) {
                            isToEnd = true;
                            if (moveToEndListener != null) {
                                moveToEndListener.moveToEnd();
                            }
                            startBlockBackAnim();
                        }
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                // 执行滑块回归动画
                if (!isToEnd) {
                    startBlockBackAnim();
                }
                break;
            default:
                break;
        }

        return true;
    }

    private void startBlockBackAnim() {
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "blockOffset", blockOffset, 0);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(1000 * blockOffset / getWidth());
        animator.start();
        lastX = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画背景
        canvas.drawRoundRect(bgRectF, (float) getHeight() / 2, (float) getHeight() / 2, bgPaint);
        // 画文字
        gradientMatrix.setTranslate(matrixTranslate, 0);
        textGradient.setLocalMatrix(gradientMatrix);
        canvas.save();
        canvas.drawText(blockText, blockWidth + BLOCK_START_X + TEXT_MARGIN_LEFT, textOffset, textPaint);
        canvas.restore();
        // 画滑块
        canvas.drawBitmap(bmBlock, BLOCK_START_X + blockOffset, BLOCK_START_Y, blockPaint);
    }

    public interface OnSlidePanelMoveToEndListener {
        /**
         * 滑块滑到了末尾
         */
        void moveToEnd();
    }
}
