package com.example.benedict.ConnectionApp;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    TextView txtDuration;
    ImageView imgSignal,imgMicrophone;
    private Rect rect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        txtDuration = (TextView) findViewById(R.id.text_view_duration);
        imgSignal = (ImageView) findViewById(R.id.image_view_signal);
        imgMicrophone = (ImageView) findViewById(R.id.image_view_microphone);

        imgMicrophone.setOnTouchListener(this);

        //TODO: imgSignal - should be equal to the input source of sound in the microphone
        //xxx.setTextColor(ContextCompat.getColor(this,R.color.grey))
        //xxx.setImageResource(R.drawable.ic_immediate_grey)
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Write your code to perform an action on down
                rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                if (view.getId() == imgMicrophone.getId()) {
                    Log.e("onTouch","ACTION_DOWN");
                    imgMicrophone.setImageResource(R.drawable.ic_microphone_pressed);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                // Write your code to perform an action on continuous touch move
                if (view.getId() == imgMicrophone.getId()) {
                    Log.e("onTouch","ACTION_MOVE");
                    imgMicrophone.setImageResource(R.drawable.ic_microphone_pressed);
                }
                return true;
            case MotionEvent.ACTION_UP:
                // Write your code to perform an action on touch up
                if (rect.contains(view.getLeft() + (int) x, view.getTop() + (int) y)) {
                    // User moved inside bounds
                    if (view.getId() == imgMicrophone.getId()) {
                        Log.e("onTouch","ACTION_UP");
                        imgMicrophone.setImageResource(R.drawable.ic_microphone_normal);
                    }
                    return true;
                }
                return false;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
