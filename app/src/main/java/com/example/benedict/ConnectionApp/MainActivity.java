package com.example.benedict.ConnectionApp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    public static final int RECORD_AUDIO = 0;

    private MediaRecorder mediaRecorder;

    private TextView txtDuration;
    private ImageView imgSignal,imgMicrophone;

    private Rect rect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        //TODO: imgSignal - should be equal to the input source of sound in the microphone
    }

    private void init() {
        txtDuration = (TextView) findViewById(R.id.text_view_duration);
        imgSignal = (ImageView) findViewById(R.id.image_view_signal);
        imgMicrophone = (ImageView) findViewById(R.id.image_view_microphone);

        imgMicrophone.setOnTouchListener(this);

        premitRecordAudio();
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
                    Log.d("onTouch","ACTION_DOWN");
                    startRecord();
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                // Write your code to perform an action on continuous touch move
                if (view.getId() == imgMicrophone.getId()) {
                    Log.d("onTouch","ACTION_MOVE");
                    startRecord();
                }
                return true;
            case MotionEvent.ACTION_UP:
                // Write your code to perform an action on touch up
                Log.d("onTouch","ACTION_UP");
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

    private void premitRecordAudio() {
        if (
            ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                    new String[] {
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, RECORD_AUDIO
            );
        }

        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            ActivityCompat.requestPermissions(this,
                    new String[] {
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, RECORD_AUDIO
            );
        }
    }

    private void startAsyncTask() {
        TimerAsyncTask timerAsyncTask = new TimerAsyncTask(this);
        timerAsyncTask.execute(10);
    }

    private void startMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(getFilePath());
        try {
            mediaRecorder.prepare(); Log.d("startMediaRecorder", "prepare()");
            mediaRecorder.start(); Log.d("startMediaRecorder", "start()");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e("startMediaRecorder", "Ilegal prepare() failed " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("startMediaRecorder", "IO prepare() failed " + e.getMessage());
        }
    }

    private void stopMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            //mediaRecorder.reset();
            mediaRecorder = null;
        }
    }

    private String getFilePath() {
        String filepath;
        filepath = Environment.getExternalStorageDirectory().getPath();
        //filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
        filepath += "/MediaRecorderSample.3gp"; //AudioRecording.3gp


        //File file = new File(filepath, "MediaRecorderSample.3gp");
        //if (!file.exists()) file.mkdirs(); //Make a new Folder

        Log.d("startMediaRecorder", "getFilePath() " + filepath);
        return (filepath);
    }

    private void startRecord() {
        if (imgMicrophone.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.ic_microphone_normal).getConstantState()) {
            startAsyncTask();
            startMediaRecorder();
        }
    }

    private void resetRecord() {
        stopMediaRecorder();
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
            Log.d("TimerAsyncTask","onPreExecute() Initialization");
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
            Log.d("TimerAsyncTask","doInBackground() background thread task");

            while (imgMicrophone.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.ic_microphone_pressed).getConstantState()) {
                publishProgress(counter);
                try {
                    Thread.sleep(1000);
                    counter++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e("TimerAsyncTask","Error " + e.getMessage());
                }
            }
            return "Finished!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("TimerAsyncTask","onProgressUpdate for Main UI Thread");
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            activity.txtDuration.setText(values[0].toString());
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            Log.d("TimerAsyncTask","onPostExecute() after doInBackground()");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaRecorder.release();
    }
}
