package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ArmCurlActivity extends Activity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private float lastX, lastY, lastZ;

    private float deltaXMax = 0;
    private float deltaYMax = 0;
    private float deltaZMax = 0;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    private TextView currentX, currentY, currentZ, maxX, maxY, maxZ;
    private Button startStopArmCurlBtn;
    private TextView numTaps;


    int taps;
    int zeroPoints;
    boolean startPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arm_curl);
        initializeViews();

        taps = 0;
        zeroPoints = 0;
        startPosition = true;

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        startStopArmCurlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mSensorManager.registerListener(ArmCurlActivity.this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
                startStopArmCurlBtn.setText("Stop Test");
                //TODO start a timer
            }
        });


    }

    public void initializeViews() {
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);

        maxX = (TextView) findViewById(R.id.maxX);
        maxY = (TextView) findViewById(R.id.maxY);
        maxZ = (TextView) findViewById(R.id.maxZ);

        startStopArmCurlBtn = (Button) findViewById(R.id.startStopArmCurlBtn);
        numTaps = (TextView) findViewById(R.id.numTapsTextView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        displayCleanValues();
        displayCurrentValues();
        displayMaxValues();

        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);

        //if change is below 2, it is just plain noise
        if (deltaX < 2) deltaX = 0;
        if (deltaY < 2) deltaY = 0;
        if (deltaZ < 2) deltaZ = 0;

        lastX = event.values[0];
        lastY = event.values[1];
        lastZ = event.values[2];

        //***calculate number of taps****
        //TODO verify on phone that this works

        //at the start position all of the delta values will be 0
        //after that they should all be zero when arm is in the middle of a curl, fully curled,
        //and fully stretched out. this means that there will be 4 times that the average of
        //delta values will be 0 per one bicep curl: half-way up, curled, half-way up, stretched out

        //when total number of 0 averages mod 4 equals 0, the number of full curls should increase
        //the first starting position of the test should not count towards counting 0 averages


        //vertical: per half curl - 0 acceleration in the center
        //horizontal: per half cult - 0 acceleration only when full streth or full curl

        if ((deltaX + deltaY + deltaZ)/3.0 == 0.0) {
            zeroPoints += 1;
        }

        //substract 1 to exclude the zero point counted when you just start the test
        if ((zeroPoints - 1) % 4 == 0) {
            taps += 1;
            numTaps.setText("Number of taps: " + taps);
        }

        if (taps == 10) {
            mSensorManager.unregisterListener(this);
            startStopArmCurlBtn.setText("Start Test");
            numTaps.setText("Number of taps: " + taps);

            //TODO include a timer stop here so we can record how long it took to do 10 taps
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void displayCleanValues() {
        currentX.setText("0.0");
        currentY.setText("0.0");
        currentZ.setText("0.0");
    }

    public void displayCurrentValues() {
        currentX.setText(Float.toString(deltaX));
        currentY.setText(Float.toString(deltaY));
        currentZ.setText(Float.toString(deltaZ));
    }

    public void displayMaxValues() {
        if (deltaX > deltaXMax) {
            deltaXMax = deltaX;
            maxX.setText(Float.toString(deltaXMax));
        }

        if (deltaY > deltaYMax) {
            deltaYMax = deltaY;
            maxY.setText(Float.toString(deltaYMax));
        }

        if (deltaZ > deltaZMax) {
            deltaZMax = deltaZ;
            maxZ.setText(Float.toString(deltaZMax));
        }
    }
}
