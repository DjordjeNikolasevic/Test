package rs.etf.pmu;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MyAsyncTaskWithWeakReference extends AsyncTask<Void, Integer, Void> {

    private WeakReference<Activity> weakActivity;

    public MyAsyncTaskWithWeakReference(Activity activity) {
        weakActivity = new WeakReference<>(activity);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (int i = 0; i < 10; i++) {
            if (isCancelled()) {
                return null;
            }
            SystemClock.sleep(1000);
            publishProgress(i);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Activity activity = weakActivity.get();
        if (activity != null) {
            TextView textView = activity.findViewById(R.id.text_view_3);
            textView.setText(values[0] + "th iteration");
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Activity activity = weakActivity.get();
        if (activity != null) {
            TextView textView = activity.findViewById(R.id.text_view_3);
            textView.setText("Slept for 10000 ms!");
        }
    }

}