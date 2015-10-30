package com.desmond.ripple;

import android.graphics.Path;

public class RipplePathFactory {
    public static final int HEART_DIMEN = RippleUtil.MIN_RIPPLE_RADIUS * 2;
    public static final int HEART_OFFSET_Y = - HEART_DIMEN /2;
    public static Path produceCirclePath(){
        Path path = new Path();
        path.addCircle(0, 0, RippleUtil.MIN_RIPPLE_RADIUS, Path.Direction.CW);
        return path;
    }

    public static Path produceHeartPath(){
        int d = RippleUtil.MIN_RIPPLE_RADIUS * 2;
        Path path = new Path();
        path.moveTo(0, HEART_OFFSET_Y);
        path.cubicTo(-d, HEART_OFFSET_Y - d / 2, -d, HEART_OFFSET_Y + d / 2, 0, HEART_OFFSET_Y + d);
        path.cubicTo(d, HEART_OFFSET_Y + d / 2, d, HEART_OFFSET_Y - d / 2, 0, HEART_OFFSET_Y);
        path.close();
        return path;
    }
}
