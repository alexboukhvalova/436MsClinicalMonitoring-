package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class Speed extends Activity implements SensorEventListener{
    SensorManager sManager ;
    Sensor stepSensor ;
    private long steps = 0;
    private long time = 0l;
    //these three will calc time ,ditance,speed
    TextView timev;
    TextView disv;
    TextView speedv;
    TextView timerTextView;
    long startTime = 0;
    int height;
    private RadioGroup radioSexGroup;
    RadioButton male;
    RadioButton female;
    private Button btnDisplay;



    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);
        sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        TextView timev = (TextView) findViewById(R.id.timeRun);
        TextView disv = (TextView) findViewById(R.id.dis);
        TextView speedv = (TextView) findViewById(R.id.speed);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        btnDisplay = (Button) findViewById(R.id.btnDisplay);
        male = (RadioButton) findViewById(R.id.radioMale);
        female = (RadioButton) findViewById(R.id.radioFemale);

        btnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (male.isChecked()){
                    height=78;
                }
                if (female.isChecked()){
                    height=70;
                }

                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
                btnDisplay.setVisibility (View.INVISIBLE);
                male.setVisibility (View.INVISIBLE);
                female.setVisibility (View.INVISIBLE);


            }
        });


        System.out.println("we are here now");
        Intent intent = getIntent();
        int intValue = intent.getIntExtra("intVariableName", 0);



    }

    // the main job for this function is to try calc time
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }


        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if(steps==0)
                time=System.nanoTime();
            System.out.println("we are here");
            steps++;
        }
        if(steps==25){
            time=System.nanoTime()-time;
            time=time/25;
            time=time/10000000;
            float distance=getDistanceRun(25)/1000;
            float speed=distance/time;
            speedv.setText(speed+"");
            timev.setText(time+"");
            disv.setText(distance+"");

        }



    }
    // calculate distance by multipying steps by average height / 100000
    public float getDistanceRun(long steps){
        float distance = (float)(steps*height)/(float)100000;
        return distance;
    }

    @Override
    protected void onStart() {
        super.onStart();
        sManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onResume() {

        super.onResume();

    }
    @Override
    protected void onStop() {
        super.onStop();

        sManager.unregisterListener(this, stepSensor);
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


}
