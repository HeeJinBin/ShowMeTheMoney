package com.example.smtm7.DetailsView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.smtm7.AirButton.FloatingActivity;
import com.example.smtm7.Connection.ApiService;
import com.example.smtm7.Connection.NetworkHelper;
import com.example.smtm7.Connection.ResponseUpdate;
import com.example.smtm7.DataBase.DBEmailAdapter;
import com.example.smtm7.DataBase.SharedPreferenceBase;
import com.example.smtm7.InitialSetting.EmailItem;
import com.example.smtm7.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentSearch fragmentSearch = new FragmentSearch();
    private FragmentFiltering fragmentFiltering = new FragmentFiltering();
    private FragmentProfile fragmentProfile = new FragmentProfile();
    private SharedPreferenceBase sharedPreferenceBase;

    public static ImageView loadingView;
    public static TextView loadingText;
    public static Button loadingBtn;

    public static boolean screenCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        screenCheck = true;
        sharedPreferenceBase = new SharedPreferenceBase(this);

        loadingBtn = findViewById(R.id.loading_btn);
        loadingText = findViewById(R.id.loading_text);
        loadingView = findViewById(R.id.loading_image);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragmentFiltering).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.tab_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (menuItem.getItemId()){
                    case R.id.menu_filter:
                        fragmentTransaction.replace(R.id.frame_layout,fragmentFiltering).commitAllowingStateLoss();
                        break;

                    case R.id.menu_search:
                        fragmentTransaction.replace(R.id.frame_layout,fragmentSearch).commitAllowingStateLoss();
                        break;

                    case R.id.menu_profile:
                        fragmentTransaction.replace(R.id.frame_layout, fragmentProfile).commitAllowingStateLoss();
                        break;
                }

                return true;
            }
        });

        loadingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTransaction(getApplicationContext());
            }
        });

        updateTransaction(getApplicationContext());
    }

    public static void updateTransaction(Context context){
        //update loading screen
        loadingText.setText("거래내역을 업데이트하고 있습니다.");
        loadingBtn.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        Glide.with(context).load(R.raw.loading).into(loadingView);

        NetworkHelper networkHelper = new NetworkHelper();
        final ApiService apiService = networkHelper.getApiService();

        final DBEmailAdapter emailAdapter = new DBEmailAdapter(context);
        final SharedPreferenceBase sharedPreferenceBase = new SharedPreferenceBase(context);
        final ArrayList<EmailItem> emailList = new ArrayList<>();

        emailAdapter.open();
        Cursor cursor = emailAdapter.searchAllEmail();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            EmailItem item = new EmailItem(cursor.getString(0),cursor.getString(1),cursor.getInt(2));
            emailList.add(item);
            Log.d("EMAIL", "email : "+item.getEmail());
            cursor.moveToNext();
        }
        emailAdapter.close();

        final int count = emailList.size();

        if(count > 0){
            String[] array = emailList.get(0).getEmail().split("@");

            apiService.updatelist(sharedPreferenceBase.getString("access"), sharedPreferenceBase.getString("id"), array[0], emailList.get(0).getPw(), emailList.get(0).getIndexs()).enqueue(new Callback<ResponseUpdate>() {
                @Override
                public void onResponse(Call<ResponseUpdate> call, Response<ResponseUpdate> response) {
                    if (response.isSuccessful()) {
                        ResponseUpdate body = response.body();

                        if (body.getMessage().equals("Save Success")) {
                            emailAdapter.open();
                            emailAdapter.updateEmail(emailList.get(0).getEmail(),emailList.get(0).getPw(),body.getIndex());
                            emailAdapter.close();
                            Log.d("Email", emailList.get(0).getEmail());

                            if(count > 1){
                                String[] array = emailList.get(1).getEmail().split("@");

                                apiService.updatelist(sharedPreferenceBase.getString("access"), sharedPreferenceBase.getString("id"), array[0], emailList.get(1).getPw(), emailList.get(1).getIndexs()).enqueue(new Callback<ResponseUpdate>() {
                                    @Override
                                    public void onResponse(Call<ResponseUpdate> call, Response<ResponseUpdate> response) {
                                        if (response.isSuccessful()) {
                                            ResponseUpdate body = response.body();

                                            if (body.getMessage().equals("Save Success")) {
                                                emailAdapter.open();
                                                emailAdapter.updateEmail(emailList.get(1).getEmail(),emailList.get(1).getPw(),body.getIndex());
                                                emailAdapter.close();
                                                Log.d("Email", emailList.get(1).getEmail());

                                                if(count>2){
                                                    String[] array = emailList.get(2).getEmail().split("@");

                                                    apiService.updatelist(sharedPreferenceBase.getString("access"), sharedPreferenceBase.getString("id"), array[0], emailList.get(2).getPw(), emailList.get(2).getIndexs()).enqueue(new Callback<ResponseUpdate>() {
                                                        @Override
                                                        public void onResponse(Call<ResponseUpdate> call, Response<ResponseUpdate> response) {
                                                            if (response.isSuccessful()) {
                                                                ResponseUpdate body = response.body();

                                                                if (body.getMessage().equals("Save Success")) {
                                                                    emailAdapter.open();
                                                                    emailAdapter.updateEmail(emailList.get(2).getEmail(),emailList.get(2).getPw(),body.getIndex());
                                                                    emailAdapter.close();
                                                                    Log.d("Email", emailList.get(2).getEmail());

                                                                    changeLoading();
                                                                }
                                                            }
                                                        }
                                                        @Override
                                                        public void onFailure(Call<ResponseUpdate> call, Throwable t) {
                                                            Log.d("Email", "onFailure: "+t.toString());
                                                        }
                                                    });
                                                } else{
                                                    changeLoading();
                                                }
                                            }
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ResponseUpdate> call, Throwable t) {
                                        Log.d("Email", "onFailure: "+t.toString());
                                    }
                                });
                            } else {
                                changeLoading();
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseUpdate> call, Throwable t) {
                    Log.d("Email", "onFailure: "+t.toString());
                }
            });
        } else{
            loadingText.setText("이메일을 연동해주세요.");
            loadingBtn.setVisibility(View.GONE);
            loadingView.setVisibility(View.GONE);
        }
    }

    public static void changeLoading(){
        loadingText.setText("거래내역이 업데이트 되었습니다.");
        loadingBtn.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (screenCheck & sharedPreferenceBase.getString("airbutton").equals("True")) {
            finish();
            Intent intent = new Intent(this, FloatingActivity.class);
            startActivity(intent);
        }
    }
}
