package com.example.benedict.ConnectionApp;

import android.os.Environment;
import android.util.Log;

abstract class BaseAudio {

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
}
