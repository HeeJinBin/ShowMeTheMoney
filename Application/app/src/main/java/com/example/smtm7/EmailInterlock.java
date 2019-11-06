package com.example.smtm7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EmailInterlock extends AppCompatActivity {

    private String email;
    private String password;

    private Button emailInterlock_cancel;
    private Button emailInterlock_add;
    private EditText edittextEmail;
    private EditText edittextPw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_interlock);

        edittextEmail = findViewById(R.id.et_email);
        edittextPw = findViewById(R.id.et_pw);

        emailInterlock_add = findViewById(R.id.btn_email_interlock_add);
        emailInterlock_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edittextEmail.getText().toString();
                password = edittextPw.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("result", true);
                intent.putExtra("email",email);
                intent.putExtra("email",password);
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        emailInterlock_cancel = findViewById(R.id.btn_email_interlock_cancel);
        emailInterlock_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result", false);
                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }
}
