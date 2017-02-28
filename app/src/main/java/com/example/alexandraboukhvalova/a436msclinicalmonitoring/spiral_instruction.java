package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class spiral_instruction extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_spiral_instruction);

        final TextView tv=(TextView)findViewById(R.id.textView1);


        tv.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(context, SpiralActivity.class);
                startActivity(intent);

            }
        }, 7000);

    }
}
