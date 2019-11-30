package com.example.smtm7.InitialSetting;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.smtm7.AirButton.FloatingActivity;
import com.example.smtm7.Certification;
import com.example.smtm7.Connection.ApiService;
import com.example.smtm7.Connection.NetworkHelper;
import com.example.smtm7.Connection.ResponseTransaction;
import com.example.smtm7.Connection.ResponseUpdate;
import com.example.smtm7.DataBase.DBEmailAdapter;
import com.example.smtm7.DataBase.DBTransactionAdapter;
import com.example.smtm7.DataBase.SharedPreferenceBase;
import com.example.smtm7.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailInterlock extends AppCompatActivity {

    private Button emailInterlock_add;
    private ImageButton email_add;

    private int count;
    public static ListView listView;
    public static EmailListAdapter listAdapter;
    public static ArrayList<EmailItem> itemList;
    private DBEmailAdapter emailAdapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_interlock);

        count = 0;
        listView = findViewById(R.id.email_list);
        itemList = new ArrayList<>();
        listAdapter = new EmailListAdapter(itemList);
        listView.setAdapter(listAdapter);

        emailAdapter = new DBEmailAdapter(this);
        emailAdapter.open();
        emailAdapter.deleteAllEmail();
        emailAdapter.close();

        email_add = findViewById(R.id.btn_email_plus);
        email_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count<3) {
                    EmailDialog dialog = new EmailDialog(EmailInterlock.this);
                    dialog.callFunction();
                    dialog.setDialogResult(new EmailDialog.OnMyDialogResult() {
                        @Override
                        public void finish(boolean result) {
                            // 이메일 연동을 한 경우
                            if (result) {
                                updateList();
                                count++;
                                if(count==3){
                                    email_add.setClickable(false);
                                    email_add.setSelected(false);
                                    email_add.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.plus_button_click));
                                }
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "이메일은 최대 3개까지 연동 가능합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        emailInterlock_add = findViewById(R.id.btn_email_interlock_add);
        emailInterlock_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemList.size()==0){
                    Toast.makeText(getApplicationContext(),"이메일을 1개 이상 연동해야 합니다.",Toast.LENGTH_SHORT).show();
                } else {
                    //거래내역 업데이트 하라고 요청
                    NetworkHelper networkHelper = new NetworkHelper();
                    final ApiService apiService = networkHelper.getApiService();

                    final SharedPreferenceBase sharedPreferenceBase = new SharedPreferenceBase(getApplicationContext());
                    for(int i=0;i<itemList.size();i++){
                        final int finalI = i;
                        final String[] array = itemList.get(i).getEmail().split("@");

                        apiService.updatelist(sharedPreferenceBase.getString("access"), sharedPreferenceBase.getString("id"), array[i], itemList.get(0).getPw(), itemList.get(0).getIndexs()).enqueue(new Callback<ResponseUpdate>() {
                            @Override
                            public void onResponse(Call<ResponseUpdate> call, Response<ResponseUpdate> response) {
                                if (response.isSuccessful()) {
                                    ResponseUpdate body = response.body();

                                    if (body.getMessage().equals("Save Success")) {
                                        emailAdapter.open();
                                        emailAdapter.updateEmail(itemList.get(finalI).getEmail(),itemList.get(finalI).getPw(),body.getIndex());
                                        emailAdapter.close();
                                        Log.d("Email", itemList.get(finalI).getEmail());
                                        Toast.makeText(getApplicationContext(),"Email update complete "+ body.getIndex(), Toast.LENGTH_LONG).show();
                                    }
                                } else{
                                    Toast.makeText(EmailInterlock.this, "Response 못 받음", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseUpdate> call, Throwable t) {
                                Toast.makeText(EmailInterlock.this, "Server 연결 실패", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    DBTransactionAdapter transactionAdapter = new DBTransactionAdapter(getApplicationContext());
                    transactionAdapter.open();
                    transactionAdapter.deleteAllTransaction();
                    transactionAdapter.close();

                    sharedPreferenceBase.setString("transactionIndex",Integer.toString(0));

                    Intent intent = new Intent(EmailInterlock.this, CertificationSetting.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void updateList(){
        itemList.clear();
        emailAdapter.open();
        Cursor cursor = emailAdapter.searchAllEmail();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            EmailItem item = new EmailItem(cursor.getString(0),cursor.getString(1),cursor.getInt(2));
            itemList.add(item);
            cursor.moveToNext();
        }
        emailAdapter.close();

        listAdapter = new EmailListAdapter(itemList);
        listView.setAdapter(listAdapter);
    }
}
