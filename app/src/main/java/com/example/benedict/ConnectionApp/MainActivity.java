package com.example.benedict.ConnectionApp;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener ,MediaPlayer.OnCompletionListener{

    private TextView txtDuration;
    private ImageView imgSignal,imgMicrophone, imgPlay, imgStop, imgRefresh;

    private Rect rect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        ManifestPermission.newInstance(this);
        //TODO: imgSignal - should be equal to the input source of sound in the microphone
        //TODO: PlayTimerAsyncTask - When playing the AudioPlayer should see the duration
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
                stopRecord();
                if (rect.contains(view.getLeft() + (int) x, view.getTop() + (int) y)) {
                    // User moved inside bounds
                    if (view.getId() == imgMicrophone.getId()) {
                        Log.d("onTouch","ACTION_UP imgMicrophone");
                        stopRecord();
                    } else if (view.getId() == imgRefresh.getId()) {
                        Log.d("onTouch","ACTION_UP imgRefresh");
                        resetRecord();
                    }
                    else if (view.getId() == imgPlay.getId()) {
                        Log.d("onTouch","ACTION_UP imgPlay");
                        playAudio();
                    }
                    else if (view.getId() == imgStop.getId()) {
                        Log.d("onTouch","ACTION_UP imgStop");
                        stopAudio();
                    }
                    return true;
                }
                return false;
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stopAudio();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ManifestPermission.check();
    }

    private void startRecord() {
        if (getMicrophoneImage() == isMicrphoneNormal()) {
            AudioRecorder.start();
            startTimerAsyncTask();
        }
    }

    private void stopRecord() {
        if (getMicrophoneImage() == isMicrphonePressed()) {
            AudioRecorder.stop();
            AudioRecorder.release();
            resetRecordView();
        }
    }

    private void playAudio() {
        AudioPlayer.start(this);
        playAudioVisibility();
    }

    private void stopAudio() {
        AudioPlayer.stop();
        AudioPlayer.release();
        stopAudioVisibility();
    }

    private void resetRecord() {
        stopAudio();
        setRecordVisibility();
    }

    private void startTimerAsyncTask() {
        RecordTimerAsyncTask.newInstance(this);
        RecordTimerAsyncTask.execute();
    }

    private void resetRecordView() {
        setTimerDurationText("00:00");
        setTimerDurationColour(false);
        setMicrophoneImage(false);
    }

    public void disableRecordView() {
        setTimerDurationText("00:00 - No Microphone");
        imgRefresh.setVisibility(View.INVISIBLE);
        imgMicrophone.setVisibility(View.INVISIBLE);
        imgPlay.setVisibility(View.INVISIBLE);
        imgStop.setVisibility(View.INVISIBLE);
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

    private Boolean isPlayVisible() {
        return imgPlay.getVisibility() == View.VISIBLE;
    }

    private Boolean isStopVisible() {
        return imgStop.getVisibility() == View.VISIBLE;
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

    private Drawable.ConstantState isMicrphoneNormal() {
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
        AudioRecorder.release();
        AudioPlayer.release();
    }
}
