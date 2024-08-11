package com.example.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.MessageFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView time;
    Button reset,start,stop;
    int seconds,minutes,milliSeconds;

    long millisecond , startTime , timeBuff , updateTime = 0L;

    Handler handler;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            millisecond = SystemClock.uptimeMillis()- startTime;
            updateTime = timeBuff + millisecond;
            seconds = (int)(updateTime/1000);
            minutes = seconds/60;
            seconds = seconds%60;
            milliSeconds = (int)(updateTime%1000);
            time.setText(MessageFormat.format("{0}:{1}:{2}",minutes,String.format(Locale.getDefault(),"%02d",seconds),String.format(Locale.getDefault(),"%02d",milliSeconds)));
           handler.postDelayed(this,0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        time = findViewById(R.id.textView);
        reset = findViewById(R.id.button5);
        start = findViewById(R.id.button6);
        stop = findViewById(R.id.button7);

        handler = new Handler(Looper.getMainLooper());
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable,0);
                reset.setEnabled(false);
                stop.setEnabled(true);
                start.setEnabled(false);
            }
        });

       stop.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               timeBuff+= millisecond;
               handler.removeCallbacks(runnable);
               reset.setEnabled(true);
               stop.setEnabled(false);
               start.setEnabled(true);
           }
       });

       reset.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               millisecond= 0L;
               startTime = 0L;
               timeBuff = 0L;
               updateTime = 0L;
               seconds = 0;
               minutes = 0;
               milliSeconds = 0;
               time.setText("00:00:00");
           }
       });

       time.setText("00:00:00");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}