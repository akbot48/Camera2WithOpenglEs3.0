package com.ff.ca2.cameras;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

public class TextureVideoViewCircleOutlineProvider extends ViewOutlineProvider {

    public TextureVideoViewCircleOutlineProvider() {
    }

    @Override
    public void getOutline(View view, Outline outline) {


        //以下是圆形效果 内切圆
        int centerX = view.getWidth()/2;
        int centerY = view.getHeight()/2;
        int radius = Math.min(view.getWidth(),view.getHeight())/2;
        int left = centerX-radius;
        int top = centerY-radius;
        int right = centerX+radius;
        int bottom = centerY+radius;
        outline.setOval(left, top, right, bottom);
    }
}
