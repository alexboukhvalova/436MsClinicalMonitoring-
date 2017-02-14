package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.os.*;



public class MainActivity extends AppCompatActivity {

    Button tappingActivityBtn;
    Button spiralButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addListenerOnButton();
    }


    public void addListenerOnButton() {

        final Context context = this;

        tappingActivityBtn = (Button) findViewById(R.id.TappingActivityButton);

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
        spiralButton=(Button) findViewById(R.id.spiralActivityButton);
        spiralButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Spiral Test Instructions");
                builder.setMessage("for this test, you need to draw a Spiral you can the example  " +
                        "once you are ready click okay");
                builder.setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(context, Spiral.class);
                                startActivity(intent);
                            }
                        }, 50);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
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
