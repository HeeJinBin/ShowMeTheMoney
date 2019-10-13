package com.example.smtm7.DetailsView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

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
}
