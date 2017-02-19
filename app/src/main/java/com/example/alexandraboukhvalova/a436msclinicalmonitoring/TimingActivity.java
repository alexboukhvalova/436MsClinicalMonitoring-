package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.content.Context;
import android.content.Intent;
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

public class TimingActivity extends AppCompatActivity {

    private TextView tempTextView;
    private TextView currEvent;
    private Button tempBtn;
    private Button homePage;
    //for you brian, this trialCounter will start at zero and increase to tell the in which triak
    //you are
    private int trialCounter;
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

    // must be accessible from anonymous inner classes
    private final int[] counter = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing);


        tempTextView = (TextView)findViewById(R.id.timerTextView);
        ((TextView)findViewById(R.id.timerTextView)).setVisibility(View.INVISIBLE);
        currEvent = (TextView)findViewById(R.id.eventTextView);
        homePage=(Button)findViewById(R.id.goHome);
        tempBtn = (Button)findViewById(R.id.starTimerButton);
        threeSecondTimerText = (TextView)findViewById(R.id.threeSecondTimer);
        trialCounter=0;
        tapTestChronometer = (Chronometer)findViewById(R.id.TapTestChronometer);
        tapTestChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            @Override
            public void onChronometerTick(Chronometer chronometer) {
                String currentTime = tapTestChronometer.getText().toString();
                if (currentTime.equals("00:11")) {
                    tapTestChronometer.stop();
                    tapTestChronometer.setVisibility(View.INVISIBLE);
                }
            }
        });

        hideArena();
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
    public void homeClick(View view){
        final Context context = this;
        homePage = (Button) findViewById(R.id.goHome);

        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);

            }
        });


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
        ((TextView)findViewById(R.id.timerTextView)).setText(minutes + ":" + seconds);
    }

    private void performTrialTiming() {
        TextView counterDisplay = (TextView) findViewById(R.id.tapCounterTextView);
        counterDisplay.setVisibility(View.INVISIBLE);
        tapTestChronometer.setBase(SystemClock.elapsedRealtime());
        tapTestChronometer.start();
        tapTestChronometer.setVisibility(View.VISIBLE);
    }

    private Runnable startTimer = new Runnable() {
        public void run() {

            tempBtn.setVisibility(View.GONE);

            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);

            if(secs <= 2) {
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(true);
                TextView counterDisplay = (TextView) findViewById(R.id.tapCounterTextView);
                counterDisplay.setVisibility(View.INVISIBLE);
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
                trialCounter++;
                /* Matt count the number of taps during this time period, but make a method outside of this Runnable*/
                /* Brian display first 10 second timer*/
                performTrialTiming();
                currEvent.setText("LEFT HAND TRIAL 1 of 5");
                tapHearing();
            }else if(secs == 17) {
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(false);
            } else if (secs == 19) {
                /* Matt display tap count from left hand trial 1*/
                //((TextView)findViewById(R.id.timerTextView)).setVisibility(View.INVISIBLE);
                currEvent.setText("Done!");
                hideArena();
                displayCounter();
            } else if (secs == 20) {
                /* display 10 second timer*/
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(true);
                performTrialTiming();
                trialCounter++;
                resetCounter();
                currEvent.setText("LEFT HAND TRIAL 2 of 5");
                tapHearing();
            }else if(secs == 30) {
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(false);
            } else if (secs == 32) {
                currEvent.setText("Done!");
                hideArena();
                displayCounter();
            }else if (secs == 33) {
                /* display 10 second timer*/
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(true);
                performTrialTiming();
                trialCounter++;
                resetCounter();
                currEvent.setText("LEFT HAND TRIAL 3 of 5");
                tapHearing();
            }
            else if (secs == 43) {

                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(false);
            }else if (secs == 45) {
                currEvent.setText("Done!");
                hideArena();
                displayCounter();
            }

            if(secs <= 48) {
                mHandler.postDelayed(this, REFRESH_RATE);
            } else {
                if(stopped){
                    startTime = System.currentTimeMillis() - elapsedTime;
                }
                else{
                    startTime = System.currentTimeMillis();
                }
                mHandler.removeCallbacks(startTimer2);
                mHandler.postDelayed(startTimer2, 0);

            }
        }
    };
    private Runnable startTimer2 = new Runnable() {
        public void run() {

            tempBtn.setVisibility(View.GONE);

            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);

            if(secs ==0) {
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(true);
                performTrialTiming();
                trialCounter++;
                resetCounter();
                currEvent.setText("LEFT HAND TRIAL 4 of 5");
                tapHearing();
            }else if(secs == 10) {
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(false);
            } else if (secs == 12) {
                currEvent.setText("Done!");
                hideArena();
                displayCounter();
            } else if (secs == 15) {
                /* display 10 second timer*/

                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(true);
                performTrialTiming();
                trialCounter++;
                resetCounter();
                currEvent.setText("LEFT HAND TRIAL 5 of 5");
                tapHearing();
            }else if(secs == 25) {
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(false);
            } else if (secs == 27) {
                /* Matt display tap count from left hand trial 1*/
                //((TextView)findViewById(R.id.timerTextView)).setVisibility(View.INVISIBLE);
                currEvent.setText("Done!");
                hideArena();
                displayCounter();
            }
            if(secs <= 30) {
                mHandler.postDelayed(this, REFRESH_RATE);
            } else {
                if(stopped){
                    startTime = System.currentTimeMillis() - elapsedTime;
                }
                else{
                    startTime = System.currentTimeMillis();
                }
                mHandler.removeCallbacks(startTimer3);
                mHandler.postDelayed(startTimer3, 0);

            }
        }
    };
    private Runnable startTimer3 = new Runnable() {
        public void run() {

            tempBtn.setVisibility(View.GONE);

            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);

            if(secs <= 2) {
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(true);
                TextView counterDisplay = (TextView) findViewById(R.id.tapCounterTextView);
                counterDisplay.setVisibility(View.INVISIBLE);
                /* Display indication to the user to use their left hand*/
                currEvent.setVisibility(View.VISIBLE);
                currEvent.setText("RIGHT HAND");
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
                currEvent.setText("RIGHT HAND TRIAL 1 of 5");
                trialCounter++;
                tapHearing();
            }else if(secs == 17) {
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(false);
            } else if (secs == 19) {
                currEvent.setText("Done!");
                hideArena();
                displayCounter();
            } else if (secs == 20) {
                /* display 10 second timer*/
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(true);
                performTrialTiming();
                trialCounter++;
                resetCounter();
                currEvent.setText("RIGHT HAND TRIAL 2 of 5");
                tapHearing();
            }else if(secs == 30) {
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(false);
            } else if (secs == 32) {
                currEvent.setText("Done!");
                hideArena();
                displayCounter();
            }else if (secs == 33) {
                /* display 10 second timer*/
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(true);
                performTrialTiming();
                trialCounter++;;
                resetCounter();
                currEvent.setText("RIGHT HAND TRIAL 3 of 5");
                tapHearing();
            }
            else if (secs == 43) {
                /* display tap count from left hand trial 1*/
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(false);
            }else if (secs == 45) {

                currEvent.setText("Done!");
                hideArena();
                displayCounter();
            }

            if(secs <= 48) {
                mHandler.postDelayed(this, REFRESH_RATE);
            } else {
                if(stopped){
                    startTime = System.currentTimeMillis() - elapsedTime;
                }
                else{
                    startTime = System.currentTimeMillis();
                }
                mHandler.removeCallbacks(startTimer4);
                mHandler.postDelayed(startTimer4, 0);

            }
        }
    };
    private Runnable startTimer4 = new Runnable() {
        public void run() {

            tempBtn.setVisibility(View.GONE);

            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);

            if(secs ==0) {
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(true);
                performTrialTiming();
                trialCounter++;;
                resetCounter();
                currEvent.setText("RIGHT HAND TRIAL 4 of 5");
                tapHearing();
            }else if(secs == 10) {
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(false);
            } else if (secs == 12) {
                currEvent.setText("Done!");
                hideArena();
                displayCounter();
            } else if (secs == 15) {
                /* display 10 second timer*/
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(true);
                performTrialTiming();
                trialCounter++;;
                resetCounter();
                currEvent.setText("RIGHT HAND TRIAL 5 of 5");
                tapHearing();
            }else if(secs == 25) {
                RelativeLayout allScreen = (RelativeLayout) findViewById(R.id.activity_timing);
                allScreen.setClickable(false);
            } else if (secs == 27) {
                currEvent.setText("Done!");
                hideArena();
                displayCounter();
            }
            else if (secs > 30) {
                currEvent.setText("Press Start Test to begin again or you can go to the home page.");
                TextView counterDisplay = (TextView) findViewById(R.id.tapCounterTextView);
                counterDisplay.setVisibility(View.INVISIBLE);
                ((TextView)findViewById(R.id.timerTextView)).setText("00:00");
            }

            if(secs <= 30) {
                mHandler.postDelayed(this, REFRESH_RATE);
            } else {
                tempBtn.setVisibility(View.VISIBLE);
                homePage.setVisibility(View.VISIBLE);
            }
        }
    };





    // initiates a trial; the entire screen is listening for taps
    private void tapHearing() {
        Button tapArena = (Button) findViewById(R.id.tapArena);
        tapArena.setOnClickListener(new View.OnClickListener() {
            // reset to 0 whenever the listener is set (?)
            int currCount = 0;
            @Override
            public void onClick(View view) {
                currCount++;
                counter[0] = currCount;
            }
        });
        tapArena.setVisibility(View.VISIBLE);
    }

    private void hideArena() {
        Button tapArena = (Button) findViewById(R.id.tapArena);
        tapArena.setVisibility(View.GONE);
    }

    public void displayCounter() {
        TextView counterDisplay = (TextView) findViewById(R.id.tapCounterTextView);
        counterDisplay.setVisibility(View.VISIBLE);
        counterDisplay.setText("Number of recorded taps: "+counter[0]);
    }
    public void resetCounter() {
        counter[0] = 0;
    }

    }
