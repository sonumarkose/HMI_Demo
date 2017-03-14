package com.example.quest.hmi_demo.homescreen;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ResizeHeightAnimation extends Animation {
    Map<FrameLayout, Integer> frames = null;
    private int mWidth;
    private int mStartWidth;
    private View mView;

    public ResizeHeightAnimation(Map<FrameLayout, Integer> frames) {
        this.frames = frames;

/*        mView = view;
        mWidth = width;
        mStartWidth = view.getHeight();*/
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        Set<Map.Entry<FrameLayout, Integer>> frs = frames.entrySet();
        Iterator<Map.Entry<FrameLayout, Integer>> it = frs.iterator();
        while (it.hasNext()) {
            int newHeight = 0;
            Map.Entry<FrameLayout, Integer> entry = it.next();
            View view = entry.getKey();
            int height = entry.getValue();
            int initialHeight = view.getHeight();
            if (height > initialHeight) {
                newHeight = initialHeight + (int) ((height - initialHeight) * interpolatedTime);
            } else {
                newHeight = height - (int) ((initialHeight - height) * interpolatedTime);
            }

            view.getLayoutParams().height = newHeight;
            LinearLayout.LayoutParams Params = (LinearLayout.LayoutParams) view.getLayoutParams();
            Params.setMargins(0, 0, 0, 0);
            view.requestLayout();
        }

/*        int newWidth = mStartWidth + (int) ((mWidth - mStartWidth) * interpolatedTime);

        mView.getLayoutParams().height = newWidth;
        mView.requestLayout();*/
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}