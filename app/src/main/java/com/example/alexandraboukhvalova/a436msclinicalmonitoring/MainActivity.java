package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.os.*;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class MainActivity extends AppCompatActivity {

    Button tappingActivityBtn;
    Button spiralActivityBtn;
    Button resultsActivityBtn;
    Button levelActivityBtn;
    Button bubbleActivityBtn;
    Button armCurlActivityBtn;
    Button stepsB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        addListenerOnButton();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


    public void addListenerOnButton() {

        final Context context = this;

        tappingActivityBtn = (Button) findViewById(R.id.TappingActivityButton);
        spiralActivityBtn = (Button) findViewById(R.id.SpiralActivityButton);
        resultsActivityBtn = (Button) findViewById(R.id.ResultsActivityButton);
        levelActivityBtn = (Button) findViewById(R.id.LevelActivityButton);
        bubbleActivityBtn = (Button) findViewById(R.id.BubbleActivityButton);
        armCurlActivityBtn = (Button) findViewById(R.id.ArmCurlActivityButton);
        stepsB=(Button) findViewById(R.id.steps);

        stepsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("steps Test Instructions");
                builder.setMessage("first choose your sex from the menu that appear after you click ok then start to walk");
                builder.setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(context, Speed.class);
                                startActivity(intent);



                            }
                        }, 50);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        tappingActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Tap Test Instructions");
                builder.setMessage("When the countdown completes, use your LEFT hand to tap the box" +
                        " as many times as possible within 10 seconds. After the first round ends, repeat this " +
                        "process with your RIGHT hand.");
                builder.setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(context, TappingActivity.class);
                                startActivity(intent);
                            }
                        }, 50);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        spiralActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Spiral Test Instructions");
                builder.setMessage("When the countdown completes, use your LEFT hand to trace the spiral" +
                        " that appears on the screen. After the first round ends, repeat this " +
                        "process with your RIGHT hand.");
                builder.setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(context, SpiralActivity.class);
                                startActivity(intent);
                            }
                        }, 50);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        levelActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Level Test Instructions");
                builder.setMessage("When the countdown completes, use your LEFT hand to keep the ball" +
                        " in the center of the bullseys that appears on the screen. After the first round ends, repeat this " +
                        "process with your RIGHT hand.");
                builder.setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(context, LevelActivity.class);
                                startActivity(intent);
                            }
                        }, 50);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        // copied directly from level activity button above
        bubbleActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Bubble Test Instructions");
                builder.setMessage("Try to hit as many bubbles as you can once the test starts" );
                builder.setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(context, BubbleActivity.class);
                                startActivity(intent);
                            }
                        }, 50);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        armCurlActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Arm Curl Test Instructions");
                builder.setMessage("Hold your arm straight out. After you hit start, tap your " +
                        "shoulder and bring your arm back out at the same level ten times." );
                builder.setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(context, ArmCurlActivity.class);
                                startActivity(intent);
                            }
                        }, 50);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        resultsActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Test Results");
                builder.setMessage("This page contains the results for the most recent set of tests");
                builder.setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                /* Fill in correct activity once class name is known
                                Intent intent = new Intent(context, ResultsActivity.class);
                                startActivity(intent);
                                */
                            }
                        }, 50);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
