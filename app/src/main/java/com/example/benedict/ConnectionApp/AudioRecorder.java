package com.example.benedict.ConnectionApp;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;

public class AudioRecorder {

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

    public static String getFilePath() {
        Log.d("AudioRecorder","getFilePath()");
        String filepath;
        filepath = Environment.getExternalStorageDirectory().getPath();
        //filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
        filepath += "/MediaRecorderSample.3gp"; //AudioRecording.3gp

        //File file = new File(filepath, "MediaRecorderSample.3gp");
        //if (!file.exists()) file.mkdirs(); //Make a new Folder

        Log.d("AudioRecorder", "getFilePath() " + filepath);
        return (filepath);
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
