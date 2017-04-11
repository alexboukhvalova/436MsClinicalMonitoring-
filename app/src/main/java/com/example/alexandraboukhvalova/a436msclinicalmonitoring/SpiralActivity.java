package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import edu.umd.cmsc436.sheets.Sheets;


public class SpiralActivity extends AppCompatActivity implements  Sheets.Host {

    public static final int LIB_ACCOUNT_NAME_REQUEST_CODE = 1001;
    public static final int LIB_AUTHORIZATION_REQUEST_CODE = 1002;
    public static final int LIB_PERMISSION_REQUEST_CODE = 1003;
    public static final int LIB_PLAY_SERVICES_REQUEST_CODE = 1004;

    private Sheets sheet;
    private String spreadsheetId = "1jus0ktF2tQw2sOjsxVb4zoDeD1Zw90KAMFNTQdkFiJQ";

    private DrawingView drawView;
    private Button newBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spiral);
        drawView = (DrawingView)findViewById(R.id.drawing);

        sheet = new Sheets(this, getResources().getString(R.string.app_name), spreadsheetId);


        newBtn = (Button)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                drawView.startNew();
            }
        });

    }

    protected void sendToSheets(float score) {
        Sheets.TestType type = Sheets.TestType.LH_SPIRAL;
        //TODO: CHANGE THIS TO MATCH YOURS
        String userId = "t04p06";

        sheet.writeData(type, userId, score);
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        sheet.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sheet.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int getRequestCode(Sheets.Action action) {
        switch (action) {
            case REQUEST_ACCOUNT_NAME:
                return LIB_ACCOUNT_NAME_REQUEST_CODE;
            case REQUEST_AUTHORIZATION:
                return LIB_AUTHORIZATION_REQUEST_CODE;
            case REQUEST_PERMISSIONS:
                return LIB_PERMISSION_REQUEST_CODE;
            case REQUEST_PLAY_SERVICES:
                return LIB_PLAY_SERVICES_REQUEST_CODE;
            default:
                return -1;
        }
    }
    @Override
    public void notifyFinished(Exception e) {
        if (e != null) {
            throw new RuntimeException(e);
        }
        Log.i(getClass().getSimpleName(), "Done");
    }

}
