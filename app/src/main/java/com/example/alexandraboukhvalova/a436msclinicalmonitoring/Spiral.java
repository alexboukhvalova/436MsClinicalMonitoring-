package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Spiral extends AppCompatActivity {

    Button instructionsBtn;
    Button startTestBtn;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spiral);

        instructionsBtn = (Button)findViewById(R.id.InstructionsButton);
        startTestBtn = (Button)findViewById(R.id.starTestButton);
        addListenerOnButton();

        // add button listener
        instructionsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Spiral Test Instructions");

                // set dialog message
                alertDialogBuilder
                        .setMessage("for this teset, you need to draw a Spiral you can the example  " +
                                "once you are ready click okay")
                        .setCancelable(false)
                        .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
    }
    public void addListenerOnButton() {

        final Context context = this;

        startTestBtn = (Button) findViewById(R.id.starTestButton);

        startTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, SpiralTest.class);
                startActivity(intent);

            }
        });

    }

}
