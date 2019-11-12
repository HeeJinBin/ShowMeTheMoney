package com.example.smtm7.DetailsView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.smtm7.AirButton.FloatingActivity;
import com.example.smtm7.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DetailsActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentSearch fragmentSearch = new FragmentSearch();
    private FragmentFiltering fragmentFiltering = new FragmentFiltering();
    private FragmentProfile fragmentProfile = new FragmentProfile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        InnerReceiver innerReceiver = new InnerReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(innerReceiver, intentFilter);

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("TAG", "onReceive: Pressed Back");
        finish();
        Intent i = new Intent(getApplicationContext(), FloatingActivity.class);
        startActivity(i);
    }

    private class InnerReceiver extends BroadcastReceiver {
        final String SYSTEM_REASON_KEY = "reason";
        final String SYSTEM_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)){
                String reason = intent.getStringExtra(SYSTEM_REASON_KEY);
                if(reason!=null){
                    if(reason.equals(SYSTEM_HOME_KEY)){
                        Log.d("TAG", "onReceive: Pressed Home Key");
                        finish();
                        Intent i = new Intent(getApplicationContext(), FloatingActivity.class);
                        startActivity(i);
                    }
                }
            }
        }
    }
}
