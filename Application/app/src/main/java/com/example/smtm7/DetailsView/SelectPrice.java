package com.example.smtm7.DetailsView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smtm7.R;
import java.text.DecimalFormat;

public class SelectPrice extends AppCompatActivity {

    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private EditText editTextPrice;
    private int price = -1;
    private String stringPrice = "";
    private Button cancelButton;
    private Button searchButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_price);

        cancelButton = findViewById(R.id.btn_price_cancel);
        searchButton = findViewById(R.id.btn_price_search);
        editTextPrice = findViewById(R.id.et_price);

        //취소
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //검색
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                if(!stringPrice.equals("")){
                    stringPrice = stringPrice.replaceAll(",","");
                    intent.putExtra("price", stringPrice);
                } else{
                    intent.putExtra("price","");
                }

                setResult(RESULT_OK, intent);
                finish();
            }
        });


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(stringPrice)){
                    stringPrice = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",","")));
                    editTextPrice.setText(stringPrice);
                    editTextPrice.setSelection(stringPrice.length());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        editTextPrice.addTextChangedListener(watcher);
    }
}
