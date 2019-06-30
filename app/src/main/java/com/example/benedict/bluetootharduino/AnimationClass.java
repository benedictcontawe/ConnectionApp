package com.example.benedict.bluetootharduino;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Benedict on 14/03/2017.
 */

public class AnimationClass {

    void fadeOutAndHideTextView(final TextView txt,int duration){
        android.view.animation.Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(duration);

        fadeOut.setAnimationListener(new android.view.animation.Animation.AnimationListener()
        {
            public void onAnimationEnd(android.view.animation.Animation animation)
            {
                txt.setVisibility(View.INVISIBLE);
            }
            public void onAnimationRepeat(android.view.animation.Animation animation) {}
            public void onAnimationStart(android.view.animation.Animation animation) {}
        });
        txt.startAnimation(fadeOut);
    }

    void fadeOutAndHideImage(final ImageView img,int duration){
        android.view.animation.Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(duration);

        fadeOut.setAnimationListener(new android.view.animation.Animation.AnimationListener()
        {
            public void onAnimationEnd(android.view.animation.Animation animation)
            {
                img.setVisibility(View.INVISIBLE);
            }
            public void onAnimationRepeat(android.view.animation.Animation animation) {}
            public void onAnimationStart(android.view.animation.Animation animation) {}
        });
        img.startAnimation(fadeOut);
    }

    void fadeOutAndHideButton(final Button btn, int duration){
        android.view.animation.Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(duration);

        fadeOut.setAnimationListener(new android.view.animation.Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                btn.setVisibility(View.INVISIBLE);
            }
            public void onAnimationRepeat(android.view.animation.Animation animation) {}
            public void onAnimationStart(android.view.animation.Animation animation) {}
        });
        btn.startAnimation(fadeOut);
    }

    void fadeOutAndHideLinearLayout(final LinearLayout LL,int duration){
        android.view.animation.Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(duration);

        fadeOut.setAnimationListener(new android.view.animation.Animation.AnimationListener()
        {
            public void onAnimationEnd(android.view.animation.Animation animation)
            {
                LL.setVisibility(View.INVISIBLE);
            }
            public void onAnimationRepeat(android.view.animation.Animation animation) {}
            public void onAnimationStart(android.view.animation.Animation animation) {}
        });
        LL.startAnimation(fadeOut);
    }

    void BackgroundColorAnimation(View v,int duration,int colorStart,int colorEnd,int returnColor){
        //int colorStart = v.getSolidColor();
        //int colorEnd = Color.RED;
        if(colorStart == 0)
            colorStart = v.getSolidColor();

        ValueAnimator colorAnim = ObjectAnimator.ofInt(v,"backgroundColor",colorStart,colorEnd);
        colorAnim.setDuration(duration);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(returnColor);//return color if returnColor is equal to 1, 0 if not
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }

    void TextViewBackgroundColorAnimation(TextView v,int duration,int colorStart,int colorEnd,int returnColor){
        //int colorStart = v.getSolidColor();
        //int colorEnd = Color.RED;
        if(colorStart == 0)
            colorStart = v.getSolidColor();

        ValueAnimator colorAnim = ObjectAnimator.ofInt(v,"backgroundColor",colorStart,colorEnd);
        colorAnim.setDuration(duration);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(returnColor);//return color if returnColor is equal to 1, 0 if not
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }

    void TextColorAnimation(TextView textView,int duration,int colorStart,int colorEnd,int returnColor){
        //int colorStart = textView.getCurrentTextColor();
        //int colorEnd = Color.RED;
        if(colorStart == 0)
            colorStart = textView.getCurrentTextColor();

        ValueAnimator colorAnim = ObjectAnimator.ofInt(textView, "textColor", colorStart, colorEnd);
        colorAnim.setDuration(duration);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(returnColor);//return color if returnColor is equal to 1, 0 if not
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }
}
