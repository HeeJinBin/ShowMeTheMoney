package com.example.smtm7.DetailsView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.smtm7.R;

public class FragmentFiltering extends Fragment {

    private Button selectPG;
    private Button selectDate;
    private Button selectPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_filtering,container,false);

        selectPG = (Button) v.findViewById(R.id.btn_filter_pg);
        selectDate = (Button) v.findViewById(R.id.btn_filter_date);
        selectPrice = (Button) v.findViewById(R.id.btn_filter_price);

        selectPG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectPG.class);
                startActivity(intent);
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectDate.class);
                startActivity(intent);
            }
        });

        selectPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectPrice.class);
                startActivity(intent);
            }
        });

        return v;
    }

//    private void refresh(){
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.detach(this).attach(this).commit();
//    }
}
