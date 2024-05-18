package com.example.myapplication;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TranslateAnimationUtil implements View.OnTouchListener
{
    private GestureDetector gesture_detector;

    public TranslateAnimationUtil(Context context, View view)
    {
        gesture_detector = new GestureDetector(context,new SimpleGestureDetector(view));
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gesture_detector.onTouchEvent(event);
    }

    public class SimpleGestureDetector extends GestureDetector.SimpleOnGestureListener
    {
        private View view_animation;
        private boolean is_finish_animation = true;

        public SimpleGestureDetector(View view_animation)
        {
            this.view_animation = view_animation;
        }

        @Override
        public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY)
        {
            if(distanceY>0)
            {
                hiddenView();
            }
            else
            {
                showView();
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
        private void hiddenView()
        {
            if(view_animation == null || view_animation.getVisibility() == View.GONE)
            {
                return;
            }
            Animation animation_down = AnimationUtils.loadAnimation(view_animation.getContext(),R.anim.bottom_down);
            animation_down.setAnimationListener(new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation)
                {
                    view_animation.setVisibility(View.VISIBLE);
                    is_finish_animation = false;
                }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                    view_animation.setVisibility(View.GONE);
                    is_finish_animation = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            if(is_finish_animation == true)
            {
                view_animation.startAnimation(animation_down);
            }
        }
        private void showView()
        {
            if(view_animation == null || view_animation.getVisibility() == View.VISIBLE)
            {
                return;
            }
            Animation animation_up = AnimationUtils.loadAnimation(view_animation.getContext(),R.anim.bottom_up);
            animation_up.setAnimationListener(new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation)
                {
                    view_animation.setVisibility(View.VISIBLE);
                    is_finish_animation = false;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    is_finish_animation = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            if(is_finish_animation == true)
            {
                view_animation.startAnimation(animation_up);
            }

        }

    }
}
