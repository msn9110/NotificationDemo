package com.example.notificationdemo;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class SetAlarmService extends Service {
    final static String TAG = "##SetAlarmService";
    public final static String ACTION_BROADCAST = "ACTION_MY_BROADCAST";
    final static ArrayList<Integer> alarmHour = new ArrayList<>(Arrays.asList(0, 1, 11, 14, 15, 16, 17, 18, 19, 20, 21, 22)) ;
    final static ArrayList<Integer> alarmMinute = new ArrayList<>(Arrays.asList(0, 5, 10, 15, 20, 30, 45, 50)) ;
    Thread doInBackground;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
        if(doInBackground == null){
            doInBackground = new Thread(runnable);
            doInBackground.start();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
        isContinue = false;
        try {
            doInBackground.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    boolean isContinue = true;
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            while (isContinue){
                Calendar cal = Calendar.getInstance();
                // 設定於 1 min 後執行
                cal.add(Calendar.MINUTE, 1);
                int alarmH = cal.get(Calendar.HOUR_OF_DAY);
                int alarmMin = cal.get(Calendar.MINUTE);
                int seconds = cal.get(Calendar.SECOND);
                //Log.d(TAG, String.valueOf(alarmH) + ":" +String.valueOf(alarmMin));
                if(alarmHour.contains(alarmH) && alarmMinute.contains(alarmMin) && seconds == 0){
                    Log.d(TAG, "SetAlarm");
                    Intent intent = new Intent(ACTION_BROADCAST);

                    PendingIntent pi = PendingIntent.getBroadcast(SetAlarmService.this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

                    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

}
