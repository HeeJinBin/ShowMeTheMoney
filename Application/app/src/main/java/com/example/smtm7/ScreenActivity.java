package com.example.smtm7;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smtm7.AirButton.FloatingActivity;
import com.example.smtm7.DataBase.SharedPreferenceBase;

public class ScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //시작 전에 splash 화면 띄우기

        SharedPreferenceBase sharedPreferenceBase = new SharedPreferenceBase(this);
        if(sharedPreferenceBase.getString("access").equals("")){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            //여기서 본인 인증으로 넘어가게

            Intent intent = new Intent(this, FloatingActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
