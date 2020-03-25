package com.example.benedict.ConnectionApp;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class AudioPlayer extends BaseAudio {

    private static MediaPlayer mediaPlayer;
    //MediaPlayer.OnCompletionListener listener;

    public static void start(MediaPlayer.OnCompletionListener listener) {
        Log.d("AudioPlayer","start()");
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(listener);
            try {
                //mediaPlayer.setOnCompletionListener(listener);
                mediaPlayer.setDataSource(getFilePath()); //Source file for the Audio Player
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("AudioPlayer", "IO prepare() failed " + e.getMessage());
            }
        } else {
            release();
            start(listener);
        }
    }

    public static Boolean hasInstance() {
        if (mediaPlayer != null) {
            return true;
        } else {
            return false;
        }
    }

    public static int getCurrentDuration() {
        return mediaPlayer.getCurrentPosition()/1000;
    }

    public static int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration()/1000;
        } else {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(getFilePath()); //Source file for the Audio Player
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("AudioPlayer", "IO prepare() failed " + e.getMessage());
            }
            return mediaPlayer.getDuration()/1000;
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
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}