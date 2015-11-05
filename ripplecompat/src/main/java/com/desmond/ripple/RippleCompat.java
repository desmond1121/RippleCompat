package com.desmond.ripple;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

    public static void apply(View v) {
        apply(v, RippleConfig.getDefaultConfig(), null);
    }

    /**
     * set ripple with ripple color.
     *
     * @param v view to set
     * @param rippleColor ripple color
     */
    public static void apply(View v, int rippleColor) {
        RippleConfig config = new RippleConfig();
        config.setRippleColor(rippleColor);
        apply(v, config, null);
    }

    /**
     * Set ripple with background
     *
     * @param rippleColor ripple color.
     * @param v           view to set.
     * @param drawable    background drawable.
     * @param scaleType   scaleType.
     */
    public static void apply(View v, int rippleColor, Drawable drawable, ImageView.ScaleType scaleType) {
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
     * @param v           view to set.
     * @param resId       resourceId of background.
     * @param scaleType   scaleType, {@link android.widget.ImageView.ScaleType}.
     */
    public static void apply(View v, int rippleColor, int resId, ImageView.ScaleType scaleType) {
        RippleConfig config = new RippleConfig();
        config.setRippleColor(rippleColor);

        if (sContext != null) {
            try {
                config.setBackgroundDrawable(sContext.getResources().getDrawable(resId));
                config.setScaleType(scaleType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "RippleCompat->apply --err log-- not init context!");
        }

        apply(v, config, null);
    }

    public static void apply(View v, RippleConfig config) {
        apply(v, config, null);
    }

    public static void apply(View v, RippleConfig config, RippleCompatDrawable.OnFinishListener onFinishListener) {
        v.setFocusableInTouchMode(true);
        final RippleCompatDrawable drawable = new RippleCompatDrawable(config);
        if (onFinishListener != null) {
            drawable.addOnFinishListener(onFinishListener);
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

    private static void adaptBackground(RippleCompatDrawable rippleDrawable, View v, RippleConfig config) {
        Drawable background;

        if (v instanceof ImageView) {
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
        } else if (config.getBackgroundDrawable() != null) {
            rippleDrawable.setBackgroundDrawable(config.getBackgroundDrawable());
            rippleDrawable.setScaleType(config.getScaleType());
            RippleUtil.setBackground(v, rippleDrawable);
        } else {
            background = v.getBackground();
            if (background != null) {
                LayerDrawable layer = new LayerDrawable(new Drawable[]{background, rippleDrawable});
                RippleUtil.setBackground(v, layer);
            } else {
                RippleUtil.setBackground(v, rippleDrawable);
            }
        }
    }

    /**
     * Set palette mode of the ripple.
     *
     * @param v view
     * @param paletteMode palette mode. {@link com.desmond.ripple.RippleUtil.PaletteMode}
     */
    public static void setPaletteMode(View v, RippleUtil.PaletteMode paletteMode){
        Drawable drawable = v.getBackground();
        if(drawable instanceof RippleCompatDrawable){
            ((RippleCompatDrawable) drawable).setPaletteMode(paletteMode);
        }else if(drawable instanceof LayerDrawable
                && ((LayerDrawable)drawable).getDrawable(1) instanceof RippleCompatDrawable){
            ((RippleCompatDrawable) ((LayerDrawable)drawable).getDrawable(1)).setPaletteMode(paletteMode);
        }
    }

    /**
     * Set scaleType of the ripple drawable background.
     *
     * @param v view
     * @param scaleType ScaleType of an image, {@link android.widget.ImageView.ScaleType}
     */
    public static void setScaleType(View v, ImageView.ScaleType scaleType) {
        Drawable drawable = v.getBackground();
        if(drawable instanceof RippleCompatDrawable){
            ((RippleCompatDrawable) drawable).setScaleType(scaleType);
        }else if(drawable instanceof LayerDrawable
                && ((LayerDrawable)drawable).getDrawable(1) instanceof RippleCompatDrawable){
            ((RippleCompatDrawable) ((LayerDrawable)drawable).getDrawable(1)).setScaleType(scaleType);
        }
        v.invalidate();
    }

    private static void measure(final RippleCompatDrawable drawable, final View v) {
        if (v instanceof Button) {
            fitButton(drawable, v instanceof AppCompatButton);
        } else if (v instanceof EditText) {
            fitEditText(drawable, v instanceof AppCompatEditText);
        }
        v.setFocusableInTouchMode(true);
        v.setOnTouchListener(new ForwardingTouchListener(drawable, v));
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int radius = Math.max(v.getMeasuredWidth(), v.getMeasuredHeight());
                drawable.setMeasure(v.getMeasuredWidth(), v.getMeasuredHeight());
                if (drawable.isFull()) drawable.setMaxRippleRadius(radius);
            }
        });
    }

    private static void fitButton(final RippleCompatDrawable drawable, boolean isAppCompatStyle) {
        drawable.setPadding(RippleUtil.BTN_INSET_HORIZONTAL,
                isAppCompatStyle ? RippleUtil.BTN_INSET_VERTICAL_APPCOMPAT : RippleUtil.BTN_INSET_VERTICAL,
                RippleUtil.BTN_INSET_HORIZONTAL,
                isAppCompatStyle ? RippleUtil.BTN_INSET_VERTICAL_APPCOMPAT : RippleUtil.BTN_INSET_VERTICAL);
    }

    private static void fitEditText(final RippleCompatDrawable drawable, boolean isAppCompatStyle){
        drawable.setPadding(isAppCompatStyle ? RippleUtil.ET_INSET_HORIZONTAL_APPCOMPAT:RippleUtil.ET_INSET,
                isAppCompatStyle ? RippleUtil.ET_INSET_TOP_APPCOMPAT:RippleUtil.ET_INSET,
                isAppCompatStyle ? RippleUtil.ET_INSET_HORIZONTAL_APPCOMPAT:RippleUtil.ET_INSET,
                isAppCompatStyle ? RippleUtil.ET_INSET_BOTTOM_APPCOMPAT:RippleUtil.ET_INSET);
    }

    private static class ForwardingTouchListener implements View.OnTouchListener {
        RippleCompatDrawable drawable;

        private ForwardingTouchListener(RippleCompatDrawable drawable, final View v) {
            this.drawable = drawable;
            drawable.addOnFinishListener(new RippleCompatDrawable.OnFinishListener() {
                @Override
                public void onFinish() {
                    if (v instanceof EditText && imm != null) {
                        v.requestFocus();
                        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                    }
                    v.performClick();
                }
            });
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    return isInBound(event.getX(), event.getY()) && drawable.onTouch(v, event);

                default:
                    return drawable.onTouch(v, event);
            }
        }

        private boolean isInBound(float x, float y) {
            Rect bound;
            if(drawable.getBackgroundDrawable() == null || drawable.getBackgroundDrawable() instanceof ColorDrawable){
                bound = drawable.getClipBound();
            }else{
                bound = drawable.getDrawableBound();
            }
            return x >= bound.left && x <= bound.right && y >= bound.top && y <= bound.bottom;
        }
    }
}
