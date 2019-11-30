package com.example.smtm7.DetailsView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smtm7.DataBase.SharedPreferenceBase;
import com.example.smtm7.R;
import com.kyleduo.switchbutton.SwitchButton;

public class FragmentProfile extends Fragment {

    private TextView username;
    private Button btnEmail;
    private Button btnPw;
    private SwitchButton switchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        username = view.findViewById(R.id.user_name);
        btnEmail = view.findViewById(R.id.btn_email_setting);
        btnPw = view.findViewById(R.id.btn_pw_setting);
        switchButton = view.findViewById(R.id.airbutton_switch);

        final SharedPreferenceBase sharedPreferenceBase = SharedPreferenceBase.getInstance(getContext());
        username.setText(sharedPreferenceBase.getString("nickname"));

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsActivity.screenCheck = false;
                Intent intent = new Intent(getActivity(), SettingEmail.class);
                startActivity(intent);
            }
        });

        btnPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsActivity.screenCheck = false;
                Intent intent = new Intent(getActivity(), SettingPw.class);
                startActivity(intent);
            }
        });

        if(sharedPreferenceBase.getString("airbutton").equals("True")){
            switchButton.setChecked(true);
        } else{
            switchButton.setChecked(false);
        }

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sharedPreferenceBase.setString("airbutton","True");
                    Log.d("airbutton check", sharedPreferenceBase.getString("airbutton"));
                } else{
                    sharedPreferenceBase.setString("airbutton","False");
                    Log.d("airbutton check", sharedPreferenceBase.getString("airbutton"));
                }
            }
        });

        return view;
    }
}
