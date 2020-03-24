package com.example.benedict.ConnectionApp;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

public class AudioRecorder extends BaseAudio {

    private static MediaRecorder mediaRecorder;

    public static void start() {
        Log.d("AudioRecorder","startMediaRecorder()");
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); //Use Android Microphone
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); //.3gp

            //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); //For Low Quality
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB); //For High Quality

            //mediaRecorder.setOutputFile("/dev/null"); //Without saving the file
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

    public static int getAmplitude() {
        //Call this only after the setAudioSource()
        return mediaRecorder.getMaxAmplitude();
    }

    public static void stop() {
        Log.d("AudioRecorder","stopMediaRecorder()");
        if (mediaRecorder != null) {
            mediaRecorder.stop();
        }
    }

    public static void release() {
        Log.d("AudioRecorder","release()");
        if (mediaRecorder != null) {
            //mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
