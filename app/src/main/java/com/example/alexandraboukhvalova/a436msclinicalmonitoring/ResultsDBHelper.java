package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ResultsDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "results_database";
    public static final String RESULTS_TABLE_NAME = "results";
    public static final String RESULTS_COLUMN_ID = "_id";
    public static final String RESULTS_COLUMN_DATE = "date";
    public static final String RESULTS_COLUMN_TYPE = "type";
    public static final String RESULTS_COLUMN_HAND = "hand";
    public static final String RESULTS_COLUMN_COUNT = "count";

    public ResultsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + RESULTS_TABLE_NAME + " (" +
                RESULTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RESULTS_COLUMN_DATE + " TEXT, " +
                RESULTS_COLUMN_TYPE + " TEXT, " +
                RESULTS_COLUMN_HAND + " TEXT, " +
                RESULTS_COLUMN_COUNT + " INT UNSIGNED" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RESULTS_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
