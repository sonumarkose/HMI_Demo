package com.example.quest.hmi_demo.homescreen;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by quest on 23/2/17.
 */
public class AnimationProvider {


    public void beginAnimation(Map<FrameLayout, Integer> framesDetails) {

        Set<Map.Entry<FrameLayout, Integer>> frame = framesDetails.entrySet();
        Iterator<Map.Entry<FrameLayout, Integer>> itr = frame.iterator();
        try {
            while (itr.hasNext()) {
                Map.Entry<FrameLayout, Integer> entry = itr.next();
                final View view = entry.getKey();
                int newHeight = entry.getValue();
                int currentHeight = view.getLayoutParams().height;

                ValueAnimator slideAnimator = ValueAnimator
                        .ofInt(currentHeight, newHeight)
                        .setDuration(1000);

                slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {

                        Integer value = (Integer) animation.getAnimatedValue();

                        view.getLayoutParams().height = value.intValue();
                        view.requestLayout();


                    }
                });

                AnimatorSet set = new AnimatorSet();
                set.play(slideAnimator);
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                set.start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
