package com.example.benedict.ConnectionApp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class PlayTimerAsyncTask extends AsyncTask<Integer, String, String> {

    private int currentDuration, totalduration;
    private WeakReference<MainActivity> activityWeakReference;
    private static PlayTimerAsyncTask playTimerAsyncTask;

    public static PlayTimerAsyncTask newInstance(MainActivity activity) {
        Log.d("PlayTimerAsyncTask","newInstance()");
        playTimerAsyncTask = new PlayTimerAsyncTask(activity);
        return playTimerAsyncTask;
    }

    PlayTimerAsyncTask(MainActivity activity) {
        Log.d("PlayTimerAsyncTask","Constructor");
        activityWeakReference = new WeakReference<MainActivity>(activity);
    }

    public static void execute() {
        Log.d("PlayTimerAsyncTask","execute()");
        playTimerAsyncTask.execute(1);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("PlayTimerAsyncTask","onPreExecute() Initialization");
        MainActivity activity = activityWeakReference.get();

        if (activity == null || activity.isFinishing()) {
            return;
        }
        currentDuration = 0;
        totalduration = AudioPlayer.getDuration();
        activity.setTimerDurationColour(true);
        activity.setTimerDurationText(currentDuration + " - " + totalduration);
    }

    @Override
    protected String doInBackground(Integer... integers) {
        Log.d("PlayTimerAsyncTask","doInBackground() background thread task");

        while (AudioPlayer.hasInstance()) {
            try {
                currentDuration = AudioPlayer.getCurrentDuration();
                publishProgress( currentDuration + " - " + totalduration);
                Log.d("PlayTimerAsyncTask","getCurrentDuration()" + currentDuration);
                Log.d("PlayTimerAsyncTask","getDuration()" + totalduration);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("PlayTimerAsyncTask","Error " + e.getMessage());
            }
        }
        publishProgress( "0 - " + totalduration);
        return "Play Timer Finished!";
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d("PlayTimerAsyncTask","onProgressUpdate for Main UI Thread");
        MainActivity activity = activityWeakReference.get();

        if (activity == null || activity.isFinishing()) {
            return;
        }

        activity.setTimerDurationText(values[0].toString());
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        Log.d("PlayTimerAsyncTask","onPostExecute() after doInBackground()");
        MainActivity activity = activityWeakReference.get();

        if (activity == null || activity.isFinishing()) {
            return;
        }
        //activity.setPlayVisibility();
        Toast.makeText(activity, string, Toast.LENGTH_SHORT).show();
    }
}
