package com.desmond.ripple;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

/**
 *
 * Created by Jiayi Yao on 2015/10/28.
 */
public class RippleCompatDrawable extends Drawable implements View.OnTouchListener {

    private enum Speed {PRESSED, NORMAL}

    public enum Type {CIRCLE, HEART}

    private Paint mPaint;
    private Speed mSpeed;
    private Path mRipplePath;
    private Interpolator mInterpolator;
    private ValueAnimator mFadeAnimator;
    private int mRippleColor;
    private int mRippleDuration;
    private int mMaxRippleRadius;
    private int mBackgroundColor;
    private Drawable mBackgroundDrawable;
    private int mFadeDuration;
    private int mAlpha;
    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;
    private int mPaddingTop = 0;
    private int mPaddingBottom = 0;

    private long mStartTime;
    private int x;
    private int y;
    private float mScale = 0f;
    private int lastX;
    private int lastY;
    private float lastScale = 0f;
    private Matrix mMatrix;

    private boolean isFull = false;
    private boolean isWaving  = false;
    private boolean isPressed = false;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            updateRipple(mSpeed);
            if (isWaving || isPressed) {
                mHandler.postDelayed(this, RippleUtil.FRAME_INTERVAL);
            } else {
                startFadeAnimation();
            }
        }
    };

    public RippleCompatDrawable(RippleConfig config){
        this(config.getRippleColor(), config.getBackgroundColor(), config.getMaxRippleRadius(),
                config.getRippleDuration(), config.getInterpolator(), config.getFadeDuration(),
                config.isFull(), config.getPath());
    }

    private RippleCompatDrawable(int rippleColor, int backgroundColor, int maxRippleRadius,
                                 int rippleDuration, Interpolator interpolator, int fadeDuration,
                                 boolean isFull, Path path) {
        mRippleColor = rippleColor;
        mBackgroundColor = backgroundColor;
        mMaxRippleRadius = maxRippleRadius;
        mRippleDuration = rippleDuration;
        mInterpolator = interpolator;
        mFadeDuration = fadeDuration;

        this.isFull = isFull;

        mRipplePath = path;

        mPaint = new Paint();
        mPaint.setColor(mRippleColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mAlpha = (mRippleColor >> 24) & 0x000000ff;
        mMatrix = new Matrix();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.clipRect(mPaddingLeft, mPaddingTop, canvas.getWidth() - mPaddingRight, canvas.getHeight() - mPaddingBottom);
        canvas.drawColor(mBackgroundColor);
        canvas.save();
        canvas.translate(x, y);
        canvas.scale(mScale, mScale);
        canvas.drawPath(mRipplePath, mPaint);
        canvas.restore();
//        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        int background = 0x3f & mBackgroundColor;
        mBackgroundColor = (alpha << 6) | background;
        mPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    private long elapsedOffset = 0;

    private void updateRipple(Speed speed) {
        if(isWaving){
            long elapsed = SystemClock.uptimeMillis() - mStartTime;
            if (speed == Speed.PRESSED) {
                elapsed = elapsed / 5;
                elapsedOffset = elapsed * 4;
            } else {
                elapsed = elapsed - elapsedOffset;
            }
            float progress = Math.min(1f, (float) elapsed / mRippleDuration);
            isWaving = progress <= 0.99f;
            mScale = (mMaxRippleRadius - RippleUtil.MIN_RIPPLE_RADIUS) / RippleUtil.MIN_RIPPLE_RADIUS * mInterpolator.getInterpolation(progress) + 1f;
        } else{
            mScale = (mMaxRippleRadius - RippleUtil.MIN_RIPPLE_RADIUS) / RippleUtil.MIN_RIPPLE_RADIUS + 1f;
        }
        if(lastX == x && lastY == y && lastScale == mScale) return;
        lastX = x;
        lastY = y;
        lastScale = mScale;

        mPaint.setAlpha(mAlpha);
        invalidateSelf();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();
                mSpeed = Speed.PRESSED;

                if (mFadeAnimator != null) mFadeAnimator.end();
                mHandler.removeCallbacks(mRunnable);
                mStartTime = SystemClock.uptimeMillis();
                mHandler.postDelayed(mRunnable, RippleUtil.FRAME_INTERVAL);
                isWaving = true;
                isPressed = true;
                elapsedOffset = 0;
                lastX = x;
                lastY = y;

                break;
            case MotionEvent.ACTION_MOVE:
                x = (int) event.getX();
                y = (int) event.getY();
                mSpeed = Speed.PRESSED;

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mSpeed = Speed.NORMAL;
                isPressed = false;
                if (!isWaving) startFadeAnimation();
                break;
        }
        return true;
    }

    public void startFadeAnimation() {
        mFadeAnimator = ValueAnimator.ofInt(mAlpha, 0);
        mFadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int alpha = (int) animation.getAnimatedValue();
                mPaint.setAlpha(alpha);
                invalidateSelf();
            }
        });
        mFadeAnimator.setDuration(mFadeDuration);
        mFadeAnimator.start();
    }

    public void setMaxRippleRadius(int maxRippleRadius){
        mMaxRippleRadius = maxRippleRadius;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setPadding(float l, float t, float r, float b){
        mPaddingLeft =   RippleUtil.dip2px(l);
        mPaddingRight =  RippleUtil.dip2px(r);
        mPaddingTop =    RippleUtil.dip2px(t);
        mPaddingBottom = RippleUtil.dip2px(b);
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public void setBackgroundDrawable(Drawable backgroundDrawable) {
        mBackgroundDrawable = backgroundDrawable;
    }
}
