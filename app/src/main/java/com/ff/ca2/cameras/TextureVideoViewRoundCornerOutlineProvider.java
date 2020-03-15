package com.ff.ca2.cameras;

import android.graphics.Outline;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewOutlineProvider;

public class TextureVideoViewRoundCornerOutlineProvider extends ViewOutlineProvider {

    int mRadius;
    public TextureVideoViewRoundCornerOutlineProvider(int radius) {
        mRadius = radius;
    }

    @Override
    public void getOutline(View view, Outline outline) {

        //以下是圆角效果
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int leftMargin = 0;
        int topMargin = 0;
        Rect selfRect = new Rect(leftMargin, topMargin,
                rect.right - rect.left - leftMargin, rect.bottom - rect.top - topMargin);
        outline.setRoundRect(selfRect, mRadius);
    }
}
