package com.desmond.ripple;

import android.graphics.Path;

public class RipplePathFactory {

    public static Path produceCirclePath(){
        Path path = new Path();
        path.addCircle(0, 0, RippleUtil.MIN_RIPPLE_RADIUS, Path.Direction.CW);
        return path;
    }

    public static Path produceHeartPath(){
        int d = RippleUtil.MIN_RIPPLE_RADIUS * 2;
        int offset = (int)(-d /2f);
        Path path = new Path();
        path.moveTo(0, offset);
        path.cubicTo(-d, offset - d / 2, -d, offset + d / 2, 0, offset + d);
        path.cubicTo(d, offset + d / 2, d, offset - d / 2, 0, offset);
        path.close();
        return path;
    }

    public static Path produceTrianglePath(){
        int d = RippleUtil.MIN_RIPPLE_RADIUS * 2;
        int offset = (int)(-d * 2 / 3f);
        int edge = (int) ( d * 2 / Math.sqrt(3));
        Path path = new Path();
        path.moveTo(0, offset);
        path.lineTo(-edge / 2f, -offset / 2f);
        path.lineTo(edge / 2f, -offset / 2f);
        path.close();
        return path;
    }
}
