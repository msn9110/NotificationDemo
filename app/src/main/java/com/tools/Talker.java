package com.tools;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;


public class Talker implements TextToSpeech.OnInitListener {

    TextToSpeech mTTs;
    private static final String TAG = "##Talker";

    public Talker(Context context){
        mTTs = new TextToSpeech(context, this);
    }

    // Implements TextToSpeech.OnInitListener.
    public void onInit(int status) {
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            language();
        }

        else {
            Log.e(TAG, "Could not initialize TextToSpeech.");
        }
    }

    private void language(){
        float speed=(float)0.8;
        int result;
        result = mTTs.setLanguage(Locale.TAIWAN);//<<<===================================
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            String error = ((result == TextToSpeech.LANG_MISSING_DATA)?"Missing Lang Data":"Lang Not Supported");
            Log.d(TAG, "Voice Setting Error : " + error);
        }
        else{
            mTTs.setSpeechRate(speed);
            Log.d(TAG, "Voice Ready");
        }

    }

    public void talk(String msg){
        mTTs.speak(msg,TextToSpeech.QUEUE_FLUSH,null);
    }
}
