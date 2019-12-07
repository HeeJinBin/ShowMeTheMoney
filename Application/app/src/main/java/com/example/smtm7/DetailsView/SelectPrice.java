package com.example.smtm7.DetailsView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smtm7.R;
import java.text.DecimalFormat;

public class SelectPrice extends AppCompatActivity {

    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private EditText editTextPrice1;
    private EditText editTextPrice2;
    private String stringPrice1 = "";
    private String stringPrice2 = "";
    private Button cancelButton;
    private Button searchButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_price);

        cancelButton = findViewById(R.id.btn_price_cancel);
        searchButton = findViewById(R.id.btn_price_search);
        editTextPrice1 = findViewById(R.id.et_price_first);
        editTextPrice2 = findViewById(R.id.et_price_second);

        //취소
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("PriceCheck","False");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //검색
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!stringPrice1.equals("")&!stringPrice2.equals("")) {
                    String price1 = stringPrice1.replace(",", "");
                    String price2 = stringPrice2.replace(",", "");

                    if (Integer.parseInt(price1) <= Integer.parseInt(price2)) {
                        Intent intent = new Intent();
                        intent.putExtra("PriceCheck", "True");
                        intent.putExtra("price1", price1);
                        intent.putExtra("price2", price2);

                        setResult(RESULT_OK, intent);
                        finish();
                    } else{
                        Toast.makeText(SelectPrice.this,"올바른 가격 범위가 아닙니다.",Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(SelectPrice.this,"검색할 가격을 지정해주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });


        TextWatcher watcher1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(stringPrice1)){
                    stringPrice1 = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",","")));
                    editTextPrice1.setText(stringPrice1);
                    editTextPrice1.setSelection(stringPrice1.length());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        editTextPrice1.addTextChangedListener(watcher1);

        TextWatcher watcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(stringPrice2)){
                    stringPrice2 = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",","")));
                    editTextPrice2.setText(stringPrice2);
                    editTextPrice2.setSelection(stringPrice2.length());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        editTextPrice2.addTextChangedListener(watcher2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DetailsActivity.screenCheck = true;
    }
}
