package com.example.benedict.ConnectionApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, MediaPlayer.OnCompletionListener {

    private TextView txtDuration;
    private ImageView imgSignal,imgMicrophone, imgPlay, imgStop, imgRefresh;
    private ManifestPermission manifestPermission;

    private Rect rect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        manifestPermission = ManifestPermission.newInstance(this);
        Log.d("getMicrophoneImage",String.valueOf(getMicrophoneImage() == isMicrphoneNormal()));
        //TODO: imgSignal - should be equal to the input decibel source of sound in the microphone
        //TODO: imgRefresh - when clicked delete the recorded audio MediaRecorderSample.3gp
        //TODO: showAppPermissionSettings() - call this method when the permissions is not granted and checked the never ask again
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
        manifestPermission.check();
    }
    //region Presenter
    private void startRecord() {
        if (getMicrophoneImage() == isMicrphoneNormal()) {
            AudioRecorder.start();
            startRecordTimerAsyncTask();
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
        startPlayTimerAsyncTask();
    }

    private void stopAudio() {
        AudioPlayer.stop();
        AudioPlayer.release();
        stopRecordView();
    }

    private void resetRecord() {
        stopAudio();
        setRecordVisibility();
        resetRecordView();
    }

    private void startRecordTimerAsyncTask() {
        RecordTimerAsyncTask.newInstance(this);
        RecordTimerAsyncTask.execute();
    }

    private void startPlayTimerAsyncTask() {
        PlayTimerAsyncTask.newInstance(this);
        PlayTimerAsyncTask.execute();
    }
    //endregion
    //region View Animation
    private void stopRecordView() {
        setTimerDurationColour(false);
        stopAudioVisibility();
    }

    private void resetRecordView() {
        setTimerDurationText("0");
        setTimerDurationColour(false);
        setMicrophoneImage(false);
    }

    public void enableRecordView() {
        setTimerDurationText("00:00");
        imgRefresh.setVisibility(View.INVISIBLE);
        imgMicrophone.setVisibility(View.VISIBLE);
        imgPlay.setVisibility(View.INVISIBLE);
        imgStop.setVisibility(View.INVISIBLE);
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

    public Boolean isPlayVisible() {
        return imgPlay.getVisibility() == View.VISIBLE;
    }

    public Boolean isStopVisible() {
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
    //endregion
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ManifestPermission.RECORD_AUDIO == requestCode) {
            Log.d("PermissionsResult", "requestCode" + requestCode);
            Log.d("PermissionsResult", "permissions" + permissions);
            Log.d("PermissionsResult", "grantResults" + grantResults);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"Permission granted successfully", Toast.LENGTH_SHORT).show();
                manifestPermission.setRequestGranted();
                enableRecordView();
            }  else {
                disableRecordView();
                //showAppPermissionSettings();
            }
        }
    }

    public void showAppPermissionSettings() {
        Toast.makeText(this,"Hardware Permissions Disabled", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
