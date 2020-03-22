package com.example.benedict.ConnectionApp;

import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

public class PlayTimerAsyncTask extends AsyncTask<Integer, String, String> {

    private int counter;
    private WeakReference<MainActivity> activityWeakReference;

    PlayTimerAsyncTask(MainActivity activity) {
        Log.d("PlayTimerAsyncTask","Constructor");
        activityWeakReference = new WeakReference<MainActivity>(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Integer... integers) {
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
