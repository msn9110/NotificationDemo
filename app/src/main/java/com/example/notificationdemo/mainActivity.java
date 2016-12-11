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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class mainActivity extends AppCompatActivity implements View.OnClickListener {

    final static String TAG = "##mainActivity";
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
        Switch sw = (Switch) findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Intent intent = new Intent(mainActivity.this,SetAlarmService.class);
//                Log.d(TAG, intent.getAction());
                if(b){
                    startService(intent);
                } else {
                    stopService(intent);
                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:

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
