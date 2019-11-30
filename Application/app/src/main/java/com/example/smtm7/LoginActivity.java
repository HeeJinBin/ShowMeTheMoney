package com.example.smtm7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smtm7.AirButton.FloatingActivity;
import com.example.smtm7.Connection.ApiService;
import com.example.smtm7.Connection.NetworkHelper;
import com.example.smtm7.Connection.ResponseLogin;
import com.example.smtm7.Connection.ResponseToken;
import com.example.smtm7.Connection.ResponseTransaction;
import com.example.smtm7.DataBase.DBEmailAdapter;
import com.example.smtm7.DataBase.DBTransactionAdapter;
import com.example.smtm7.DataBase.SharedPreferenceBase;
import com.example.smtm7.InitialSetting.EmailInterlock;

import java.util.List;
import java.util.regex.Pattern;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;

public class LoginActivity extends AppCompatActivity {
    //아이디 정규식
    private static final Pattern ID_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    //비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{6,20}$");

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

        //내부 DB clear
        DBTransactionAdapter transactionAdapter = new DBTransactionAdapter(this);
        transactionAdapter.open();
        transactionAdapter.deleteAllTransaction();
        transactionAdapter.close();

        DBEmailAdapter emailAdapter = new DBEmailAdapter(this);
        emailAdapter.open();
        emailAdapter.deleteAllEmail();
        emailAdapter.close();

        editTextID = findViewById(R.id.et_id);
        editTextPassword = findViewById(R.id.et_password);
        visibility = findViewById(R.id.visible_pw);

        visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count_visibility % 2 == 0) {
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
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

        if (isValidID() && isValidPasswd()) {
            loginUser();
        } else {
            Toast.makeText(this,"올바른 형식의 입력이 아닙니다.",Toast.LENGTH_SHORT).show();
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
    private void loginUser() {
        NetworkHelper networkHelper = new NetworkHelper();
        final ApiService apiService = networkHelper.getApiService();

        //서버에 POST 수행
        apiService.login(id, password).enqueue(new Callback<ResponseToken>() {
            @Override
            public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                if (response.isSuccessful()) {
                    final ResponseToken body = response.body();
                    final String accessToken = "Bearer " + body.getAccess();

                    final SharedPreferenceBase sharedPreferenceBase = SharedPreferenceBase.getInstance(getApplicationContext());
                    sharedPreferenceBase.setString("access", accessToken);
                    sharedPreferenceBase.setString("refresh", body.getRefresh());

                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    Log.d("access", accessToken);

                    //nickname 받아오기
                    apiService.getnickname(accessToken, id, password).enqueue(new Callback<ResponseLogin>() {
                        @Override
                        public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                            if (response.isSuccessful()) {
                                ResponseLogin bodyLogin = response.body();

                                if (bodyLogin.getResult().equals("Login Success")) {
                                    sharedPreferenceBase.setString("id", id);
                                    sharedPreferenceBase.setString("nickname", bodyLogin.getNickname());

                                    Intent intent = new Intent(LoginActivity.this, EmailInterlock.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseLogin> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Server 연결 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            //거래내역 데이터 받아오기
//                        apiService.getTransaction(id).enqueue(new Callback<List<ResponseTransaction>>() {
//                            @Override
//                            public void onResponse(Call<List<ResponseTransaction>> call, Response<List<ResponseTransaction>> response) {
//                                if(response.isSuccessful()){
//                                    List<ResponseTransaction> resource = response.body();
//
//                                    DBTransactionAdapter transactionAdapter = new DBTransactionAdapter(getApplicationContext());
//                                    transactionAdapter.open();
//
//                                    //내부 DB에 저장 (Transaction)
//                                    for(ResponseTransaction re : resource){
//                                        transactionAdapter.insertTransaction(re.getPGname(), re.getDate(), re.getPurchasing_office(), re.getPurchasing_item(), re.getPrice());
//                                        Log.d("GETTRANSACTION", re.getPGname()+" "+re.getDate()+" "+re.getPurchasing_office()+" "+re.getPurchasing_item()+" "+re.getPrice());
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<List<ResponseTransaction>> call, Throwable t) {
//                                Toast.makeText(LoginActivity.this, "거래내역 받아오기 실패", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                        Intent intent = new Intent(LoginActivity.this, FloatingActivity.class);
//                        startActivity(intent);
//                        finish();
            @Override
            public void onFailure(Call<ResponseToken> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Server 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
