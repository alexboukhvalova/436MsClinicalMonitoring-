package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edu.umd.cmsc436.sheets.Sheets;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class BubbleActivity extends Activity implements Sheets.Host {

    int totalBubbles = 0;
    int poppedBubbles = 0;
    long timeOfBirth;
    long timeOfDeath;

    // to store response times
    final ArrayList<Double> lifespans = new ArrayList<Double>();

    int oldBubbleX;
    int oldBubbleY;
    int newBubbleX;
    int newBubbleY;

    Button startTrial;
    Button bubble;
    TextView debugNarrator;

    public static final int LIB_ACCOUNT_NAME_REQUEST_CODE = 1001;
    public static final int LIB_AUTHORIZATION_REQUEST_CODE = 1002;
    public static final int LIB_PERMISSION_REQUEST_CODE = 1003;
    public static final int LIB_PLAY_SERVICES_REQUEST_CODE = 1004;

    // main spreadsheet information
    private Sheets centralSheet;
    private Sheets teamSheet;

    private String centralSpreadsheetId = "1YvI3CjS4ZlZQDYi5PaiA7WGGcoCsZfLoSFM0IdvdbDU";
    private String teamSpreadsheetId = "1jus0ktF2tQw2sOjsxVb4zoDeD1Zw90KAMFNTQdkFiJQ";

    // user id
    private static final String USER_ID = "t04p05";

    // indicates if test should write to central spreadsheet
    private static final boolean WRITE_TO_CENTRAL = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);

        debugNarrator = (TextView) findViewById(R.id.debugNarrator);
        debugNarrator.setVisibility(View.INVISIBLE);

        // initialize sheet
        centralSheet = new Sheets(this, getString(R.string.app_name), centralSpreadsheetId);
        teamSheet = new Sheets(this, getString(R.string.app_name), teamSpreadsheetId);

        bubble = (Button) findViewById(R.id.bubble);

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
        bubble.setVisibility(View.GONE);

        // the max number of trials can be changed here
        if (totalBubbles < 10) {
            bubble = (Button) findViewById(R.id.bubble);

            // get screen dimensions
            RelativeLayout.LayoutParams scene = (RelativeLayout.LayoutParams) bubble.getLayoutParams();
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            // set new location for bubble
            int fullWidth = metrics.widthPixels;
            int fullHeight = metrics.heightPixels;
            int x = bubble.getWidth()
                    + new Random()
                    .nextInt(fullWidth - (4 * bubble.getWidth()));
            int y = bubble.getHeight()
                    + new Random()
                    .nextInt(fullHeight - (5 * bubble.getHeight()));

            scene.leftMargin = x;
            scene.topMargin = y;

            // set bubble at new location
            bubble.setLayoutParams(scene);
            bubble.setVisibility(View.VISIBLE);

            // save time of appearance as time of birth
            timeOfBirth = System.nanoTime();

            // increment trialNum
            totalBubbles++;

            // update old and new coordinates
            oldBubbleX = newBubbleX;
            oldBubbleY = newBubbleY;
            newBubbleX = x;
            newBubbleY = y;

            // calculate distance between old and new coordinates
            int a = Math.abs((newBubbleX - oldBubbleX)^2);
            int b = Math.abs((newBubbleY - oldBubbleY)^2);

            // TODO : this should factor into the metric
            double distance = Math.sqrt(a + b);

            /*
            // FOR DEBUG PURPOSES
            debugNarrator.setText("full: " + fullWidth + " x " + fullHeight + "\n"
                            + "old location: " + oldBubbleX + " x " + oldBubbleY + "\n"
                            + "new location: " + newBubbleX + " x " + newBubbleY + "\n"
                            + "distX: " + a + "\n"
                            + "distY: " + b + "\n"
                            + "distance: " + distance + "\n"
                            + "birthday: " + timeOfBirth + "\n"
                            + "deathday: " + timeOfDeath + "\n"
                    //+ "lifespan: " + lifespan + "\n"
            );
            */


            bubble.postDelayed(new Runnable() {
                public void run() {
                    moveBubble();
                }
            }, 5000);
        }

        if (totalBubbles == 10) {
            // not sure why this is here; who added?
            totalBubbles = 100;

            double result = 0.0;
            DecimalFormat precision = new DecimalFormat("0.00");

            /*
            // FOR DEBUG PURPOSES
            String detailData = "";
            for (int i = 0; i < lifespans.size(); i++) {
                detailData += "" + i + ": " + precision.format(lifespans.get(i)) + "\n";
            }
            */

            if(poppedBubbles > 0) {
                double totalReactionTime = 0;
                for (int i = 0; i < lifespans.size(); i++) {
                    totalReactionTime += lifespans.get(i);
                }
                result = totalReactionTime / poppedBubbles;
            } else {
                result = 0.0;
            }

            bubble.setVisibility(View.INVISIBLE);
            TextView resultScreen = (TextView) findViewById(R.id.showResult);

            resultScreen.setText("You hit " + poppedBubbles + " bubbles.\n"
                            + "Your average tap response time was " + precision.format(result)
                            + " seconds.\n"
                    //+ detailData
            );

            teamSheet.writeData(Sheets.TestType.LH_POP, USER_ID, (float) result);

            if (WRITE_TO_CENTRAL)
                centralSheet.writeData(Sheets.TestType.LH_POP, USER_ID, (float) result);

            resultScreen.setTextSize(25);
            resultScreen.setVisibility(View.VISIBLE);
        }
    }

    public void initialLocation() {
        bubble = (Button) findViewById(R.id.bubble);

        // get screen dimensions
        RelativeLayout.LayoutParams scene = (RelativeLayout.LayoutParams) bubble.getLayoutParams();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // set initial location for bubble
        int fullWidth = metrics.widthPixels;
        int fullHeight = metrics.heightPixels;
        int x = bubble.getWidth()
                + new Random()
                .nextInt(fullWidth - (5 * bubble.getWidth()));
        int y = bubble.getHeight()
                + new Random()
                .nextInt(fullHeight - (5 * bubble.getHeight()));

        /*
        // FOR DEBUG PURPOSES
        debugNarrator.setText("full: " + fullWidth + " x " + fullHeight + "\n"
                + "location: " + x + " x " + y);
        */

        scene.leftMargin = x;
        scene.topMargin = y;

        oldBubbleX = x;
        oldBubbleY = y;
        newBubbleX = x;
        newBubbleY = y;

        bubble.setLayoutParams(scene);

        // save time of appearance as time of birth
        timeOfBirth =  System.nanoTime();


        bubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poppedBubbles++;
                timeOfDeath = System.nanoTime();
                saveLife();
                moveBubble();
            }
        });
    }

    private void saveLife() {
        double lifespan = ((timeOfDeath - timeOfBirth)/1000000000.0);
        lifespans.add(lifespan);
    }

    // the following four methods for Sheet implementation have been copied directly from the
    // class example app
    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        centralSheet.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        centralSheet.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int getRequestCode(Sheets.Action action) {
        switch (action) {
            case REQUEST_ACCOUNT_NAME:
                return LIB_ACCOUNT_NAME_REQUEST_CODE;
            case REQUEST_AUTHORIZATION:
                return LIB_AUTHORIZATION_REQUEST_CODE;
            case REQUEST_PERMISSIONS:
                return LIB_PERMISSION_REQUEST_CODE;
            case REQUEST_PLAY_SERVICES:
                return LIB_PLAY_SERVICES_REQUEST_CODE;
            default:
                return -1;
        }
    }

    @Override
    public void notifyFinished(Exception e) {
        if (e != null) {
            throw new RuntimeException(e);
        }
        Log.i(getClass().getSimpleName(), "Done");
    }
}
