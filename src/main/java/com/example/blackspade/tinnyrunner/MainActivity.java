package com.example.blackspade.tinnyrunner;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button btnStart, btnReset, btnOptions;
    TextView tvTimeLeft, tvTimeElapsed, tvMessage;
    int totalMinutes = 60;
    int secElapsed = 0;
    int timerStatus = 0;
    CountdownRunner countdown;
    ProgressBar progress;

    private class CountdownRunner extends CountDownTimer{
        CountdownRunner(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }
        public void onTick(long millisUntilFinished){
            updateUI();
        }
        public void onFinish(){

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnOptions = (Button) findViewById(R.id.btnOptions);
        tvTimeLeft = (TextView) findViewById(R.id.tvTimeLeft);
        tvTimeElapsed = (TextView) findViewById(R.id.tvTimeElapsed);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        tvTimeElapsed.setText(getTimeElapsed());
        tvTimeLeft.setText(getTimeLeft());
        progress = (ProgressBar) findViewById(R.id.pbProgress);
        progress.setMax(totalMinutes * 60);
        progress.setVisibility(View.VISIBLE);

        btnStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(timerStatus == 0) {
                    //start the timer
                    countdown = new CountdownRunner(totalMinutes * 60000, 1000);
                    countdown.start();
                    btnStart.setText(R.string.pause);
                    btnReset.setText(R.string.stop);
                    timerStatus = 2;
                }
                else if(timerStatus == 2){
                    //pause the timer
                    btnStart.setText(R.string.unpause);
                    btnReset.setText(R.string.reset);
                    countdown.cancel();
                    timerStatus = 1;
                }
                else if(timerStatus == 1){
                    //unpause the timer
                    long seconds = totalMinutes * 60 - secElapsed;
                    countdown = new CountdownRunner(seconds * 1000, 1000);
                    countdown.start();
                    timerStatus = 2;
                    btnStart.setText(R.string.pause);
                    btnReset.setText(R.string.stop);
                }
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerStatus == 2){
                    //Stop the timer
                    timerStatus = 0;
                    countdown.cancel();
                }
                //reset the timer
                countdown = null;
                secElapsed = 0;
                tvTimeElapsed.setText(getTimeElapsed());
                tvTimeLeft.setText(getTimeLeft());
                btnStart.setText(R.string.start);
                btnReset.setText(R.string.reset);

            }
        });
        
    }

    private String getFormattedTime(int[] time){
        String mins = String.valueOf(time[0]);
        String secs = String.valueOf(time[1]);
        if(time[0] < 10)
            mins = "0" + mins;
        if(time[1] < 10)
            secs = "0" + secs;
        return mins + ":" + secs;
    }


    public String getTimeElapsed(){
        int[] time = new int[2];
        time[1] = secElapsed % 60;
        time[0] = (int) (secElapsed / 60);
        return getFormattedTime(time);
    }

    public String getTimeLeft(){
        int[] time = new int[2];
        int secLeft = totalMinutes * 60 - secElapsed;
        time[1] = secLeft % 60;
        time[0] = (int) (secLeft / 60);
        return getFormattedTime(time);
    }

    public void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                secElapsed += 1;
                tvTimeElapsed.setText(getTimeElapsed());
                tvTimeLeft.setText(getTimeLeft());
                progress.setProgress((int)secElapsed);
            }
        });
    }
}
