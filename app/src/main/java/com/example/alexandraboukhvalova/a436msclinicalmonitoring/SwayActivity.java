package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SwayActivity extends AppCompatActivity {

    private Button startBtn;
    private TextView instr_text;
    private AnimationView animationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sway);

        animationView = (AnimationView) findViewById(R.id.level_path);

        final Context context = this;
        instr_text = (TextView) findViewById(R.id.instruction_text);
        instr_text.setVisibility(View.INVISIBLE);
        startBtn = (Button) findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // user will lift device to head in preparation for trial start at this time
                startBtn.setVisibility(View.INVISIBLE);
                instr_text.setVisibility(View.VISIBLE);

                Runnable r = new Runnable() {
                    @Override
                    public void run(){
                        instr_text.setVisibility(View.INVISIBLE);
                        beepSound();
                        Intent intent = new Intent(context, LevelActivity.class);
                        startActivity(intent);

                    }
                };

                Handler h = new Handler();
                h.postDelayed(r, 3000);
            }
        });
    }



    private void beepSound() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
