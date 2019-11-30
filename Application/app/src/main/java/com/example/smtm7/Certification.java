package com.example.smtm7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smtm7.AirButton.FloatingActivity;
import com.example.smtm7.DataBase.SharedPreferenceBase;
import com.example.smtm7.DetailsView.DetailsActivity;

import java.util.ArrayList;

public class Certification extends AppCompatActivity {
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
    private Button buttonReset;

    private ArrayList<Integer> pw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification);

        pw = new ArrayList<>(6);

        imagePw1 = findViewById(R.id.btn_pw_1_first);
        imagePw2 = findViewById(R.id.btn_pw_2_first);
        imagePw3 = findViewById(R.id.btn_pw_3_first);
        imagePw4 = findViewById(R.id.btn_pw_4_first);
        imagePw5 = findViewById(R.id.btn_pw_5_first);
        imagePw6 = findViewById(R.id.btn_pw_6_first);

        button0 = findViewById(R.id.button_0_first);
        button1 = findViewById(R.id.button_1_first);
        button2 = findViewById(R.id.button_2_first);
        button3 = findViewById(R.id.button_3_first);
        button4 = findViewById(R.id.button_4_first);
        button5 = findViewById(R.id.button_5_first);
        button6 = findViewById(R.id.button_6_first);
        button7 = findViewById(R.id.button_7_first);
        button8 = findViewById(R.id.button_8_first);
        button9 = findViewById(R.id.button_9_first);
        buttonErase = findViewById(R.id.button_cancel_first);
        buttonClear = findViewById(R.id.button_x_first);
//        buttonReset = findViewById(R.id.btn_pw_reset);

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

//        // 비밀번호 재설정
//        buttonReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

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
        pw.add(num);
        setImageView(pw.size(),true);

        // 비밀번호 6자리 입력 확인
        if(pw.size()==6){
            SharedPreferenceBase sharedPreferenceBase = new SharedPreferenceBase(this);
            String password = "";

            for(int i=0;i<6;i++){
                password += pw.get(i);
            }

            if(password.equals(sharedPreferenceBase.getString("pin"))){
                if(sharedPreferenceBase.getString("airbutton").equals("True")) {
                    Intent intent = new Intent(this, FloatingActivity.class);
                    startActivity(intent);
                    finish();
                } else{
                    Intent intent = new Intent(this, DetailsActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else{
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                clearPw();
            }
        }
    }

    public void erasePw(){
        int eraseItem = pw.size();

        if(eraseItem != 0){
            setImageView(eraseItem,false);
            pw.remove(eraseItem-1);
        }
    }

    public void clearPw(){
        pw.clear();

        for(int i=1;i<=6;i++){
            setImageView(i,false);
        }
    }
}
