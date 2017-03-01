package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class BubbleActivity extends Activity {

    int trialNum = 0;
    long timeOfBirth;
    long timeOfDeath;

    // to store response times
    final ArrayList<Long> lifespans = new ArrayList<Long>();

    Button bubble;
    Button startTrial;

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
                timeOfDeath = System.currentTimeMillis()/1000;

                //TODO: this is not recording accurately for some reason...
                // using something other than System.currentTimeMillis() might be useful
                long lifespan = timeOfDeath - timeOfBirth;
                lifespans.add(lifespan);
                moveBubble();
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
            Button b = (Button) findViewById(R.id.bubble);

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
            timeOfBirth = System.currentTimeMillis()/1000;

            // increment trialNum
            trialNum++;
        }
    }

    public void initialLocation() {
        Button b = (Button) findViewById(R.id.bubble);

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
        timeOfBirth = System.currentTimeMillis()/1000;
    }
}
