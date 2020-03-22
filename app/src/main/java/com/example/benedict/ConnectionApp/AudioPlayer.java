package com.example.benedict.ConnectionApp;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class AudioPlayer {

    private static MediaPlayer mediaPlayer;
    //MediaPlayer.OnCompletionListener listener
    public static void start() {
        Log.d("AudioPlayer","start()");
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            try {
                //mediaPlayer.setOnCompletionListener(listener);
                mediaPlayer.setDataSource(AudioRecorder.getFilePath());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                Log.e("AudioPlayer", "IO prepare() failed " + e.getMessage());
            }
        }
    }

    public static void pause() {
        Log.d("AudioPlayer","pause()");
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public static void stop() {
        Log.d("AudioPlayer","pause()");
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public static void release() {
        Log.d("AudioPlayer","release()");
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
