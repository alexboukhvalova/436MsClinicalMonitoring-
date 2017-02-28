package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class LevelInstructions extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_instructions);
        final Context context = this;

        final TextView tv=(TextView)findViewById(R.id.textView1);


        tv.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(context, LevelActivity.class);
                startActivity(intent);

            }
        }, 7000);

    }
}
