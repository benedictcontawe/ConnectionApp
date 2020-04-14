package com.example.benedict.ConnectionApp;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

public class AudioDecibel extends BaseAudio {

    private static MediaRecorder mediaRecorder;
    static final private double EMA_FILTER = 0.6;

    private static void start() {
        Log.d("AudioDecibel","start()");
        mediaRecorder = setMediaRecorderInstance();
        mediaRecorder.setOutputFile(getDevNullPath()); //Without saving the file
        try {
            mediaRecorder.prepare();
            Log.d("AudioRecorder", "prepare()");
            mediaRecorder.start();
            Log.d("AudioRecorder", "start()");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e("AudioDecibel", "Ilegal prepare() failed " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("AudioDecibel", "IO prepare() failed " + e.getMessage());
        }
    }

    private static Boolean hasInstance() {
        if (mediaRecorder != null) {
            return true;
        } else {
            return false;
        }
    }

    public static double getAmplitude() {
        //Call this only after the setAudioSource()
        if (hasInstance())
            //return  mediaRecorder.getMaxAmplitude();
            return 20 * Math.log10(mediaRecorder.getMaxAmplitude() / 2700.0);
        else {
            start();
            return mediaRecorder.getMaxAmplitude();
        }

    }

    public double getAmplitudeEMA() {
        double mEMA = 0.0;
        double amp = getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }

    private static void stop() {
        Log.d("AudioRecorder","stopMediaRecorder()");
        if (hasInstance()) {
            mediaRecorder.stop();
        }
    }

    private static void release() {
        Log.d("AudioRecorder","release()");
        if (hasInstance()) {
            //mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
