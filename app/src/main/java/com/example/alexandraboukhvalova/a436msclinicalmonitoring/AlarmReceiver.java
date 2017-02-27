package com.example.alexandraboukhvalova.a436msclinicalmonitoring;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;


public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Only create notification if last completed is not set to the current day
        if (TimingActivity.getLastCompleted() != (int) Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH)) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //if we want sound on notification then uncomment below line//
            // Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context).
                    setSmallIcon(R.drawable.tapping).
                    setContentIntent(pendingIntent).
                    setContentText("TEST REMINDER").
                    setContentTitle("You have tests to take").
            //      setSound(alarmSound).
                    setAutoCancel(true);
            notificationManager.notify(100, builder.build());
        }

    }



}
