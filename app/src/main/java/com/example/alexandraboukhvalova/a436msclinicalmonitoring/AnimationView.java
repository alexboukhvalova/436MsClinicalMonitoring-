package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import static android.content.Context.SENSOR_SERVICE;

import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.umd.cmsc436.sheets.Sheets;


/**
 * Created by jonf on 12/6/2016.
 */

public class AnimationView extends View implements SensorEventListener {
    public static float MAX_VELOCITY = 100;
    public static float DEFAULT_BALL_RADIUS = 100;
    private static final Random _random = new Random();

    private Paint _paintText = new Paint();
    private Paint _paintTrail = new Paint();

    private int _desiredFramesPerSecond = 40;

    private ArrayList<Ball> balls = new ArrayList<Ball>();
    private ArrayList<Point> points = new ArrayList<Point>();

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    //private TextView text;

    // This is for measuring frame rate, you can ignore
    private float _actualFramesPerSecond = -1;
    private long _startTime = -1;
    private int _frameCnt = 0;

    //https://developer.android.com/reference/java/util/Timer.html
    private Timer _timer = new Timer("AnimationView");
    private long lastTimeAccelSensorChangedInMs = -1;
    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];

    private Bitmap scaled_pic;
    private Point median;

    //Context context;
    private float score;

    private long secs,mins,hrs;
    private String minutes,seconds;
    private long startTime;
    private long elapsedTime;
    private Handler mHandler = new Handler();
    private final int REFRESH_RATE = 100;

    boolean done = false;

    private TextView mTimer;
    private TextView mStartButton;

    public AnimationView(Context context) {
        super(context);
        init(null, null, 0);
        setBackgroundResource(R.drawable.bullseye);
    }

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
        setBackgroundResource(R.drawable.bullseye);
    }

    public AnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
        setBackgroundResource(R.drawable.bullseye);
    }

    /*Ensure that the view is always a square*/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = Math.min(getMeasuredWidth(),getMeasuredHeight());
        setMeasuredDimension(size, size);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setBackgroundResource(R.drawable.bullseye);
        _paintTrail.setStyle(Paint.Style.FILL);
        _paintTrail.setAntiAlias(true);

        _paintText.setColor(Color.BLACK);
        _paintText.setTextSize(40f);

        //this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);


    }

    public void startTest(TextView timer, TextView startButton, int x, int y) {
        startTime = System.currentTimeMillis();
        mTimer = timer;
        mStartButton = startButton;
        mHandler.removeCallbacks(startTimer);
        mHandler.postDelayed(startTimer, 0);
        scaled_pic = prepareToMeasure();

        AnimationView.Ball ball = new AnimationView.Ball(DEFAULT_BALL_RADIUS, x, y, 0, 0, Color.argb(255, 10, 10, 10));
        points.add(new Point(x, y));
        synchronized (this.balls) {
            if (this.balls.size() < 1) {
                this.balls.add(ball);
            }

        }
        invalidate();

    }


    private Runnable startTimer = new Runnable() {
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);
            if(secs < 10) {
                mHandler.postDelayed(this, REFRESH_RATE);
            } else {
                done = true;
                score();
                //mStartButton.setEnabled(true);
                mStartButton.setText("Done!");
                ((LevelActivity) getContext()).sendToSheets(score);
            }
        }
    };

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
        mTimer.setText(minutes + ":" + seconds);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(done){
            score();
            mSensorManager.unregisterListener(this);
        }

        // start time helps measure fps calculations
        if(_startTime == -1) {
            _startTime = SystemClock.elapsedRealtime();
        }
        _frameCnt++;



        synchronized (this.balls) {
            for(Ball ball : this.balls){
                canvas.drawCircle(ball.xLocation, ball.yLocation, ball.radius, ball.paint);
            }
        }

        //canvas.drawCircle(3*getWidth()/8,3*getWidth()/8,40,_paintText);
        //canvas.drawCircle(getWidth()/4,getWidth()/4,40,_paintText);

        /* The code below is about measuring and printing out fps calculations. You can ignore
        long endTime = SystemClock.elapsedRealtime();
        long elapsedTimeInMs = endTime - _startTime;
        if(elapsedTimeInMs > 1000){
            _actualFramesPerSecond = _frameCnt / (elapsedTimeInMs/1000f);
            _frameCnt = 0;
            _startTime = -1;
        }
        //MessageFormat: https://developer.android.com/reference/java/text/MessageFormat.html
        canvas.drawText(MessageFormat.format("fps: {0,number,#.#}", _actualFramesPerSecond), 5, 40, _paintText);
        */
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                //Log.i("MOVE","MOVING");
                //Log.d("sensor", "1 " + event.values[1] + " 2 " + event.values[2]);
                if(lastTimeAccelSensorChangedInMs == -1) {
                    lastTimeAccelSensorChangedInMs = SystemClock.currentThreadTimeMillis();
                }
                // In this example, alpha is calculated as t / (t + dT),
                // where t is the low-pass filter's time-constant and
                // dT is the event delivery rate.

                final float alpha = 0.8f;

                // Isolate the force of gravity with the low-pass filter.
                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                // Remove the gravity contribution with the high-pass filter.
                linear_acceleration[0] = event.values[0] - gravity[0];
                linear_acceleration[1] = event.values[1] - gravity[1];
                linear_acceleration[2] = event.values[2] - gravity[2];

                for(Ball b : balls){
                    b.xVelocity -= linear_acceleration[0];

                    b.yVelocity += linear_acceleration[1];
                    if(b.xLocation + b.xVelocity > 0 && b.xLocation + b.xVelocity < getWidth()) {
                        b.xLocation += b.xVelocity*(SystemClock.currentThreadTimeMillis()-lastTimeAccelSensorChangedInMs)/10f;
                    }
                    if(b.yLocation + b.yVelocity > 0 && b.yLocation + b.yVelocity < getHeight()) {
                        b.yLocation += b.yVelocity*(SystemClock.currentThreadTimeMillis() - lastTimeAccelSensorChangedInMs)/10f;
                    }
                    points.add(new Point((int)b.xLocation, (int)b.yLocation));


                    //TODO: instead of calling this all the time, call it after the trial is done.


                }
                lastTimeAccelSensorChangedInMs = SystemClock.currentThreadTimeMillis();
                break;
        }
        invalidate();
    }

    private void score() {
        //double[] diffs = new double[points.size()-1];
        double avg = 0;
        int count = 0;
        for(int i = 1; i < points.size(); i++){
            int x1 = points.get(i).x;
            int x2 = points.get(i-1).x;

            int y1 = points.get(i).y;
            int y2 = points.get(i-1).y;
            avg += Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
            count++;
        }
        score = Math.round((avg/count)*100);
        /*TextView textView;
        Activity parentActivity = (Activity)this.getContext();

        if (parentActivity != null) {
            textView = (TextView) parentActivity.findViewById(R.id.levelScore);
            textView.setVisibility(View.VISIBLE);
            textView.setText("SCORE: " + Math.round((avg/count)*100));
        }*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private Bitmap prepareToMeasure(){

        Bitmap ic = drawableToBitmap(ContextCompat.getDrawable(getContext(), R.drawable.ic_noun_745038_cc));
        Bitmap icon = Bitmap.createScaledBitmap(ic,this.getWidth(),this.getHeight(),false);

        return icon;
    }

    private Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }




    class Ball{

        public float radius = DEFAULT_BALL_RADIUS;
        public float xLocation = 0; // center x
        public float yLocation = 0; // center y
        public float xVelocity = 10;
        public float yVelocity = 10;
        public Paint paint = new Paint();

        public Ball(float radius, float xLoc, float yLoc, float xVel, float yVel, int color){
            this.radius = radius;
            this.xLocation = xLoc;
            this.yLocation = yLoc;
            this.xVelocity = xVel;
            this.yVelocity = yVel;
            this.paint.setColor(color);
        }

        public float getRight() {
            return this.xLocation + this.radius;
        }

        public float getLeft(){
            return this.xLocation - this.radius;
        }

        public float getTop(){
            return this.yLocation - this.radius;
        }

        public float getBottom(){
            return this.yLocation + this.radius;
        }
    }
}