package com.example.benedict.ConnectionApp;

import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private TextView txtDuration;
    private ImageView imgSignal,imgMicrophone;
    private Rect rect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        //TODO: imgSignal - should be equal to the input source of sound in the microphone
        //TODO: imgMicrophone - audio recording function
        //xxx.setImageResource(R.drawable.ic_immediate_grey)
    }

    private void init() {
        txtDuration = (TextView) findViewById(R.id.text_view_duration);
        imgSignal = (ImageView) findViewById(R.id.image_view_signal);
        imgMicrophone = (ImageView) findViewById(R.id.image_view_microphone);

        imgMicrophone.setOnTouchListener(this);
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
                    startRecord();
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                // Write your code to perform an action on continuous touch move
                if (view.getId() == imgMicrophone.getId()) {
                    Log.e("onTouch","ACTION_MOVE");
                    startRecord();
                }
                return true;
            case MotionEvent.ACTION_UP:
                // Write your code to perform an action on touch up
                Log.e("onTouch","ACTION_UP");
                resetRecord();
                if (rect.contains(view.getLeft() + (int) x, view.getTop() + (int) y)) {
                    // User moved inside bounds
                    if (view.getId() == imgMicrophone.getId()) {

                    }
                    return true;
                }
                return false;
        }
        return false;
    }

    private void startAsyncTask() {
        TimerAsyncTask timerAsyncTask = new TimerAsyncTask(this);
        timerAsyncTask.execute(10);
    }

    private void startRecord() {
        if (imgMicrophone.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.ic_microphone_normal).getConstantState()) {
            startAsyncTask();
        }
    }

    private void resetRecord() {
        txtDuration.setText("00:00");
        txtDuration.setTextColor(ContextCompat.getColor(this,R.color.black));
        imgMicrophone.setImageResource(R.drawable.ic_microphone_normal);
    }

    private class TimerAsyncTask extends AsyncTask<Integer, Integer, String> {
        int counter;
        private WeakReference<MainActivity> activityWeakReference;

        TimerAsyncTask(MainActivity activity) {
            activityWeakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("TimerAsyncTask","onPreExecute");
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            counter = 0;
            imgMicrophone.setImageResource(R.drawable.ic_microphone_pressed);
            txtDuration.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.green));
        }

        @Override
        protected String doInBackground(Integer... integers) {
            Log.e("TimerAsyncTask","doInBackground");

            while (imgMicrophone.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.ic_microphone_pressed).getConstantState()) {
                publishProgress(counter);
                try {
                    Thread.sleep(1000);
                    counter++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Finished!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.e("TimerAsyncTask","onProgressUpdate");
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            activity.txtDuration.setText(values[0].toString());
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            Log.e("TimerAsyncTask","onPostExecute");
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            Toast.makeText(activity, string, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
