package com.example.benedict.ConnectionApp;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

abstract class BaseAudio {

    public static MediaRecorder setMediaRecorderInstance() {
        MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); //Use Android Microphone
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); //.3gp

        //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); //For Low Quality
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB); //For High Quality

        return mediaRecorder;
    }

    public static String getFilePath() {
        Log.d("BaseAudio","getFilePath()");
        String filepath;
        filepath = Environment.getExternalStorageDirectory().getPath();
        //filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
        filepath += "/MediaRecorderSample.3gp"; //AudioRecording.3gp

        //File file = new File(filepath, "MediaRecorderSample.3gp");
        //if (!file.exists()) file.mkdirs(); //Make a new Folder

        Log.d("AudioRecorder", "getFilePath() " + filepath);
        return (filepath);
    }

    public static String getDevNullPath() {
        Log.d("BaseAudio","getFilePath()");
        return "/dev/null";
    }
}
