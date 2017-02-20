package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LevelActivity extends AppCompatActivity {


    Chronometer levelTestChronometer;
    private TextView tempTextView;
    private TextView currEvent;
    private Button tempBtn;
    private Chronometer tapTestChronometer;
    private Handler mHandler = new Handler();
    private long startTime;
    private long elapsedTime;
    private final int REFRESH_RATE = 100;
    private String minutes,seconds;
    private long secs,mins,hrs,msecs;
    private boolean stopped = false;
    private TextView threeSecondTimerText;
    private Button tapArena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        tempTextView = (TextView)findViewById(R.id.timerTextView2);
        ((TextView)findViewById(R.id.timerTextView2)).setVisibility(View.INVISIBLE);
        currEvent = (TextView)findViewById(R.id.eventTextView2);
        tempBtn = (Button)findViewById(R.id.starTimerButton2);
        threeSecondTimerText = (TextView)findViewById(R.id.threeSecondTimer2);

        levelTestChronometer = (Chronometer)findViewById(R.id.TapTestChronometer2);
        levelTestChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            @Override
            public void onChronometerTick(Chronometer chronometer) {
                String currentTime = levelTestChronometer.getText().toString();
                if (currentTime.equals("00:11")) {
                    levelTestChronometer.stop();
                    levelTestChronometer.setVisibility(View.INVISIBLE);
                }
            }
        });


    }

    public void startClick (View view){
        if(stopped){
            startTime = System.currentTimeMillis() - elapsedTime;
        }
        else{
            startTime = System.currentTimeMillis();
        }
        mHandler.removeCallbacks(startTimer);
        mHandler.postDelayed(startTimer, 0);
    }

    private void updateTimer (float time){
        secs = (long)(time/1000);
        mins = (long)((time/1000)/60);
        hrs = (long)(((time/1000)/60)/60);

		/* Convert the seconds to String
		 * and format to ensure it has
		 * a leading zero when required
		 */
        secs = secs % 60;
        seconds=String.valueOf(secs);
        if(secs == 0){
            seconds = "00";
        }
        if(secs <10 && secs > 0){
            seconds = "0"+seconds;
        }

		/* Convert the minutes to String and format the String */

        mins = mins % 60;
        minutes=String.valueOf(mins);
        if(mins == 0){
            minutes = "00";
        }
        if(mins <10 && mins > 0){
            minutes = "0"+minutes;
        }

		/* Setting the timer text to the elapsed time */
        ((TextView)findViewById(R.id.timerTextView2)).setText(minutes + ":" + seconds);
    }

    private void performTrialTiming() {

        levelTestChronometer.setBase(SystemClock.elapsedRealtime());
        levelTestChronometer.start();
        levelTestChronometer.setVisibility(View.VISIBLE);
    }

    private Runnable startTimer = new Runnable() {
        public void run() {

            tempBtn.setVisibility(View.GONE);

            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);

            if(secs <= 2) {
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_level);

                /* Display indication to the user to use their left hand*/
                currEvent.setVisibility(View.VISIBLE);
                currEvent.setText("LEFT HAND");
                threeSecondTimerText.setVisibility(View.VISIBLE);
                threeSecondTimerText.setText("Get ready!");
            }else if (secs >= 4 && secs < 7) {
                if (secs == 4) {
                    threeSecondTimerText.setVisibility(View.VISIBLE);
                }
                /* Display a 3 second countdown in the middle of the screen in big numbers*/
                threeSecondTimerText.setText("" + (3 - (secs - 4)));
                //currEvent.setText("3 SECOND COUNTDOWN");
            } else if (secs == 7) {
                threeSecondTimerText.setVisibility(View.GONE);

                performTrialTiming();
                currEvent.setText("LEFT HAND TRIAL");

            }else if(secs == 17) {
                //RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_level);
                //allScreen.setClickable(false);
            } else if (secs == 19) {
                /* Matt display tap count from left hand trial 1*/
                //((TextView)findViewById(R.id.timerTextView)).setVisibility(View.INVISIBLE);
                currEvent.setText("Get your right hand ready");

            } else if (secs == 22) {
                /* display 10 second timer*/
                //RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_level);
                //allScreen.setClickable(true);
                performTrialTiming();
                currEvent.setText("RIGHT HAND TRIAL");

            }  else if (secs == 33) {
                /* display tap count from left hand trial 1*/
                //RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_level);
                //allScreen.setClickable(false);
                currEvent.setText("Done!");

            } else if (secs > 35) {
                currEvent.setText("Press Start Test to begin again.");
                //TextView counterDisplay = (TextView) findViewById(R.id.tapCounterTextView);
                //counterDisplay.setVisibility(View.INVISIBLE);
                ((TextView)findViewById(R.id.timerTextView2)).setText("00:00");
            }

            if(secs <= 34) {
                mHandler.postDelayed(this, REFRESH_RATE);
            } else {
                tempBtn.setVisibility(View.VISIBLE);
            }
        }
    };



    /*
    private void hideArena() {
        Button tapArena = (Button) findViewById(R.id.tapArena);
        tapArena.setVisibility(View.GONE);
    }

    public void displayCounter() {
        TextView counterDisplay = (TextView) findViewById(R.id.tapCounterTextView);
        counterDisplay.setVisibility(View.VISIBLE);
        counterDisplay.setText("Number of recorded taps: "+counter[0]);
    }

    */
}