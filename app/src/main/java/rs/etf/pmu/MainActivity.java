package rs.etf.pmu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    private MainViewModel mainViewModel;

    private MyHandlerThread myHandlerThread;

    private MyAsyncTaskWithWeakReference myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = findViewById(R.id.text_view_1);
        button1 = findViewById(R.id.button_1);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mainViewModel.getIteration().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 9) {
                    textView1.setText("Slept for 10000 ms!");
                } else {
                    textView1.setText(integer + "th iteration");
                }

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyAsyncTaskWithViewModel(mainViewModel).execute();
            }
        });

        textView2 = findViewById(R.id.text_view_2);
        button2 = findViewById(R.id.button_2);

        myHandlerThread = new MyHandlerThread();
        myHandlerThread.start();

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myHandlerThread.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        Handler mainThreadHandler = new Handler(Looper.getMainLooper());
                        for (int i = 0; i < 10; i++) {
                            SystemClock.sleep(1000);
                            final int finalIteration = i;
                            mainThreadHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    textView2.setText(finalIteration + "th iteration");
                                }
                            });
                        }
                        mainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                textView2.setText("Slept for 10000 ms!");
                            }
                        });
                    }
                });
            }
        });

        textView3 = findViewById(R.id.text_view_3);
        button3 = findViewById(R.id.button_3);

        if (savedInstanceState != null && savedInstanceState.getBoolean("startAsyncTask")) {
            myAsyncTask = new MyAsyncTaskWithWeakReference(this);
            myAsyncTask.execute();
        }

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAsyncTask = new MyAsyncTaskWithWeakReference(MainActivity.this);
                myAsyncTask.execute();
            }
        });

        textView4 = findViewById(R.id.text_view_4);
        button4 = findViewById(R.id.button_4);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2,
                4,
                1,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            SystemClock.sleep(1000);
                            final int finalIteration = i;
                            textView4.post(new Runnable() {
                                @Override
                                public void run() {
                                    textView4.setText(finalIteration + "th iteration");
                                }
                            });
                        }
                        textView4.post(new Runnable() {
                            @Override
                            public void run() {
                                textView4.setText("Slept for 10000 ms!");
                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (myAsyncTask != null && myAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            myAsyncTask.cancel(true);
            outState.putBoolean("startAsyncTask", true);
        }
    }

}
