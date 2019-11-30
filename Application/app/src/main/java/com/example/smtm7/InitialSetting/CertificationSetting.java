package com.example.smtm7.InitialSetting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smtm7.AirButton.FloatingActivity;
import com.example.smtm7.DataBase.SharedPreferenceBase;
import com.example.smtm7.R;

import java.util.ArrayList;

public class CertificationSetting extends AppCompatActivity {

    private Button backButton;
    private TextView textViewPw;
    private TextView textViewInfo;

    private ImageView imagePw1;
    private ImageView imagePw2;
    private ImageView imagePw3;
    private ImageView imagePw4;
    private ImageView imagePw5;
    private ImageView imagePw6;

    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button buttonClear;
    private Button buttonErase;

    private ArrayList<Integer> pw;
    private ArrayList<Integer> pwCheck;

    private int mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification_setting);

        mode = 1;
        pw = new ArrayList<>(6);
        pwCheck = new ArrayList<>(6);

        backButton = findViewById(R.id.certification_back);
        textViewPw = findViewById(R.id.tv_pw_top);
        textViewInfo = findViewById(R.id.tv_pw_info);

        backButton.setVisibility(View.GONE);
        backButton.setClickable(false);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMode();
            }
        });

        imagePw1 = findViewById(R.id.btn_pw_1);
        imagePw2 = findViewById(R.id.btn_pw_2);
        imagePw3 = findViewById(R.id.btn_pw_3);
        imagePw4 = findViewById(R.id.btn_pw_4);
        imagePw5 = findViewById(R.id.btn_pw_5);
        imagePw6 = findViewById(R.id.btn_pw_6);

        button0 = findViewById(R.id.button_0);
        button1 = findViewById(R.id.button_1);
        button2 = findViewById(R.id.button_2);
        button3 = findViewById(R.id.button_3);
        button4 = findViewById(R.id.button_4);
        button5 = findViewById(R.id.button_5);
        button6 = findViewById(R.id.button_6);
        button7 = findViewById(R.id.button_7);
        button8 = findViewById(R.id.button_8);
        button9 = findViewById(R.id.button_9);
        buttonErase = findViewById(R.id.button_cancel);
        buttonClear = findViewById(R.id.button_x);

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePw(0);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePw(1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePw(2);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePw(3);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePw(4);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePw(5);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePw(6);
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePw(7);
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePw(8);
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePw(9);
            }
        });
        buttonErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                erasePw();
            }
        });
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPw();
            }
        });
    }

    public void setImageView(int num, boolean check){
        switch (num){
            case 1:
                if(check)
                    imagePw1.setImageResource(R.drawable.password_fill);
                else
                    imagePw1.setImageResource(R.drawable.password);
                break;
            case 2:
                if(check)
                    imagePw2.setImageResource(R.drawable.password_fill);
                else
                    imagePw2.setImageResource(R.drawable.password);
                break;
            case 3:
                if(check)
                    imagePw3.setImageResource(R.drawable.password_fill);
                else
                    imagePw3.setImageResource(R.drawable.password);
                break;
            case 4:
                if(check)
                    imagePw4.setImageResource(R.drawable.password_fill);
                else
                    imagePw4.setImageResource(R.drawable.password);
                break;
            case 5:
                if(check)
                    imagePw5.setImageResource(R.drawable.password_fill);
                else
                    imagePw5.setImageResource(R.drawable.password);
                break;
            case 6:
                if(check)
                    imagePw6.setImageResource(R.drawable.password_fill);
                else
                    imagePw6.setImageResource(R.drawable.password);
                break;
        }
    }

    public void updatePw(int num){
        if(mode == 1){
            pw.add(num);
            setImageView(pw.size(),true);

            if(pw.size() == 6)
                changeMode();
        } else {
            pwCheck.add(num);
            setImageView(pwCheck.size(), true);

            if (pwCheck.size() == 6) {
                String password = "";
                String check = "";

                for (int i = 0; i < 6; i++) {
                    password += pw.get(i);
                    check += pwCheck.get(i);
                }

                if (check.equals(password)) {
                    SharedPreferenceBase sharedPreferenceBase = new SharedPreferenceBase(this);
                    sharedPreferenceBase.setString("pin", password);
                    Log.d("PIN", password);

                    Intent intent = new Intent(this, FloatingActivity.class);
                    startActivity(intent);
                    sharedPreferenceBase.setString("login", "True");
                    finish();
                } else {
                    //다르면 다시 앞으로 이동
                    changeMode();
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void erasePw(){
        if(mode == 1){
            int eraseItem = pw.size();

            if (eraseItem != 0) {
                setImageView(eraseItem, false);
                pw.remove(eraseItem - 1);
            }
        } else {
            int eraseItem = pwCheck.size();

            if (eraseItem != 0) {
                setImageView(eraseItem, false);
                pwCheck.remove(eraseItem - 1);
            }
        }
    }

    public void clearPw(){
        if(mode == 1){
            pw.clear();
            pwCheck.clear();
        } else{
            pwCheck.clear();
        }

        for(int i=1;i<=6;i++){
            setImageView(i,false);
        }
    }

    public void changeMode(){
        if(mode == 1){
            mode = 2;

            backButton.setVisibility(View.VISIBLE);
            backButton.setClickable(true);
            textViewPw.setText("비밀번호 재입력");
            textViewInfo.setText("비밀번호를 한번 더 입력해주세요.");
        } else{
            mode = 1;

            backButton.setVisibility(View.GONE);
            backButton.setClickable(false);
            textViewPw.setText("비밀번호");
            textViewInfo.setText("현재 기기에서 사용할 비밀번호 6자리를 입력해주세요.");
        }
        clearPw();
    }
}
