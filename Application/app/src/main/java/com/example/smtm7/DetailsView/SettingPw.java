package com.example.smtm7.DetailsView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smtm7.DataBase.SharedPreferenceBase;
import com.example.smtm7.R;

import java.util.ArrayList;

public class SettingPw extends AppCompatActivity {

    private TextView text_pw;
    private TextView text_pw_info;

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
    private Button buttonBack;

    private ArrayList<Integer> pw;
    private ArrayList<Integer> pwCheck;

    private int mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pw);

        mode = 1;
        pw = new ArrayList<>(6);
        pwCheck = new ArrayList<>(6);

        text_pw = findViewById(R.id.tv_pw_setting_top);
        text_pw_info = findViewById(R.id.tv_pw_setting_info);

        imagePw1 = findViewById(R.id.btn_pw_setting_1);
        imagePw2 = findViewById(R.id.btn_pw_setting_2);
        imagePw3 = findViewById(R.id.btn_pw_setting_3);
        imagePw4 = findViewById(R.id.btn_pw_setting_4);
        imagePw5 = findViewById(R.id.btn_pw_setting_5);
        imagePw6 = findViewById(R.id.btn_pw_setting_6);

        button0 = findViewById(R.id.button_setting_0);
        button1 = findViewById(R.id.button_setting_1);
        button2 = findViewById(R.id.button_setting_2);
        button3 = findViewById(R.id.button_setting_3);
        button4 = findViewById(R.id.button_setting_4);
        button5 = findViewById(R.id.button_setting_5);
        button6 = findViewById(R.id.button_setting_6);
        button7 = findViewById(R.id.button_setting_7);
        button8 = findViewById(R.id.button_setting_8);
        button9 = findViewById(R.id.button_setting_9);
        buttonErase = findViewById(R.id.button_setting_cancel);
        buttonClear = findViewById(R.id.button_setting_x);
        buttonBack = findViewById(R.id.pw_setting_back);

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
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton();
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
        if(mode == 1) {
            pw.add(num);
            setImageView(pw.size(), true);

            // 비밀번호 6자리 입력 완료시 확인 화면으로 이동
            if (pw.size() == 6) {
                SharedPreferenceBase sharedPreferenceBase = new SharedPreferenceBase(this);
                String password = "";

                for (int i = 0; i < 6; i++) {
                    password += pw.get(i);
                }

                if (password.equals(sharedPreferenceBase.getString("pin"))) {
                    changeMode(2);
                } else {
                    clearPw();
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        } else if(mode == 2){
            pw.add(num);
            setImageView(pw.size(), true);

            // 비밀번호 6자리 입력 완료시 확인 화면으로 이동
            if (pw.size() == 6) {
                changeMode(3);
            }
        } else{
            pwCheck.add(num);
            setImageView(pwCheck.size(), true);

            if(pwCheck.size() == 6){
                SharedPreferenceBase sharedPreferenceBase = new SharedPreferenceBase(this);
                String password = "";
                String check = "";

                for (int i = 0; i < 6; i++) {
                    password += pw.get(i);
                    check += pwCheck.get(i);
                }

                if (password.equals(check)) {
                    sharedPreferenceBase.setString("pin", password);
                    Toast.makeText(getApplicationContext(), "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    changeMode(2);
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
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
        if(mode == 3)
            pwCheck.clear();
        else{
            pw.clear();
            pwCheck.clear();
        }

        for(int i=1;i<=6;i++){
            setImageView(i,false);
        }
    }

    public void changeMode(int num){
        mode = num;
        clearPw();
        switch (num){
            case 2:
                text_pw.setText("비밀번호");
                text_pw_info.setText("현재 기기에서 사용할 비밀번호 6자리를 입력해주세요.");
                break;
            case 3:
                text_pw.setText("비밀번호 재입력");
                text_pw_info.setText("비밀번호를 한번 더 입력해주세요.");
                break;
        }
    }

    public void backButton(){
        switch (mode){
            case 1:
            case 2:
                finish();
                break;
            case 3:
                changeMode(2);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DetailsActivity.screenCheck = true;
    }
}
