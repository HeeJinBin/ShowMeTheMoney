package com.example.smtm7;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    //아이디 정규식
    private static final Pattern ID_PATTERN = Pattern.compile("^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$");
    //비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{6,20}$");

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    private EditText edittextName;
    private EditText edittextID;
    private EditText edittextPassword;
    private EditText edittextPasswordCheck;
    private Button emailInterlock;
    private Button IDCheck;

    private String name;
    private String id;
    private String password;
    private String passwordCheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firebaseAuth = FirebaseAuth.getInstance();

        edittextName = findViewById(R.id.et_name);
        edittextID = findViewById(R.id.et_id_up);
        edittextPassword = findViewById(R.id.et_password_up);
        edittextPasswordCheck = findViewById(R.id.et_password_up_check);

        //이메일 중복체크
        IDCheck = findViewById(R.id.btn_id_check);
        IDCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //서버에 아이디 중복되는지 여부 물어보고 dialog 띄워주기
            }
        });

        //이메일 연동
        emailInterlock = findViewById(R.id.btn_email_add);
        emailInterlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void createUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            Toast.makeText(SignUpActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();

                            //내부 DB에 저장!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                            //서버에 보내기

                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // 회원가입 실패
                            Toast.makeText(SignUpActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                        }
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
                    createUser(id,password);
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
}
