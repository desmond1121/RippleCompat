package com.desmond.ripple;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class RippleCompat {
    private static final String TAG = "RippleCompat";
    private static InputMethodManager imm = null;

    public static void init(Context context){
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static void apply(View v){
        apply(v, RippleConfig.getDefaultConfig());
    }

    public static void apply(View v, RippleConfig config){
        RippleCompatDrawable drawable = new RippleCompatDrawable(config);

        Drawable background = v.getBackground();

        if(background != null){
            drawable.setBackgroundColor(Color.TRANSPARENT);
        }else{
            drawable.setBackgroundColor(config.getBackgroundColor());
        }

        applyView(drawable, v);

        if(background != null){
            LayerDrawable layer = new LayerDrawable(new Drawable[]{background, drawable});
            RippleUtil.setBackground(v, layer);
        }else{
            RippleUtil.setBackground(v, drawable);
        }
    }

    public static void applyView(final RippleCompatDrawable drawable, final View v){
        v.setFocusableInTouchMode(true);

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

        v.setOnTouchListener(new ForwardingTouchListener(drawable));
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int radius = Math.max(v.getMeasuredWidth(), v.getMeasuredHeight());
                if(drawable.isFull())drawable.setMaxRippleRadius(radius);
            }
        });
    }

    static class ForwardingTouchListener implements View.OnTouchListener {
        RippleCompatDrawable drawable;

        private ForwardingTouchListener(RippleCompatDrawable drawable){
            this.drawable = drawable;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()){
                case MotionEvent.ACTION_UP:
                    if(v instanceof EditText && imm != null){
                        v.requestFocus();
                        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                    }else{
                        v.performClick();
                    }
                default:
                    return drawable.onTouch(v, event);
            }
        }
    }
}
