package com.example.notificationdemo;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.gerli.handsomeboy.gerlisqlitedemo.GerliDatabaseManager;

import java.util.Calendar;

public class mainActivity extends AppCompatActivity implements View.OnClickListener {

    final static String TAG = "##mainActivity";
    GerliDatabaseManager gerliDatabaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        gerliDatabaseManager=new GerliDatabaseManager(this);
        Button btnNotice = (Button)findViewById(R.id.button);
        btnNotice.setOnClickListener(this);
        setPreference();

    }

    private void setPreference(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean b = sharedPrefs.getBoolean("notice",true);
        Intent intent = new Intent(mainActivity.this,SetAlarmService.class);
        if(b){
            startService(intent);
        } else {
            stopService(intent);
        }
    }

    private static final int RESULT_SETTINGS = 1;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_settings:
                Intent i = new Intent(this, settingActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                break;

        }

        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                /*
                final int notifyID = 1;
                final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
                // 建立通知
                final Notification notification = new Notification.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("NotificationDemo")
                        .setContentText("該記帳囉").build();
                notificationManager.notify(notifyID, notification); // 發送通知
                */
                Calendar calendar = Calendar.getInstance();
                //TODAY in long represent
                calendar.clear(Calendar.MILLISECOND);
                calendar.clear(Calendar.SECOND);
                calendar.clear(Calendar.MINUTE);
                calendar.clear(Calendar.HOUR_OF_DAY);
                System.out.println(calendar.getTime().toString());
                //TODAY MORNING in long represent
                calendar.set(Calendar.HOUR_OF_DAY,22);
                System.out.println(calendar.getTime().toString());
                Log.d(TAG,gerliDatabaseManager.getLatestRecordTime().toString());
                break;
        }
    }
}
