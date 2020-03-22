package com.example.benedict.ConnectionApp;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;

public class AudioRecorder {

    private static MediaRecorder mediaRecorder;

    public static void startMediaRecorder() {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); //Use Android Microphone
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); //.3gp
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); //For Low Quality
            //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB); //For High Quality
            mediaRecorder.setOutputFile(getFilePath()); //Output file for the Audio Record
            try {
                mediaRecorder.prepare();
                Log.d("startMediaRecorder", "prepare()");
                mediaRecorder.start();
                Log.d("startMediaRecorder", "start()");
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Log.e("startMediaRecorder", "Ilegal prepare() failed " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("startMediaRecorder", "IO prepare() failed " + e.getMessage());
            }
        }
    }

    public static void stopMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            //mediaRecorder.reset();
            mediaRecorder = null;
        }
    }

    private static String getFilePath() {
        String filepath;
        filepath = Environment.getExternalStorageDirectory().getPath();
        //filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
        filepath += "/MediaRecorderSample.3gp"; //AudioRecording.3gp


        //File file = new File(filepath, "MediaRecorderSample.3gp");
        //if (!file.exists()) file.mkdirs(); //Make a new Folder

        Log.d("startMediaRecorder", "getFilePath() " + filepath);
        return (filepath);
    }

    public static void release() {
        mediaRecorder.release();
    }
}
