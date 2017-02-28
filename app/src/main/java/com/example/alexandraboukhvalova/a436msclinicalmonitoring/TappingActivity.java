package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TappingActivity extends AppCompatActivity {
    Button instructionsBtn;
    Button startTestBtn;
    Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapping);

        instructionsBtn = (Button)findViewById(R.id.InstructionsButton);
        startTestBtn = (Button)findViewById(R.id.starTestButton);
        addListenerOnButton();

        // add button listener
        instructionsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, instruction.class);
                startActivity(intent);


            }
        });
    }

    public void addListenerOnButton() {

        final Context context = this;

        startTestBtn = (Button) findViewById(R.id.starTestButton);

        startTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, TimingActivity.class);
                startActivity(intent);

            }
        });

    }


}
