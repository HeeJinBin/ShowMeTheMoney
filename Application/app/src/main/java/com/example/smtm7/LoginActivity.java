package com.example.smtm7;

import android.content.Intent;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

import com.example.smtm7.AirButton.FloatingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    //아이디 정규식
    private static final Pattern ID_PATTERN = Pattern.compile("^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$");
    //비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{6,16}$");

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 아이디과 비밀번호
    private EditText editTextID;
    private EditText editTextPassword;
    private Button visibility;
    private int count_visibility = 0;

    private String id = "";
    private String password = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextID = findViewById(R.id.et_id);
        editTextPassword = findViewById(R.id.et_password);
        visibility = findViewById(R.id.visible_pw);

        visibility.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(count_visibility%2==0){
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else{
                    editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                count_visibility++;
            }
        });
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    public void signIn(View view) {
        id = editTextID.getText().toString();
        password = editTextPassword.getText().toString();

        if(isValidID() && isValidPasswd()) {
            loginUser(id, password);

            Intent intent = new Intent(LoginActivity.this, FloatingActivity.class);
            startActivity(intent);
            finish();
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

    // 로그인
    private void loginUser(String email, String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(LoginActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                        } else {
                            // 로그인 실패
                            Toast.makeText(LoginActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
