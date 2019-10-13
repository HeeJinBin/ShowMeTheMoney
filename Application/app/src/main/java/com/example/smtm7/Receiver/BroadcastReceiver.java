package com.example.smtm7.Receiver;

import android.content.Context;
import android.content.Intent;

import com.example.smtm7.AirButton.FloatingActivity;
import com.example.smtm7.LoginActivity;

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action.equals(Intent.ACTION_PACKAGE_ADDED)){
            //앱이 설치되었을 때
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);

        } else if(action.equals(intent.ACTION_BOOT_COMPLETED)){
            // 앱이 부팅되었을 때
            Intent i = new Intent(context, FloatingActivity.class);
            context.startService(i);
        }
    }
}
