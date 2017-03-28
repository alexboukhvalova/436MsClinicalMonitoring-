package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class BubbleActivity extends Activity {

    int trialNum = 0;
    long timeOfBirth;
    long timeOfDeath;
    int passTrial=0;

    // to store response times
    final ArrayList<Long> lifespans = new ArrayList<Long>();

    Button bubble;
    Button startTrial;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);

        bubble = (Button) findViewById(R.id.bubble);
        bubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on click, record the difference between time of appearance
                // and time of click
                timeOfDeath =  System.nanoTime();
                //nano second fro recording trials
                long lifespan = (timeOfDeath - timeOfBirth);
                lifespans.add(lifespan);
                passTrial++;

            }
        });

        // the bubble should not be visible until the trial has started
        bubble.setVisibility(View.GONE);

        startTrial = (Button) findViewById(R.id.startTrial);
        startTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTest();
                // remove the start trial button
                startTrial.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void startTest() {
        initialLocation();
        bubble.setVisibility(View.VISIBLE);
    }

    public void moveBubble() {

        // the max number of trials can be changed here
        if (trialNum < 10) {
            btn = (Button) findViewById(R.id.bubble);

            // get screen dimensions
            RelativeLayout.LayoutParams scene = (RelativeLayout.LayoutParams) btn.getLayoutParams();
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            // set new location for bubble
            scene.leftMargin = btn.getWidth()
                    + new Random().nextInt(metrics.widthPixels - 5 * btn.getWidth());
            scene.topMargin = btn.getHeight()
                    + new Random().nextInt(metrics.heightPixels - 3 * btn.getHeight());
            btn.setLayoutParams(scene);

            // save time of appearance as time of birth
            // increment trialNum
            trialNum++;

            timeOfBirth = System.nanoTime();

            btn.postDelayed(new Runnable() {
                public void run() {
                    moveBubble();
                }
            }, 1000);

        }
        if (trialNum == 10) {
            double result = 0.0;
            DecimalFormat precision = new DecimalFormat("0.00");

            for (Long s : lifespans) {
                result=result+(s/1000000000.0);
            }

            if(passTrial>0)
                result=result/passTrial;
            else
                result=0.0;

            btn.setVisibility(View.INVISIBLE);
            TextView textView=(TextView) findViewById(R.id.showResult);
            textView.setText("You hit " + passTrial+"\n"+
                    "Your average tap response time was " + precision.format(result) + " seconds");

            // Send data to GOOGLE SHEETS
            sendToSheets(passTrial, result, Sheets.UpdateType.LH_POP.ordinal());

            textView.setTextSize(25);
            textView.setVisibility(View.VISIBLE);
        }
    }

    public void initialLocation() {
        btn = (Button) findViewById(R.id.bubble);

        // get screen dimensions
        RelativeLayout.LayoutParams scene = (RelativeLayout.LayoutParams) btn.getLayoutParams();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // set new location for bubble
        scene.leftMargin = btn.getWidth()
                + new Random().nextInt(metrics.widthPixels - 3 * btn.getWidth());
        scene.topMargin = btn.getHeight()
                + new Random().nextInt(metrics.heightPixels - 3 * btn.getHeight());
        btn.setLayoutParams(scene);

        // save time of appearance as time of birth
        timeOfBirth =  System.nanoTime();

        btn.postDelayed(new Runnable() {
            public void run() {
                moveBubble();
            }
        }, 1000);
    }

    private void sendToSheets(int hits, double avg, int sheet) {
        Intent sheets = new Intent(this, Sheets.class);
        ArrayList<String> row = new ArrayList<>();
        SimpleDateFormat dateFormat;
        Calendar calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());

        // Add components to row
        row.add(Integer.toString(Sheets.teamID));
        row.add(dateFormat.format(calendar.getTime()));
        row.add("1");
        row.add(Integer.toString(hits));
        row.add(Double.toString(avg));

        sheets.putStringArrayListExtra(Sheets.EXTRA_SHEETS, row);
        sheets.putExtra(Sheets.EXTRA_TYPE, sheet);
        startActivity(sheets);
    }
}
