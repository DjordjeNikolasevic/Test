package rs.etf.pmu;

import android.os.Handler;
import android.os.Looper;

public class MyHandlerThread extends Thread {

    private Looper looper;
    private Handler handler;

    @Override
    public void run() {
        Looper.prepare();

        looper = Looper.myLooper();
        handler = new Handler();

        Looper.loop();
    }

    public Looper getLooper() {
        return looper;
    }

    public Handler getHandler() {
        return handler;
    }

}
