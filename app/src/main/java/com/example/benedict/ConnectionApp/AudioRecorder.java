package com.example.benedict.ConnectionApp;

import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

public class AudioRecorder extends BaseAudio {

    private static MediaRecorder mediaRecorder;

    public static void start() {
        Log.d("AudioRecorder","start()");
        if (!hasInstance()) {
            mediaRecorder = setMediaRecorderInstance();
            mediaRecorder.setOutputFile(getFilePath()); //Output file for the Audio Record
            try {
                mediaRecorder.prepare();
                Log.d("AudioRecorder", "prepare()");
                mediaRecorder.start();
                Log.d("AudioRecorder", "start()");
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Log.e("AudioRecorder", "Ilegal prepare() failed " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("AudioRecorder", "IO prepare() failed " + e.getMessage());
            }
        }
    }

    public static Boolean hasInstance() {
        if (mediaRecorder != null) {
            return true;
        } else {
            return false;
        }
    }

    public static int getAmplitude() {
        //Call this only after the setAudioSource()
        return mediaRecorder.getMaxAmplitude();
    }

    public static void pause() {
        Log.d("AudioRecorder","pause()");
        if (hasInstance()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaRecorder.pause();
            }
        }
    }

    public static void stop() {
        Log.d("AudioRecorder","stopMediaRecorder()");
        if (hasInstance()) {
            mediaRecorder.stop();
        }
    }

    public static void release() {
        Log.d("AudioRecorder","release()");
        if (hasInstance()) {
            //mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
