package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TappingActivity extends AppCompatActivity {
    Button instructionsBtn;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapping);

        instructionsBtn = (Button)findViewById(R.id.InstructionsButton);

        // add button listener
        instructionsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Your Title");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Click ok to exit!")
                        .setCancelable(false)
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
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


}
