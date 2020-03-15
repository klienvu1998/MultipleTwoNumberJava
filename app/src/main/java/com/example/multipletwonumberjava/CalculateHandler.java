package com.example.multipletwonumberjava;

import android.os.Handler;
import android.os.Message;

import java.text.DecimalFormat;


public class CalculateHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(MainActivity.COMPLETED_THREAD_POOL == msg.what){
            DecimalFormat dF = new DecimalFormat("###,###.###");
            MainActivity.textViewResult.setText(String.valueOf(dF.format(MainActivity.result)));
        }
    }
}
