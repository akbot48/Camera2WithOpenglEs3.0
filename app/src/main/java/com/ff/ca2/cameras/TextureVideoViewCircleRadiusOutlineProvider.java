package com.ff.ca2.cameras;

import android.graphics.Outline;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewOutlineProvider;

public class TextureVideoViewCircleRadiusOutlineProvider extends ViewOutlineProvider {

    int mRadius;

    public TextureVideoViewCircleRadiusOutlineProvider(int radius) {
        mRadius = radius;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        //以下是圆形效果 指定半径
        int left = (int) (view.getWidth() / 2 - mRadius);
        int top = (int) (view.getHeight() / 2 - mRadius);
        int right = (int) (left + 2 * mRadius);
        int bottom = (int) (top + mRadius * 2);
        outline.setOval(left, top, right, bottom);
    }
}
