package com.example.multipletwonumberjava;

import android.os.Handler;
import android.os.Message;


public class CalculateHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(MainActivity.COMPLETED_THREAD_POOL == msg.what){
            MainActivity.textViewResult.setText(String.valueOf(MainActivity.result));
        }
    }
}
