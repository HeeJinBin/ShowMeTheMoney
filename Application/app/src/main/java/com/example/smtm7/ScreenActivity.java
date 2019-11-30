package com.example.smtm7;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smtm7.DataBase.DBEmailAdapter;
import com.example.smtm7.DataBase.SharedPreferenceBase;

public class ScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //시작 전에 splash 화면 띄우기

        SharedPreferenceBase sharedPreferenceBase = new SharedPreferenceBase(this);

        if(sharedPreferenceBase.getString("access").equals("")){
            sharedPreferenceBase.setString("login", "False");
            sharedPreferenceBase.setString("airbutton","True");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else{
            if(sharedPreferenceBase.getString("login").equals("True")) {
                Intent intent = new Intent(this, Certification.class);
                startActivity(intent);
                finish();
            } else{
                DBEmailAdapter emailAdapter = new DBEmailAdapter(this);
                emailAdapter.open();
                emailAdapter.deleteAllEmail();
                emailAdapter.close();

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
