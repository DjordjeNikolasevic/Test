package rs.etf.pmu;

import android.os.AsyncTask;
import android.os.SystemClock;

public class MyAsyncTaskWithViewModel extends AsyncTask<Void, Integer, Void> {

    private MainViewModel mainViewModel;

    public MyAsyncTaskWithViewModel(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (int i = 0; i < 10; i++) {
            SystemClock.sleep(1000);
            mainViewModel.setIteration(i);
        }
        return null;
    }

}
