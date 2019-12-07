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
