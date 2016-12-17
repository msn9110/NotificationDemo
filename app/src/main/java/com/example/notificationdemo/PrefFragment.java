package com.example.notificationdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


public class PrefFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

    // 加入欄位變數宣告
    private SharedPreferences sharedPreferences;
    private CheckBoxPreference defaultNotice;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();

        addPreferencesFromResource(R.xml.settings);

        defaultNotice =  (CheckBoxPreference) findPreference("notice");
        defaultNotice.setOnPreferenceChangeListener(this);

        // 建立SharedPreferences物件
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

    }

    @Override
    public void onResume() {
        super.onResume();

        boolean alarmService = sharedPreferences.getBoolean("notice",false);
        defaultNotice.setDefaultValue(alarmService);
        defaultNotice.setChecked(alarmService);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        switch (preference.getKey()){
            case "notice":
                boolean b = !sharedPreferences.getBoolean("notice",false);
                Intent intent  = new Intent(mContext,SetAlarmService.class);
                if(b){
                    mContext.startService(intent);
                } else {
                    mContext.stopService(intent);
                }
                defaultNotice.setChecked(b);
                sharedPreferences.edit()
                        .putBoolean("notice",b)
                        .apply();
                break;
        }
        return false;
    }
}