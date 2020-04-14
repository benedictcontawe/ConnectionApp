package com.example.benedict.ConnectionApp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class SoundLevelAsyncTask extends AsyncTask<Integer, String, String> {

    private WeakReference<MainActivity> activityWeakReference;
    private static SoundLevelAsyncTask soundLevelAsyncTask;

    public static SoundLevelAsyncTask newInstance(MainActivity activity, Boolean isRecording) {
        Log.d("SoundLevelAsyncTask","newInstance()");
        soundLevelAsyncTask = new SoundLevelAsyncTask(activity);
        return soundLevelAsyncTask;
    }

    SoundLevelAsyncTask(MainActivity activity) {
        Log.d("SoundLevelAsyncTask","Constructor");
        activityWeakReference = new WeakReference<MainActivity>(activity);
    }

    public static void execute() {
        Log.d("SoundLevelAsyncTask","execute()");
        soundLevelAsyncTask.execute(1);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("SoundLevelAsyncTask","onPreExecute() Initialization");
        MainActivity activity = activityWeakReference.get();

        if (activity == null || activity.isFinishing()) {
            return;
        }

    }

    @Override
    protected String doInBackground(Integer... integers) {
        Log.d("SoundLevelAsyncTask","doInBackground() background thread task");
        MainActivity activity = activityWeakReference.get();

        while (AudioRecorder.hasInstance()) {
            try {
                Log.e("SoundLevelAsyncTask","()" + AudioRecorder.getAmplitude());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("SoundLevelAsyncTask","Error " + e.getMessage());
            }
        }
        publishProgress( "0 - ");
        return "Play Timer Finished!";
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d("SoundLevelAsyncTask","onProgressUpdate for Main UI Thread");
        MainActivity activity = activityWeakReference.get();

        if (activity == null || activity.isFinishing()) {
            return;
        }

    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        Log.d("SoundLevelAsyncTask","onPostExecute() after doInBackground()");
        MainActivity activity = activityWeakReference.get();

        if (activity == null || activity.isFinishing()) {
            return;
        }

        Toast.makeText(activity, string, Toast.LENGTH_SHORT).show();
    }
}
