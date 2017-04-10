package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import static android.content.Context.SENSOR_SERVICE;
import android.widget.TextView;

import org.w3c.dom.Text;


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
        _paintTrail.setStyle(Paint.Style.STROKE);
        _paintTrail.setAntiAlias(true);

        _paintText.setColor(Color.BLACK);
        _paintText.setTextSize(40f);

        //this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);

        //text = (TextView) findViewById(R.id.text);


        // https://developer.android.com/referecance/java/util/Timer.html#scheduleAtFixedRate(java.util.TimerTask, long, long)
        // 60 fps will have period of 16.67
        // 40 fps will have period of 25
        //long periodInMillis = 1000 / _desiredFramesPerSecond;
        //_timer.schedule(new AnimationTimerTask(this), 0, periodInMillis);
    }





    @Override
    public void onDraw(Canvas canvas) {
        // start time helps measure fps calculations
        if(_startTime == -1) {
            _startTime = SystemClock.elapsedRealtime();
        }
        _frameCnt++;

        super.onDraw(canvas);

        synchronized (this.balls) {
            for(Ball ball : this.balls){
                canvas.drawCircle(ball.xLocation, ball.yLocation, ball.radius, ball.paint);
            }
        }

       // canvas.drawCircle(3*getWidth()/8,3*getWidth()/8,40,_paintText);
       // canvas.drawCircle(getWidth()/4,getWidth()/4,40,_paintText);

        // The code below is about measuring and printing out fps calculations. You can ignore
        long endTime = SystemClock.elapsedRealtime();
        long elapsedTimeInMs = endTime - _startTime;
        if(elapsedTimeInMs > 1000){
            _actualFramesPerSecond = _frameCnt / (elapsedTimeInMs/1000f);
            _frameCnt = 0;
            _startTime = -1;
        }

        Path path = new Path();
        Paint paint = new Paint();
        boolean first = true;


        for(Point point : points){
            if(first){
                first = false;
                path.moveTo(point.x, point.y);
            }
            else{
                path.lineTo(point.x, point.y);

            }
        }




        canvas.drawPath(path, paint);

        //MessageFormat: https://developer.android.com/reference/java/text/MessageFormat.html
        //canvas.drawText(MessageFormat.format("fps: {0,number,#.#}", _actualFramesPerSecond), 5, 40, _paintText);
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        long startTime = SystemClock.elapsedRealtime();

        float curTouchX = motionEvent.getX();
        float curTouchY = motionEvent.getY();

        scaled_pic = prepareToMeasure();

        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                /*float randomXVelocity = _random.nextFloat() * MAX_VELOCITY;
                float randomYVelocity = randomXVelocity;*/

                //setup random direction
                //randomXVelocity = _random.nextFloat() < 0.5f ? randomXVelocity : -1 * randomXVelocity;
                //randomYVelocity = _random.nextFloat() < 0.5f ? randomYVelocity : -1 * randomYVelocity;

                float randomXVelocity = 0;
                float randomYVelocity = 0;

                Ball ball = new Ball(DEFAULT_BALL_RADIUS, curTouchX, curTouchY, randomXVelocity, randomYVelocity, Color.argb(255, 10, 10, 10));
                points.add(new Point((int) curTouchX, (int)curTouchY));
                synchronized (this.balls) {
                    if (this.balls.size() < 1) {
                        this.balls.add(ball);
                    }

                }

            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
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
                    score();

                }
                lastTimeAccelSensorChangedInMs = SystemClock.currentThreadTimeMillis();
                break;
        }
        invalidate();
    }

    private void score() {
        double[] diffs = new double[points.size()-1];
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
        TextView textView;
        Activity parentActivity = (Activity)this.getContext();
        if (parentActivity != null) {
            textView = (TextView) parentActivity.findViewById(R.id.levelScore);
            textView.setVisibility(View.VISIBLE);
            //textView.setText("SCORE: " + Math.round((avg/count)*100));
            textView.setText("SCORE: " + points.size()/100);
        }
        /*
        Collections.sort( points, new Comparator<Point>() {
            public int compare(Point x1, Point x2) {
                int result = Double.compare(x1.x, x2.x);
                if ( result == 0 ) {
                    result = Double.compare(x1.y, x2.y);
                }
                return result;
            }
        });
        int len = points.size();
        int x,y;
        if(len % 2 == 1){
            x = points.get(len/2).x;
            y = points.get(len/2).y;

        } else {
            int x1 = points.get((int)Math.floor(len/2.0)).x;
            int x2 = points.get((int)Math.ceil(len/2.0)).x;
            int y1 = points.get((int)Math.floor(len/2.0)).y;
            int y2 = points.get((int)Math.ceil(len/2.0)).y;
            x = (x1+x2)/2;
            y = (y1+y2)/2;
        }
        median = new Point(x,y);
        //Log.i("Median",x+" "+y);
        //Log.i("Center",getWidth()/2+" "+getWidth()/2);
        String grade = "-";

        double dist = Math.sqrt(Math.pow(x-getWidth(),2) + Math.pow(y-getWidth(),2));
        Log.i("Location",dist + " " +3*getWidth()/8 + " " + getWidth()/4+" ");
        if(dist < 3*getWidth()/8){
            Log.i("Location","blue");
            grade = "A";
        } else if(dist < getWidth()/4){
            Log.i("Location","red");
            grade = "B";
        } else {
            grade = "C";
        }
        TextView textView;
        Activity parentActivity = (Activity)this.getContext();
        if (parentActivity != null) {
            textView = (TextView) parentActivity.findViewById(R.id.levelScore);
            textView.setVisibility(View.VISIBLE);
            textView.setText("SCORE: " + grade);
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

    //TimerTask: https://developer.android.com/reference/java/util/TimerTask.html
    /*
    class AnimationTimerTask extends TimerTask {

        private AnimationView _animationView;
        private long _lastTimeInMs = -1;

        public AnimationTimerTask(AnimationView animationView){
            _animationView = animationView;
        }

        @Override
        public void run() {
            if(_lastTimeInMs == -1){
                _lastTimeInMs = SystemClock.elapsedRealtime();
            }
            long curTimeInMs = SystemClock.elapsedRealtime();

            synchronized (_animationView.balls){
                for(Ball ball : _animationView.balls){
                    ball.xLocation += ball.xVelocity * (curTimeInMs - _lastTimeInMs)/1000f;
                    ball.yLocation += ball.yVelocity * (curTimeInMs - _lastTimeInMs)/1000f;

                    // check x ball boundary
                    if(ball.getRight() >= getWidth() || ball.getLeft() <= 0){
                        // switch directions
                        ball.xVelocity = -1 * ball.xVelocity;
                    }

                    // check y ball boundary
                    if(ball.getBottom() >= getHeight() || ball.getTop() <= 0){
                        // switch directions
                        ball.yVelocity = -1 * ball.yVelocity;
                    }

                }



            }


            _animationView.postInvalidate();
            _lastTimeInMs = curTimeInMs;
        }
    }
    */
}