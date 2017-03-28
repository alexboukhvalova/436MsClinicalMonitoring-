package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ArmCurlActivity extends Activity implements SensorEventListener{

    private SensorManager mSensorManager;
    //private Sensor mSensorAccel, mSensorMag, mSensorProx;
    private Sensor mSensorProx;

    private Button startStopArmCurlBtn;
    private TextView numTaps;


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

        startStopArmCurlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //mSensorManager.registerListener(ArmCurlActivity.this, mSensorAccel, SensorManager.SENSOR_DELAY_NORMAL);
                //mSensorManager.registerListener(ArmCurlActivity.this, mSensorMag, SensorManager.SENSOR_DELAY_NORMAL);
                mSensorManager.registerListener(ArmCurlActivity.this, mSensorProx, SensorManager.SENSOR_DELAY_NORMAL);
                startStopArmCurlBtn.setClickable(false);
                startStopArmCurlBtn.setText("Stop Test");
                startStopArmCurlBtn.setAlpha(.5f);
                maxDist = mSensorProx.getMaximumRange();

                //TODO start a timer
                startTime = System.currentTimeMillis();
                mHandler.removeCallbacks(startTimer);
                mHandler.postDelayed(startTimer, 0);
            }
        });
    }

    private Runnable startTimer = new Runnable() {
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);
            if(secs < 10) {
                mHandler.postDelayed(this, REFRESH_RATE);
            } else {
                startStopArmCurlBtn.setClickable(true);
                startStopArmCurlBtn.setText("Start Test");
                startStopArmCurlBtn.setAlpha(1.0f);
            }
        }
    };

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

}
