package com.desmond.ripple;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

public class RippleCompat {
    private static final String TAG = "RippleCompat";
    private static InputMethodManager imm = null;
    private static Context sContext = null;

    public static void init(Context context){
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        sContext = context;
    }

    public static void apply(View v){
        apply(v, RippleConfig.getDefaultConfig(), null);
    }

    /**
     * set ripple with ripple color.
     *
     * @param v
     * @param rippleColor
     */
    public static void apply(View v, int rippleColor){
        RippleConfig config = new RippleConfig();
        config.setRippleColor(rippleColor);
        apply(v, config, null);
    }

    /**
     * Set ripple with background
     *
     * @param rippleColor ripple color.
     * @param v view to set.
     * @param drawable background drawable.
     * @param scaleType scaleType.
     */
    public static void apply(View v, int rippleColor, Drawable drawable, ImageView.ScaleType scaleType){
        RippleConfig config = new RippleConfig();
        config.setRippleColor(rippleColor);
        config.setBackgroundDrawable(drawable);
        config.setScaleType(scaleType);
        apply(v, config, null);
    }

    /**
     * Set ripple with background
     *
     * @param rippleColor ripple color.
     * @param v view to set.
     * @param resId resourceId of background.
     * @param scaleType scaleType.
     */
    public static void apply(View v, int rippleColor, int resId, ImageView.ScaleType scaleType){
        RippleConfig config = new RippleConfig();
        config.setRippleColor(rippleColor);

        if(sContext != null){
            try{
                config.setBackgroundDrawable(sContext.getResources().getDrawable(resId));
                config.setScaleType(scaleType);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            Log.e(TAG, "RippleCompat->apply --err log-- not init context!");
        }

        apply(v, config, null);
    }

    public static void apply(View v, RippleConfig config){
        apply(v, config, null);
    }

    public static void apply(View v, RippleConfig config, RippleCompatDrawable.OnFinishListener onFinishListener){
        v.setFocusableInTouchMode(true);
        final RippleCompatDrawable drawable = new RippleCompatDrawable(config);
        if(onFinishListener != null){
            drawable.setOnFinishListener(onFinishListener);
        }
        v.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                drawable.finishRipple();
            }
        });
        measure(drawable, v);
        adaptBackground(drawable, v, config);
    }

    /**
     * Set ripple background. If set, the "android:background" of the view and background color of ripple
     * would be ignored!
     *
     * @param rippleDrawable
     * @param v
     * @param config
     */
    private static void adaptBackground(RippleCompatDrawable rippleDrawable, View v, RippleConfig config) {
        Drawable background;
        if(v instanceof ImageView){
            ImageView.ScaleType scaleType = ((ImageView) v).getScaleType();
            background = ((ImageView) v).getDrawable();
            rippleDrawable.setBackgroundDrawable(background);
            rippleDrawable.setScaleType(scaleType);
            rippleDrawable.setPadding(
                    v.getPaddingLeft(),
                    v.getPaddingTop(),
                    v.getPaddingRight(),
                    v.getPaddingBottom());
            ((ImageView) v).setImageDrawable(null);
            RippleUtil.setBackground(v, rippleDrawable);
        }else if(config.getBackgroundDrawable() != null) {
            rippleDrawable.setBackgroundDrawable(config.getBackgroundDrawable());
            rippleDrawable.setScaleType(config.getScaleType());
            RippleUtil.setBackground(v, rippleDrawable);
        }else {
            background = v.getBackground();
            if (background != null) {
                rippleDrawable.setBackgroundColor(Color.TRANSPARENT);
                LayerDrawable layer = new LayerDrawable(new Drawable[]{background, rippleDrawable});
                RippleUtil.setBackground(v, layer);
            } else {
                rippleDrawable.setBackgroundColor(config.getBackgroundColor());
                RippleUtil.setBackground(v, rippleDrawable);
            }
        }
    }

    public static void setScaleType(ImageView iv, ImageView.ScaleType scaleType){
        if(iv.getBackground() instanceof RippleCompatDrawable){
            RippleCompatDrawable drawable = (RippleCompatDrawable) iv.getBackground();
            drawable.setScaleType(scaleType);
            drawable.invalidateSelf();
        }
    }

    private static void measure(final RippleCompatDrawable drawable, final View v){
        if(v instanceof AppCompatButton){
            drawable.setPadding(RippleUtil.MATERIAL_BTN_INSET_HORIZONTAL,
                    RippleUtil.MATERIAL_BTN_INSET_VERTICAL,
                    RippleUtil.MATERIAL_BTN_INSET_HORIZONTAL,
                    RippleUtil.MATERIAL_BTN_INSET_VERTICAL);
        }else if(v instanceof AppCompatEditText){
            drawable.setPadding(
                    RippleUtil.MATERIAL_ET_INSET_HORIZONTAL,
                    RippleUtil.MATERIAL_ET_INSET_TOP,
                    RippleUtil.MATERIAL_ET_INSET_HORIZONTAL,
                    RippleUtil.MATERIAL_ET_INSET_BOTTOM + 1.5f);
        }
        v.setFocusableInTouchMode(true);
        v.setOnTouchListener(new ForwardingTouchListener(drawable));
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int radius = Math.max(v.getMeasuredWidth(), v.getMeasuredHeight());
                drawable.setMeasure(v.getMeasuredWidth(), v.getMeasuredHeight());
                if (drawable.isFull()) drawable.setMaxRippleRadius(radius);
            }
        });
    }

    private static class ForwardingTouchListener implements View.OnTouchListener {
        RippleCompatDrawable drawable;

        private ForwardingTouchListener(RippleCompatDrawable drawable){
            this.drawable = drawable;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(isInBound(event.getX(), event.getY())){
                        if (v instanceof EditText && imm != null) {
                            v.requestFocus();
                            imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                        }
                        return drawable.onTouch(v, event);
                    }else {
                        return false;
                    }

                case MotionEvent.ACTION_UP:
                    v.performClick();

                default:
                    return drawable.onTouch(v, event);
            }
        }

        private boolean isInBound(float x, float y){
            Rect rect = drawable.getBackgroundDrawable() == null ? drawable.getClipBound() : drawable.getDrawableBound();
            return x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom;
        }
    }
}
