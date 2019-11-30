package com.example.smtm7.InitialSetting;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smtm7.Connection.ApiService;
import com.example.smtm7.Connection.NetworkHelper;
import com.example.smtm7.Connection.ResponseCipher;
import com.example.smtm7.Connection.ResponseSignin;
import com.example.smtm7.DataBase.DBEmailAdapter;
import com.example.smtm7.DataBase.Encryption;
import com.example.smtm7.DataBase.KeyGeneration;
import com.example.smtm7.DataBase.SharedPreferenceBase;
import com.example.smtm7.R;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailDialog{
    private Context context;
    private DBEmailAdapter emailAdapter;
    OnMyDialogResult dialogResult;
    private String email;
    private String pw;

    //아이디 정규식
    private static final Pattern ID_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public EmailDialog(Context context){
        this.context = context;
    }

    public void callFunction(){
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.email_dialog);

        dialog.show();

        final EditText et_email = dialog.findViewById(R.id.et_email);
        final EditText et_pw = dialog.findViewById(R.id.et_email_pw);
        final Button btn_add = dialog.findViewById(R.id.email_add);
        final Button btn_cancel = dialog.findViewById(R.id.email_cancel);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_email.getText().toString();
                pw = et_pw.getText().toString();

                if(isValidID(email)){
                    NetworkHelper networkHelper = new NetworkHelper();
                    final ApiService apiService = networkHelper.getApiService();

                    SharedPreferenceBase sharedPreferenceBase = new SharedPreferenceBase(context);
                    final String[] array = email.split("@");

                    apiService.getemailinterlock(sharedPreferenceBase.getString("access"), array[0], pw).enqueue(new Callback<ResponseSignin>() {
                        @Override
                        public void onResponse(Call<ResponseSignin> call, Response<ResponseSignin> response) {
                            if(response.isSuccessful()){
                                ResponseSignin body = response.body();
                                if(body.getResult().equals("Connection Success")){
                                    //DB에 저장
                                    emailAdapter = new DBEmailAdapter(context);
                                    emailAdapter.open();
                                    emailAdapter.insertEmail(email, pw,0);
                                    emailAdapter.close();

                                    if(dialogResult != null){
                                        dialogResult.finish(true);
                                    }
                                    dialog.dismiss();

                                }
                                if(body.getResult().equals("Connection Error")){
                                    Toast.makeText(context,"유효한 이메일 정보가 아닙니다.\n다시 입력해주세요!",Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseSignin> call, Throwable t) {

                        }
                    });


//                    // 암호화 Test
//                    try {
//                        KeyGeneration keyGeneration = new KeyGeneration(sharedPreferenceBase.getString("access"));
//                        final String password = Encryption.Encode(keyGeneration.getKey(),pw,sharedPreferenceBase.getString("access"));
//
//                        Log.d("TAG", "PW : "+password+" length : "+password.length());
//                        apiService.getCipher(sharedPreferenceBase.getString("access"),password,keyGeneration.getKey()).enqueue(new Callback<ResponseCipher>() {
//                            @Override
//                            public void onResponse(Call<ResponseCipher> call, Response<ResponseCipher> response) {
//                                if(response.isSuccessful()){
//                                    ResponseCipher body = response.body();
//                                    Toast.makeText(context, "응답 성공", Toast.LENGTH_SHORT).show();
//                                    if(body.getResult().equals("jwt_access")){
//                                        Log.d("TAG", "onResponse: "+body.getKey());
//                                        Log.d("TAG", "onResponse: "+body.getAes_encrypted_pw());
//                                        Log.d("TAG", "onResponse: "+body.getAes_dscrypted_pw());
//                                    }
//                                } else {
//                                    Toast.makeText(context, "응답 실패", Toast.LENGTH_SHORT).show();
//                                    Log.d("TAG", "onFailure Response");
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<ResponseCipher> call, Throwable t) {
//                                Toast.makeText(context, "연결 실패", Toast.LENGTH_SHORT).show();
//                                Log.d("TAG", "onFailure Connection");
//                            }
//                        });
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    } catch (NoSuchPaddingException e) {
//                        e.printStackTrace();
//                    } catch (NoSuchAlgorithmException e) {
//                        e.printStackTrace();
//                    } catch (InvalidAlgorithmParameterException e) {
//                        e.printStackTrace();
//                    } catch (InvalidKeyException e) {
//                        e.printStackTrace();
//                    } catch (BadPaddingException e) {
//                        e.printStackTrace();
//                    } catch (IllegalBlockSizeException e) {
//                        e.printStackTrace();
//                    }

//                    try {
//                        //비밀번호 암호화
//                        KeyGeneration keyGeneration = new KeyGeneration(sharedPreferenceBase.getString("access"));
//                        final String password = Encryption.Encode(keyGeneration.getKey(),pw,sharedPreferenceBase.getString("access"));
//
//                        apiService.getemailinterlock(sharedPreferenceBase.getString("access"), array[0], password).enqueue(new Callback<ResponseSignin>() {
//                            @Override
//                            public void onResponse(Call<ResponseSignin> call, Response<ResponseSignin> response) {
//                                if(response.isSuccessful()){
//                                    ResponseSignin body = response.body();
//                                    if(body.getResult().equals("Connection Success")){
//                                        //DB에 저장
//                                        emailAdapter = new DBEmailAdapter(context);
//                                        emailAdapter.open();
//                                        emailAdapter.insertEmail(email, password,0);
//                                        emailAdapter.close();
//
//                                        if(dialogResult != null){
//                                            dialogResult.finish(true);
//                                        }
//                                        dialog.dismiss();
//
//                                    }
//                                    if(body.getResult().equals("Connection Error")){
//                                        Toast.makeText(context,"유효한 이메일 정보가 아닙니다.\n다시 입력해주세요!",Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<ResponseSignin> call, Throwable t) {
//
//                            }
//                        });
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    } catch (NoSuchPaddingException e) {
//                        e.printStackTrace();
//                    } catch (NoSuchAlgorithmException e) {
//                        e.printStackTrace();
//                    } catch (InvalidAlgorithmParameterException e) {
//                        e.printStackTrace();
//                    } catch (InvalidKeyException e) {
//                        e.printStackTrace();
//                    } catch (BadPaddingException e) {
//                        e.printStackTrace();
//                    } catch (IllegalBlockSizeException e) {
//                        e.printStackTrace();
//                    }
                } else {
                    Toast.makeText(context,"유효한 이메일 형식이 아닙니다.",Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogResult != null){
                    dialogResult.finish(false);
                }
                dialog.dismiss();
            }
        });
    }

    // 아이디 유효성 검사
    public boolean isValidID(String id) {
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

    public void setDialogResult(OnMyDialogResult dialogResult){
        this.dialogResult = dialogResult;
    }

    public interface OnMyDialogResult{
        void finish(boolean result);
    }
}
