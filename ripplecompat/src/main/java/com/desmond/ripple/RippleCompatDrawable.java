package com.desmond.ripple;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;

/**
 *
 * Created by Jiayi Yao on 2015/10/28.
 */
public class RippleCompatDrawable extends Drawable implements View.OnTouchListener {
    private static final String TAG = "RippleCompatDrawable";
    private enum Speed {PRESSED, NORMAL}

    public enum Type {CIRCLE, HEART, TRIANGLE}

    public interface OnFinishListener {
        void onFinish();
    }

    /* ClipBound for widget inset padding.*/
    private Rect mClipBound;
    /* Drawable bound for background image. */
    private Rect mDrawableBound;
    private OnFinishListener mOnFinishListener;
    private Paint mRipplePaint;
    private Speed mSpeed;
    private Path mRipplePath;
    private Interpolator mInterpolator;
    private ValueAnimator mFadeAnimator;
    private Drawable mBackgroundDrawable;
    private ImageView.ScaleType mScaleType = ImageView.ScaleType.FIT_CENTER;
    private int mWidth = 0;
    private int mHeight = 0;
    private int mRippleColor;
    private int mRippleDuration;
    private int mMaxRippleRadius;
    private int mBackgroundColor;
    private int mFadeDuration;
    private int mAlpha;
    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;
    private int mPaddingTop = 0;
    private int mPaddingBottom = 0;
    private float mDegree;

    private long mStartTime;
    private int x;
    private int y;
    private float mScale = 0f;
    private int lastX;
    private int lastY;
    private float lastScale = 0f;

    private boolean isFull = false;
    private boolean isSpin = false;
    private boolean isWaving  = false;
    private boolean isPressed = false;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            updateRipple(mSpeed);
            if (isWaving || isPressed) {
                mHandler.postDelayed(this, RippleUtil.FRAME_INTERVAL);
            } else if(!mFadeAnimator.isStarted()){
                startFadeAnimation();
            }
        }
    };

    public RippleCompatDrawable(RippleConfig config){
        this(config.getRippleColor(), config.getBackgroundColor(), config.getMaxRippleRadius(),
                config.getRippleDuration(), config.getInterpolator(), config.getFadeDuration(),
                config.isFull(), config.getPath(), config.isSpin());
    }

    private RippleCompatDrawable(int rippleColor, int backgroundColor, int maxRippleRadius,
                                 int rippleDuration, Interpolator interpolator, int fadeDuration,
                                 boolean isFull, Path path, boolean isSpin) {
        mRippleColor = rippleColor;
        mBackgroundColor = backgroundColor;
        mMaxRippleRadius = maxRippleRadius;
        mRippleDuration = rippleDuration;
        mInterpolator = interpolator;
        mFadeDuration = fadeDuration;

        this.isFull = isFull;
        this.isSpin = isSpin;

        mRipplePath = path;

        mRipplePaint = new Paint();
        mRipplePaint.setAntiAlias(true);
        mAlpha = Color.alpha(mRippleColor);
    }

    @Override
    public void draw(Canvas canvas) {

        if(mBackgroundDrawable == null){
            canvas.drawColor(mBackgroundColor);
            canvas.clipRect(mClipBound);
        }else{
            if(mDrawableBound == null) {
                mDrawableBound = RippleUtil.getBound(mScaleType, new Rect(0, 0, mWidth, mHeight),
                        mBackgroundDrawable.getIntrinsicWidth(), mBackgroundDrawable.getIntrinsicHeight());
            }
            mBackgroundDrawable.setBounds(mDrawableBound);
            canvas.clipRect(mDrawableBound);
            mBackgroundDrawable.draw(canvas);
        }

        canvas.save();
        canvas.translate(x, y);
        canvas.scale(mScale, mScale);
        if(isSpin) canvas.rotate(mDegree);
        canvas.drawPath(mRipplePath, mRipplePaint);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
        int background = 0x3f & mBackgroundColor;
        mBackgroundColor = (alpha << 6) | background;
        mRipplePaint.setAlpha(alpha);
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
        float progress = 0f;
        if(isWaving) {
            long elapsed = SystemClock.uptimeMillis() - mStartTime;
            if (speed == Speed.PRESSED) {
                elapsed = elapsed / 5;
                elapsedOffset = elapsed * 4;
            } else {
                elapsed = elapsed - elapsedOffset;
            }
            progress = Math.min(1f, (float) elapsed / mRippleDuration);
            isWaving = progress <= 0.99f;
            mScale = (mMaxRippleRadius - RippleUtil.MIN_RIPPLE_RADIUS) / RippleUtil.MIN_RIPPLE_RADIUS * mInterpolator.getInterpolation(progress) + 1f;
        } else {
            mScale = (mMaxRippleRadius - RippleUtil.MIN_RIPPLE_RADIUS) / RippleUtil.MIN_RIPPLE_RADIUS + 1f;
        }
        if(lastX == x && lastY == y && lastScale == mScale) return;
        lastX = x;
        lastY = y;
        lastScale = mScale;

        mRipplePaint.setColor(mRippleColor);
        mRipplePaint.setStyle(Paint.Style.FILL);

        mDegree = progress * 480f;
        invalidateSelf();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();

                mSpeed = Speed.PRESSED;

                if (mFadeAnimator != null){
                    mFadeAnimator.end();
                }

                mHandler.removeCallbacks(mRunnable);
                mStartTime = SystemClock.uptimeMillis();
                mHandler.postDelayed(mRunnable, RippleUtil.FRAME_INTERVAL);
                isWaving = true;
                isPressed = true;
                elapsedOffset = 0;
                lastX = x;
                lastY = y;
                mDegree = 0;
                mAlpha = Color.alpha(mRippleColor);

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
                startFadeAnimation();
                break;
        }
        return true;
    }

    public void finishRipple(){
        mHandler.removeCallbacks(mRunnable);
        mFadeAnimator.removeAllUpdateListeners();
        mFadeAnimator.removeAllListeners();
        mFadeAnimator = null;
    }

    private void startFadeAnimation() {
        if(mFadeAnimator == null){
            mFadeAnimator = ValueAnimator.ofInt(mAlpha, 0);
            mFadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int alpha = (int) animation.getAnimatedValue();
                    mRipplePaint.setAlpha(alpha);
                    invalidateSelf();
                }
            });

            mFadeAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mRipplePaint.setAlpha(0);
                    if(mOnFinishListener != null) mOnFinishListener.onFinish();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mFadeAnimator.setDuration(mFadeDuration);
        }else{
            mFadeAnimator.end();
        }
        mFadeAnimator.start();
    }

    protected void setPadding(float l, float t, float r, float b){
        mPaddingLeft =   RippleUtil.dip2px(l);
        mPaddingRight =  RippleUtil.dip2px(r);
        mPaddingTop =    RippleUtil.dip2px(t);
        mPaddingBottom = RippleUtil.dip2px(b);
    }

    protected void setMeasure(int width, int height){
        mWidth = width;
        mHeight = height;
        setClipBound();
    }

    public void setMaxRippleRadius(int maxRippleRadius){
        mMaxRippleRadius = maxRippleRadius;
    }

    public boolean isFull() {
        return isFull;
    }

    protected void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public void setBackgroundDrawable(Drawable backgroundDrawable) {
        mBackgroundDrawable = backgroundDrawable;
        mDrawableBound = null;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        mScaleType = scaleType;
        mDrawableBound = null;
    }

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        mOnFinishListener = onFinishListener;
    }

    private void setClipBound(){
        if(mClipBound == null){
            mClipBound = new Rect(mPaddingLeft, mPaddingTop, mWidth - mPaddingRight, mHeight - mPaddingBottom);
        }else{
            mClipBound.set(mPaddingLeft, mPaddingTop, mWidth - mPaddingRight, mHeight - mPaddingBottom);
        }
    }

    public Rect getDrawableBound() {
        return mDrawableBound;
    }

    public Rect getClipBound() {
        return mClipBound;
    }

    public Drawable getBackgroundDrawable() {
        return mBackgroundDrawable;
    }
}
