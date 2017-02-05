package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class TappingActivity extends AppCompatActivity {
    private static final int NUM_TRIALS = 4;
    private static final int LEFTHAND = 1;
    private static final int RIGHTHAND = 2;
    private static final long PREP_COUNTDOWN_BASE = 6000;

    private Button instructionsBtn;
    private Button beginTapTestBtn;

    private Chronometer tapTestChronometer;
    private CountDownTimer countDownTimer;

    private TextView preparationTextView;
    private TextView countdownTextView;
    private TextView handPrepTextView;

    private int currentHand;
    private int numTrialsCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapping);
        addListenerOnButton();
        addCountDownTimer();
        addTapTestChronometer();

        currentHand = LEFTHAND;
        numTrialsCompleted = 0;

        preparationTextView = (TextView)findViewById(R.id.PreparationTextView);
        handPrepTextView = (TextView)findViewById(R.id.PrepTextView);
        countdownTextView = (TextView)findViewById(R.id.CountdownTextView);
    }

    public void addListenerOnButton() {
        final Context context = this;

        instructionsBtn = (Button)findViewById(R.id.InstructionsButton);
        beginTapTestBtn = (Button)findViewById(R.id.BeginTapTestButton);

        // add Instruction button listener
        instructionsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Your Title");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Click ok to exit!")
                        .setCancelable(false)
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity

                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        beginTapTestBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                initiateTapTest();
            }

        });
    }

    public void addCountDownTimer() {
        countDownTimer = new CountDownTimer(PREP_COUNTDOWN_BASE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTextView.setText(new Long(millisUntilFinished/1000).toString());
            }

            @Override
            public void onFinish() {
                countdownTextView.setVisibility(View.INVISIBLE);
                preparationTextView.setVisibility(View.INVISIBLE);
                handPrepTextView.setVisibility(View.INVISIBLE);
                performTapTest();
            }
        };
    }

    public void addTapTestChronometer() {
        tapTestChronometer = (Chronometer)findViewById(R.id.TapTestChronometer);

        tapTestChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            @Override
            public void onChronometerTick(Chronometer chronometer) {
                String currentTime = tapTestChronometer.getText().toString();
                if (currentTime.equals("00:10")) {
                    tapTestChronometer.stop();
                    numTrialsCompleted++;
                    System.out.println("Trials completed: " + numTrialsCompleted);
                    if (numTrialsCompleted == NUM_TRIALS) {
                        // end the test
                    } else {
                        tapTestChronometer.setVisibility(View.INVISIBLE);
                        handPrepTextView.setVisibility(View.INVISIBLE);
                        switchHands();
                        performCountdown();
                    }
                }
            }
        });
    }

    public void initiateTapTest() {
        instructionsBtn.setVisibility(View.INVISIBLE);
        beginTapTestBtn.setVisibility(View.INVISIBLE);

        performCountdown();
    }

    public void performCountdown() {

        switch (currentHand) {
            case LEFTHAND:  handPrepTextView.setText("With your LEFT hand,");
                            break;
            case RIGHTHAND: handPrepTextView.setText("With your RIGHT hand,");
                            break;
        }

        handPrepTextView.setVisibility(View.VISIBLE);
        preparationTextView.setVisibility(View.VISIBLE);
        countdownTextView.setVisibility(View.VISIBLE);

        countDownTimer.start();
    }

    public void performTapTest() {
        handPrepTextView.setText("Tap the boxed region");
        handPrepTextView.setVisibility(View.VISIBLE);

        tapTestChronometer.setBase(SystemClock.elapsedRealtime());
        tapTestChronometer.setVisibility(View.VISIBLE);
        tapTestChronometer.start();

        /*
         * TODO: Add functionality for counting taps
         *
         * TODO: Add a box to the screen for the user to tap on
         */

    }

    public void switchHands() {
        switch (currentHand) {
            case LEFTHAND:  currentHand = RIGHTHAND;
                            break;
            case RIGHTHAND: currentHand = LEFTHAND;
                            break;
        }
    }
}
