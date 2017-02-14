package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SpiralTest extends AppCompatActivity {
    Button startTestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spiral_teat);

        startTestBtn = (Button)findViewById(R.id.starTestButton);
        addListenerOnButton();

    }
    public void addListenerOnButton() {

        final Context context = this;

        startTestBtn = (Button) findViewById(R.id.starTestButton);

        startTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);

            }
        });

    }

}
