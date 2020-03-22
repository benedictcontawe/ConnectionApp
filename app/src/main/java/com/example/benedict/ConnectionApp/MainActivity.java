package com.example.benedict.ConnectionApp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    public static final int RECORD_AUDIO = 0;

    private MediaRecorder mediaRecorder;

    private TextView txtDuration;
    private ImageView imgSignal,imgMicrophone, imgPlay, imgStop, imgRefresh;

    private Rect rect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        //TODO: imgSignal - should be equal to the input source of sound in the microphone
        //TODO: imgPlay - should play the recorded audio in the microphone
        //TODO: imgStop - should stop the recorded audio in the microphone
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        txtDuration = (TextView) findViewById(R.id.text_view_duration);
        imgSignal = (ImageView) findViewById(R.id.image_view_signal);
        imgRefresh = (ImageView) findViewById(R.id.image_view_refresh);
        imgPlay = (ImageView) findViewById(R.id.image_view_play);
        imgStop = (ImageView) findViewById(R.id.image_view_stop);
        imgMicrophone = (ImageView) findViewById(R.id.image_view_microphone);

        imgMicrophone.setOnTouchListener(this);
        imgRefresh.setOnTouchListener(this);
        imgPlay.setOnTouchListener(this);
        imgStop.setOnTouchListener(this);

        permissionRecordAudio();
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
                    Log.d("onTouch","ACTION_DOWN imgMicrophone");
                    startRecord();
                } else if (view.getId() == imgRefresh.getId()) {
                    Log.d("onTouch","ACTION_DOWN imgRefresh");
                }
                else if (view.getId() == imgPlay.getId()) {
                    Log.d("onTouch","ACTION_DOWN imgPlay");
                }
                else if (view.getId() == imgStop.getId()) {
                    Log.d("onTouch","ACTION_DOWN imgStop");
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                // Write your code to perform an action on continuous touch move
                if (view.getId() == imgMicrophone.getId()) {
                    Log.d("onTouch","ACTION_MOVE imgMicrophone");
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

                    } else if (view.getId() == imgRefresh.getId()) {
                        Log.d("onTouch","imgRefresh");
                        setRecordVisibility();
                    }
                    else if (view.getId() == imgPlay.getId()) {
                        Log.d("onTouch","imgPlay");
                        playAudioVisibility();
                    }
                    else if (view.getId() == imgStop.getId()) {
                        Log.d("onTouch","imgStop");
                        stopAudioVisibility();
                    }
                    return true;
                }
                return false;
        }
        return false;
    }

    private void permissionRecordAudio() {
        //TODO: ake the code clear
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

    protected boolean hasMicrophone() {
        PackageManager pmanager = this.getPackageManager();
        return pmanager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
    }

    private void startRecord() {
        if (getMicrophoneImage() == isMicrphoneNormal()) {
            startAsyncTask();
            startMediaRecorder();
        }
    }

    private void resetRecord() {
        if (getMicrophoneImage() == isMicrphonePressed()) {
            stopMediaRecorder();
            resetRecordState();
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

    private void startAsyncTask() {
        RecordTimerAsyncTask recordTimerAsyncTask = new RecordTimerAsyncTask(this);
        recordTimerAsyncTask.execute(1);
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

    private void resetRecordState() {
        txtDuration.setText("00:00");
        setTimerDurationColour(false);
        setMicrophoneImage(false);
    }

    private void setRecordVisibility() {
        imgRefresh.setVisibility(View.INVISIBLE);
        imgMicrophone.setVisibility(View.VISIBLE);
        imgPlay.setVisibility(View.INVISIBLE);
        imgStop.setVisibility(View.INVISIBLE);
    }

    public void setPlayVisibility() {
        imgRefresh.setVisibility(View.VISIBLE);
        imgMicrophone.setVisibility(View.INVISIBLE);
        stopAudioVisibility();
    }

    private void stopAudioVisibility() {
        imgPlay.setVisibility(View.VISIBLE);
        imgStop.setVisibility(View.INVISIBLE);
    }

    private void playAudioVisibility() {
        imgPlay.setVisibility(View.INVISIBLE);
        imgStop.setVisibility(View.VISIBLE);
    }

    public void setMicrophoneImage(Boolean isRecording) {
        if (isRecording) {
            imgMicrophone.setImageResource(R.drawable.ic_microphone_pressed);
        } else {
            imgMicrophone.setImageResource(R.drawable.ic_microphone_normal);
        }
    }

    public Drawable.ConstantState getMicrophoneImage() {
        return imgMicrophone.getDrawable().getConstantState();
    }

    public Drawable.ConstantState isMicrphoneNormal() {
        return getResources().getDrawable(
                R.drawable.ic_microphone_normal
        ).getConstantState();
    }

    public Drawable.ConstantState isMicrphonePressed() {
        return getResources().getDrawable(
                R.drawable.ic_microphone_pressed
        ).getConstantState();
    }

    public void setTimerDurationText(String data) {
        txtDuration.setText(data);
    }

    public void setTimerDurationColour(Boolean isRecording) {
        if (isRecording) {
            txtDuration.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.green));
        } else {
            txtDuration.setTextColor(ContextCompat.getColor(this,R.color.black));
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
