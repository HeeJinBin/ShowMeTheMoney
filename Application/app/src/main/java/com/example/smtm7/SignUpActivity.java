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
    private static final Pattern ID_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    //비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{6,20}$");

    private EditText edittextName;
    private EditText edittextID;
    private EditText edittextPassword;
    private EditText edittextPasswordCheck;
    private Button cancel;

    private String name;
    private String id;
    private String password;
    private String passwordCheck;
    private String email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edittextName = findViewById(R.id.et_name);
        edittextID = findViewById(R.id.et_id_up);
        edittextPassword = findViewById(R.id.et_password_up);
        edittextPasswordCheck = findViewById(R.id.et_password_up_check);
        cancel = findViewById(R.id.btn_signup_cancel);

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
        apiService.signup(name, id, password, passwordCheck, email).enqueue(new Callback<ResponseSignin>() {
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
        email = id;
        password = edittextPassword.getText().toString();
        passwordCheck = edittextPasswordCheck.getText().toString();

        if(isValidID()){
            if(isValidPasswd()){
                if(password.equals(passwordCheck)){
                    createUser();
                } else{
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(this, "유효한 비밀번호가 아닙니다.",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "유효한 아이디 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
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
}
