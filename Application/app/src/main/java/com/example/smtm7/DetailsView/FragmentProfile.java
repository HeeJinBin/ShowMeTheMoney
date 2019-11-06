package com.example.smtm7.DetailsView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smtm7.DataBase.SharedPreferenceBase;
import com.example.smtm7.R;

public class FragmentProfile extends Fragment {

    private TextView username;
    private Button btnEmail;
    private Button btnWidget;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        username = view.findViewById(R.id.user_name);
        btnEmail = view.findViewById(R.id.btn_email_setting);
        btnWidget = view.findViewById(R.id.btn_widget_setting);

        SharedPreferenceBase sharedPreferenceBase = SharedPreferenceBase.getInstance(getContext());
        username.setText(sharedPreferenceBase.getString("nickname"));

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
