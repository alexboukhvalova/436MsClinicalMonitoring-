package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
    Button b;

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
            b = (Button) findViewById(R.id.bubble);

            // get screen dimensions
            RelativeLayout.LayoutParams scene = (RelativeLayout.LayoutParams) b.getLayoutParams();
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            // set new location for bubble
            scene.leftMargin = b.getWidth()
                    + new Random().nextInt(metrics.widthPixels - 5 * b.getWidth());
            scene.topMargin = b.getHeight()
                    + new Random().nextInt(metrics.heightPixels - 3 * b.getHeight());
            b.setLayoutParams(scene);

            // save time of appearance as time of birth
            // increment trialNum

            trialNum++;

            timeOfBirth = System.nanoTime();

            b.postDelayed(new Runnable() {
                public void run() {
                    moveBubble();
                }
            }, 1000);

        }
        if (trialNum == 10) {
            int i=0;
            double result = 0.0;
            DecimalFormat precision = new DecimalFormat("0.00");


            for (Long s : lifespans) {
                result=result+(s/1000000000.0);
            }

            if(passTrial>0)
            result=result/passTrial;
            else
            result=0.0;



            b.setVisibility(View.INVISIBLE);
            TextView textView=(TextView) findViewById(R.id.showResult);
            textView.setText("you hit "+passTrial+"\n"+
                    "you take "+precision.format(result)+" a second to respond in average for the pass once");

            textView.setTextSize(25);

            textView.setVisibility(View.VISIBLE);


        }
    }

    public void initialLocation() {
         b = (Button) findViewById(R.id.bubble);

        // get screen dimensions
        RelativeLayout.LayoutParams scene = (RelativeLayout.LayoutParams) b.getLayoutParams();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // set new location for bubble
        scene.leftMargin = b.getWidth()
                + new Random().nextInt(metrics.widthPixels - 3 * b.getWidth());
        scene.topMargin = b.getHeight()
                + new Random().nextInt(metrics.heightPixels - 3 * b.getHeight());
        b.setLayoutParams(scene);

        // save time of appearance as time of birth

        timeOfBirth =  System.nanoTime();

        b.postDelayed(new Runnable() {
            public void run() {
                moveBubble();
            }
        }, 1000);
    }
}
