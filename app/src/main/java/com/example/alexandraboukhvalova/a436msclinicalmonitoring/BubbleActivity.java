package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
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

    private int trialNum = 0;
    private long timeOfBirth;
    private long timeOfDeath;
    private int passTrial=0;

    // to store response times
    private final ArrayList<Long> lifespans = new ArrayList<Long>();

    private Button bubble;
    private Button startTrial;
    private Button btn;

    public static final int LIB_ACCOUNT_NAME_REQUEST_CODE = 1001;
    public static final int LIB_AUTHORIZATION_REQUEST_CODE = 1002;
    public static final int LIB_PERMISSION_REQUEST_CODE = 1003;
    public static final int LIB_PLAY_SERVICES_REQUEST_CODE = 1004;

    private Sheets teamSheet;
    private Sheets centralSheet;

    private String teamSpreadsheetId = "1jus0ktF2tQw2sOjsxVb4zoDeD1Zw90KAMFNTQdkFiJQ";
    private String centralSpreadsheetId = "1YvI3CjS4ZlZQDYi5PaiA7WGGcoCsZfLoSFM0IdvdbDU";
    private String userId = "t04p03";

    private static final boolean WRITE_TO_CENTRAL = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);

        teamSheet = new Sheets(this, getString(R.string.app_name), teamSpreadsheetId);
        centralSheet = new Sheets(this, getString(R.string.app_name), centralSpreadsheetId);

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
            trialNum = 100;
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

            textView.setTextSize(25);
            textView.setVisibility(View.VISIBLE);

            teamSheet.writeData(Sheets.TestType.RH_POP, userId, (float) result);
            if (WRITE_TO_CENTRAL)
                centralSheet.writeData(Sheets.TestType.RH_POP, userId, (float) result);
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

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        teamSheet.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        teamSheet.onActivityResult(requestCode, resultCode, data);
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
