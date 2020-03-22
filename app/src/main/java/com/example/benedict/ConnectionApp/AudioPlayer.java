package com.example.benedict.ConnectionApp;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class AudioPlayer {

    private static MediaPlayer mediaPlayer;

    public static void startMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(AudioRecorder.getFilePath());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                Log.e("startMediaPlayer", "prepare() failed");
            }
        }
    }

    public static void stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
