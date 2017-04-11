package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.example.sheets436.Sheets;

import edu.umd.cmsc436.sheets.Sheets;

import org.w3c.dom.Text;

public class ArmCurlActivity extends Activity implements SensorEventListener, Sheets.Host{

    private SensorManager mSensorManager;
    //private Sensor mSensorAccel, mSensorMag, mSensorProx;
    private Sensor mSensorProx;

    private Button startStopArmCurlBtn;
    private TextView numTaps;

    public static final int LIB_ACCOUNT_NAME_REQUEST_CODE = 1001;
    public static final int LIB_AUTHORIZATION_REQUEST_CODE = 1002;
    public static final int LIB_PERMISSION_REQUEST_CODE = 1003;
    public static final int LIB_PLAY_SERVICES_REQUEST_CODE = 1004;

    private Sheets sheet;
    private String spreadsheetId = "1jus0ktF2tQw2sOjsxVb4zoDeD1Zw90KAMFNTQdkFiJQ";

    int taps;
    //int zeroPoints;
    //boolean startPosition;

    private float distance, maxDist;
    private boolean isStartPosition;

    private long secs,mins,hrs;
    private String minutes,seconds;
    private long startTime;
    private long elapsedTime;
    private Handler mHandler = new Handler();
    private final int REFRESH_RATE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arm_curl);
        initializeViews();

        taps = 0;
        //eroPoints = 0;
        isStartPosition = true;

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //mSensorAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //mSensorMag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorProx = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sheet = new Sheets(this, getString(R.string.app_name), spreadsheetId);

        startStopArmCurlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                mSensorManager.registerListener(ArmCurlActivity.this, mSensorProx, SensorManager.SENSOR_DELAY_NORMAL);
                startStopArmCurlBtn.setEnabled(false);
                startStopArmCurlBtn.setText("Test is running for right hand...");
                //startStopArmCurlBtn.setAlpha(.5f);
                maxDist = mSensorProx.getMaximumRange();

                //TODO start a timer
                startTime = System.currentTimeMillis();
                mHandler.removeCallbacks(startRightTimer);
                mHandler.postDelayed(startRightTimer, 0);
            }
        });
    }

    private Runnable startRightTimer = new Runnable() {
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);
            if(secs < 10) {
                mHandler.postDelayed(this, REFRESH_RATE);
            } else {
                //startStopArmCurlBtn.setEnabled(true);
                startStopArmCurlBtn.setText("Starting test for left hand...");
                //startStopArmCurlBtn.setAlpha(1.0f);
                sendToSheets(true);
                startTime = System.currentTimeMillis();
                taps = 0;
                mHandler.removeCallbacks(startRightTimer);
                mHandler.postDelayed(startLeftTimer, 500);
            }
        }
    };

    private Runnable startLeftTimer = new Runnable() {
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);
            if(secs < 10) {
                startStopArmCurlBtn.setText("Test is running for left hand...");
                mHandler.postDelayed(this, REFRESH_RATE);
            } else{

                startStopArmCurlBtn.setText("Done Test!");
                //startStopArmCurlBtn.setClickable(true);
                //startStopArmCurlBtn.setText("Start Test");
                //startStopArmCurlBtn.setAlpha(1.0f);
                sendToSheets(false);
            }
        }
    };


    private void sendToSheets(boolean right) {

        Sheets.TestType type;

        if(right){
            type = Sheets.TestType.RH_CURL;
        } else {
            type = Sheets.TestType.LH_CURL;
        }
        //TODO: CHANGE THIS TO MATCH YOURS
        String userId = "t04p06";

        sheet.writeData(type, userId, taps);
    }

    public void initializeViews() {
        startStopArmCurlBtn = (Button) findViewById(R.id.startStopArmCurlBtn);
        numTaps = (TextView) findViewById(R.id.numTapsTextView);
        numTaps.setTextColor(Color.BLACK);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if( event.sensor.getType() == Sensor.TYPE_PROXIMITY){
            distance = event.values[0];
            updateOrientationAngles();
        }

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
        ((TextView)findViewById(R.id.timerText)).setText(minutes + ":" + seconds);
    }
    public void updateOrientationAngles() {

        if(distance == 0.0f && isStartPosition){
            taps++;
            isStartPosition = false;
            numTaps.setText("Number of taps: " + taps);
        } else if(distance > 0){
            isStartPosition = true;
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        sheet.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sheet.onActivityResult(requestCode, resultCode, data);
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
