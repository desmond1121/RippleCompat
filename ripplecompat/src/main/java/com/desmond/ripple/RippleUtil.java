package com.desmond.ripple;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Jiayi Yao on 2015/10/28.
 */
public class RippleUtil {
    private static final String TAG = "RippleUtil";
    public static final int FRAME_INTERVAL = 1000/60;

    public static final int MAX_RIPPLE_RADIUS = dip2px(200);
    public static final int MIN_RIPPLE_RADIUS = dip2px(30);
    public static final int RIPPLE_DURATION = 400;
    public static final int RIPPLE_COLOR = 0xa000ff00;
    public static final int RIPPLE_BACKGROUND_OFFSET = 56;

    public static final int ET_INSET_TOP_APPCOMPAT = 10;
    public static final int ET_INSET_BOTTOM_APPCOMPAT = 6;
    public static final int ET_INSET_HORIZONTAL_APPCOMPAT = 4;
    public static final int ET_INSET = 4;

    public static final int BTN_INSET_HORIZONTAL = 4;
    public static final int BTN_INSET_VERTICAL = 5;
    public static final int BTN_INSET_VERTICAL_APPCOMPAT = 6;

    public static final int ANCHOR_START = 1;
    public static final int ANCHOR_END = -1;

    public enum PaletteMode{
        DISABLED,
        VIBRANT,
        VIBRANT_LIGHT,
        VIBRANT_DARK,
        MUTED,
        MUTED_LIGHT,
        MUTED_DARK
    }

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

    public static int alphaColor(int color, int alpha){
        return (alpha << 24) | 0xffffff & color;
    }

    public static int produceBackgroundColor(int rippleColor){
        int a = (rippleColor & 0xff000000) >> 24;
        int r = (rippleColor & 0xff0000) >> 16;
        int g = (rippleColor & 0xff00) >> 8;
        int b = rippleColor & 0xff;

        r = r < 128 ? r + RIPPLE_BACKGROUND_OFFSET : r - RIPPLE_BACKGROUND_OFFSET;
        g = g < 128 ? g + RIPPLE_BACKGROUND_OFFSET : g - RIPPLE_BACKGROUND_OFFSET;
        b = b < 128 ? b + RIPPLE_BACKGROUND_OFFSET : b - RIPPLE_BACKGROUND_OFFSET;
        return Color.argb(a, r, g, b);
    }

    /**
     * set ripple color with palette of image.
     *
     * @param compatDrawable ripple drawable for using
     * @param background image for palette
     * @param mode palette mode.
     */
    public static void palette(final RippleCompatDrawable compatDrawable, final Drawable background,
                               final PaletteMode mode){
        if(mode == PaletteMode.DISABLED || background.getIntrinsicWidth() <= 0
                || background.getIntrinsicHeight() <= 0)return;
        Palette.Builder builder = Palette.from(drawable2Bitmap(background));
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int color = getPaletteColor(palette, mode);
                compatDrawable.setRippleColor(alphaColor(color, 128));
            }
        });
    }

    public static int getPaletteColor(Palette palette, PaletteMode mode){
        switch (mode){
            case VIBRANT:
                return palette.getVibrantColor(RIPPLE_COLOR);
            case VIBRANT_LIGHT:
                return palette.getLightVibrantColor(RIPPLE_COLOR);
            case VIBRANT_DARK:
                return palette.getDarkVibrantColor(RIPPLE_COLOR);
            case MUTED:
                return palette.getMutedColor(RIPPLE_COLOR);
            case MUTED_LIGHT:
                return palette.getLightMutedColor(RIPPLE_COLOR);
            case MUTED_DARK:
                return palette.getDarkVibrantColor(RIPPLE_COLOR);
            case DISABLED:
            default:
                return 0;
        }
    }

    public static Bitmap drawable2Bitmap(final Drawable drawable){
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Get image bound after scaleType.
     *
     * @param scaleType
     * @param bound
     * @param w
     * @param h
     * @return Bound
     */
    public static Rect getBound(ImageView.ScaleType scaleType, Rect bound, int w, int h){
        int l = bound.left;
        int t = bound.top;
        int r = bound.right;
        int b = bound.bottom;

        if(bound.width() == w && bound.height() == h){
            return new Rect(l, t, r, b);
        }

        float scale;
        switch (scaleType){
            case CENTER:
                return center(bound, w, h);

            case CENTER_CROP:

                if(compareScale(bound, w, h) >= 0){
                    scale = (float)bound.width() / w;
                }else{
                    scale = (float)bound.height() / h;
                }
                return center(bound, w, h, scale);

            case CENTER_INSIDE:
                if(bound.width() >= w && bound.height() >= h){
                    return center(bound, w, h);
                }

                if(compareScale(bound, w, h) >= 0){
                    scale = (float)bound.height() / h;
                }else{
                    scale = (float)bound.width() / w;
                }
                return center(bound, w, h, scale);

            case FIT_END:
                if(compareScale(bound, w, h) >= 0){
                    scale = (float)bound.height() / h;
                }else{
                    scale = (float)bound.width() / w;
                }
                return center(bound, w, h, scale, ANCHOR_END);

            case FIT_START:
                if(compareScale(bound, w, h) >= 0){
                    scale = (float)bound.height() / h;
                }else{
                    scale = (float)bound.width() / w;
                }
                return center(bound, w, h, scale, ANCHOR_START);

            case FIT_CENTER:
                if(compareScale(bound, w, h) >= 0){
                    scale = (float)bound.height() / h;
                }else{
                    scale = (float)bound.width() / w;
                }
                return center(bound, w, h, scale);

            case MATRIX:
                r = l + w;
                b = t + h;
                break;

            case FIT_XY:
            default:
                break;
        }

        return new Rect(l, t, r, b);
    }

    public static int compareScale(Rect rect, int w, int h){
        return Float.compare(rect.width() / (float) rect.height(),
                w / (float) h);
    }

    public static Rect center(Rect rect, int w, int h){
        return center(rect, w, h, 1f);
    }

    public static Rect center(Rect rect, int w, int h, float scale){
        return center(rect, w, h, scale, 0);
    }

    public static Rect center(Rect rect, int w, int h, float scale, int anchor){
        w = (int)(w * scale);
        h = (int)(h * scale);

        int left = rect.left + (rect.width() - w) / 2;
        int top = rect.top + (rect.height() - h) / 2;
        int right = left + w;
        int bottom = top + h;

        int offset = 0;
        if(anchor == ANCHOR_START){
            offset = rect.left - left;
        }else if(anchor == ANCHOR_END){
            offset = rect.width() - w - left;
        }

        return new Rect(left+offset, top, right + offset, bottom);
    }
}
