package com.desmond.ripple;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.Button;

/**
 * Created by Jiayi Yao on 2015/10/28.
 */
public class RippleUtil {
    public static final int FRAME_INTERVAL = 1000/60;
    public static final int DEFAULT_INT = -1;

    public static final int MAX_RIPPLE_RADIUS = dip2px(200);
    public static final int MIN_RIPPLE_RADIUS = dip2px(30);
    public static final int RIPPLE_DURATION = 300;
    public static final int FADE_DURATION = 300;

    public static final int MATERIAL_ET_INSET_TOP = 10;
    public static final int MATERIAL_ET_INSET_BOTTOM = 7;
    public static final int MATERIAL_ET_INSET_HORIZONTAL = 4;

    public static final int MATERIAL_BTN_INSET_VERTICAL = 6;
    public static final int MATERIAL_BTN_INSET_HORIZONTAL = 4;

    public static int px2dip(int pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return  (int)(dipValue * scale + 0.5f);
    }

    public static void setBackground(View v, Drawable drawable){
        if(Build.VERSION.SDK_INT>=16){
            v.setBackground(drawable);
        }else{
            v.setBackgroundDrawable(drawable);
        }
    }

    public static void setBackground(Button btn, Drawable drawable){
        if(Build.VERSION.SDK_INT>=16){
            btn.setBackground(drawable);
        }else{
            btn.setBackgroundDrawable(drawable);
        }
    }
}
