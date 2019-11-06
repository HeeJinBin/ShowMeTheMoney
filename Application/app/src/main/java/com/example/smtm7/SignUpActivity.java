package com.example.smtm7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smtm7.Connection.ApiService;
import com.example.smtm7.Connection.NetworkHelper;
import com.example.smtm7.Connection.ResponseSignin;

import java.util.HashMap;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    //아이디 정규식
    private static final Pattern ID_PATTERN = Pattern.compile("^[a-zA-Z]{1}[a-zA-Z0-9_]{4,19}$");
    //비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{6,20}$");

    private EditText edittextName;
    private EditText edittextID;
    private EditText edittextPassword;
    private EditText edittextPasswordCheck;
    private Button emailInterlock;
    private Button cancel;
    private TextView tvInterlock;

    private String name;
    private String id;
    private String password;
    private String passwordCheck;
    private String email;
    private String email_pw;
    private boolean boolInterlock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edittextName = findViewById(R.id.et_name);
        edittextID = findViewById(R.id.et_id_up);
        edittextPassword = findViewById(R.id.et_password_up);
        edittextPasswordCheck = findViewById(R.id.et_password_up_check);
        tvInterlock = findViewById(R.id.tv_interlock);
        cancel = findViewById(R.id.btn_signup_cancel);

        boolInterlock = false;

        //이메일 연동
        emailInterlock = findViewById(R.id.btn_email_add);
        emailInterlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, EmailInterlock.class);
                startActivityForResult(intent, 1);
            }
        });

        //회원가입 취소
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createUser() {
        NetworkHelper networkHelper = new NetworkHelper();
        ApiService apiService = networkHelper.getApiService();

        //서버에 POST 수행
        apiService.signup(name, id, password, passwordCheck, email, email, email_pw).enqueue(new Callback<ResponseSignin>() {
            @Override
            public void onResponse(Call<ResponseSignin> call, Response<ResponseSignin> response) {
                if(response.isSuccessful()){
                    ResponseSignin body = response.body();
                    if(body.getResult().equals("User Creation Success")){
                        // 회원가입 성공
                        Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // 회원가입 실패
                        Toast.makeText(SignUpActivity.this, body.getResult(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSignin> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Server 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void join(View view){
        name = edittextName.getText().toString();
        id = edittextID.getText().toString();
        password = edittextPassword.getText().toString();
        passwordCheck = edittextPasswordCheck.getText().toString();

        if(isValidID()){
            if(isValidPasswd()){
                if(password.equals(passwordCheck)){
                    if(boolInterlock){
                        createUser();
                    } else{
                        Toast.makeText(this, "이메일 연동이 필요합니다.",Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(this, "유효한 비밀번호가 아닙니다.",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "유효한 아이디가 아닙니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // 아이디 유효성 검사
    public boolean isValidID() {
        if (id.isEmpty()) {
            // 아이디 공백
            return false;
        } else if (!ID_PATTERN.matcher(id).matches()) {
            // 아이디 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    public boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                boolean result = data.getBooleanExtra("result", false);
                if(result){
                    email = data.getStringExtra("email");
                    email_pw = data.getStringExtra("pw");

                    tvInterlock.setText("이메일 연동이 완료되었습니다!");
                    boolInterlock = true;
                }
            }
        }
    }
}
