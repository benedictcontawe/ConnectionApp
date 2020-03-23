package com.example.benedict.ConnectionApp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class RecordTimerAsyncTask extends AsyncTask<Integer, Integer, String> {

    private int counter;
    private WeakReference<MainActivity> activityWeakReference;
    private static RecordTimerAsyncTask recordTimerAsyncTask;

    public static RecordTimerAsyncTask newInstance(MainActivity activity) {
        Log.d("RecordTimerAsyncTask","newInstance()");
        recordTimerAsyncTask = new RecordTimerAsyncTask(activity);
        return recordTimerAsyncTask;
    }

    RecordTimerAsyncTask(MainActivity activity) {
        Log.d("RecordTimerAsyncTask","Constructor");
        activityWeakReference = new WeakReference<MainActivity>(activity);
    }

    public static void execute() {
        Log.d("RecordTimerAsyncTask","execute()");
        recordTimerAsyncTask.execute(1);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("RecordTimerAsyncTask","onPreExecute() Initialization");
        MainActivity activity = activityWeakReference.get();

        if (activity == null || activity.isFinishing()) {
            return;
        }
        counter = 0;
        activity.setMicrophoneImage(true);
        activity.setTimerDurationColour(true);
    }

    @Override
    protected String doInBackground(Integer... integers) {
        Log.d("RecordTimerAsyncTask","doInBackground() background thread task");
        MainActivity activity = activityWeakReference.get();

        while (activity.getMicrophoneImage() == activity.isMicrphonePressed()) {
            publishProgress(counter);
            try {
                Thread.sleep(1000);
                counter++;
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e("RecordTimerAsyncTask","Error " + e.getMessage());
            }
        }

        return "Record Timer Finished!";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.d("RecordTimerAsyncTask","onProgressUpdate for Main UI Thread");
        MainActivity activity = activityWeakReference.get();

        if (activity == null || activity.isFinishing()) {
            return;
        }

        activity.setTimerDurationText(values[0].toString());
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        Log.d("RecordTimerAsyncTask","onPostExecute() after doInBackground()");
        MainActivity activity = activityWeakReference.get();

        if (activity == null || activity.isFinishing()) {
            return;
        }
        activity.setPlayVisibility();
        Toast.makeText(activity, string, Toast.LENGTH_SHORT).show();
    }
}
