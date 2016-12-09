package com.example.notificationdemo;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class mainActivity extends AppCompatActivity implements View.OnClickListener{


    private TService.MyBinder myBinder;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (TService.MyBinder) service;
            myBinder.startDownload();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btnNotice = (Button)findViewById(R.id.button);
        btnNotice.setOnClickListener(this);
        Button btnStart = (Button)findViewById(R.id.startService);
        btnStart.setOnClickListener(this);
        Button btnStop = (Button)findViewById(R.id.stopService);
        btnStop.setOnClickListener(this);
        Button bindService = (Button) findViewById(R.id.bind_service);
        Button unbindService = (Button) findViewById(R.id.unbind_service);
        bindService.setOnClickListener(this);
        unbindService.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                final int notifyID = 3; // 通知的識別號碼
                final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
                // 建立通知
                final Notification notification = new Notification.Builder(getApplicationContext())
                                                                .setSmallIcon(R.mipmap.ic_launcher)
                                                                .setContentTitle("內容標題")
                                                                .setContentText("內容文字").build();
                notificationManager.notify(notifyID, notification); // 發送通知
                Calendar cal = Calendar.getInstance(Locale.getDefault());
                System.out.println(cal.getTime().toString());
                Date date = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss",Locale.getDefault());
                String current = df.format(date);
                System.out.println(current);
                break;

            case R.id.startService:
                Intent intent = new Intent(this,TService.class);
                startService(intent);
                break;

            case R.id.stopService:
                intent = new Intent(this,TService.class);
                stopService(intent);
                break;

            case R.id.bind_service:
                Intent bindIntent = new Intent(this, TService.class);
                bindService(bindIntent, connection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                unbindService(connection);
                break;
        }
    }
}
