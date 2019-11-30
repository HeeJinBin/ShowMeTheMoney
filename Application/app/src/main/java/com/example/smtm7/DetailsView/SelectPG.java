package com.example.smtm7.DetailsView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smtm7.R;

public class SelectPG extends AppCompatActivity {

    private Button cancelButton;
    private Button searchButton;
    private CheckBox checkBoxkg;
    private CheckBox checkBoxnhn;
    private CheckBox checkBoxnice;
    private CheckBox checkBoxlg;
    private CheckBox checkBoxnaver;
    private CheckBox checkBoxpayco;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pg);

        cancelButton = findViewById(R.id.btn_pg_cancel);
        searchButton = findViewById(R.id.btn_pg_search);

        checkBoxkg = findViewById(R.id.checkbox_kg);
        checkBoxnhn = findViewById(R.id.checkbox_nhn);
        checkBoxnice = findViewById(R.id.checkbox_nice);
        checkBoxlg = findViewById(R.id.checkbox_lg);
        checkBoxnaver = findViewById(R.id.checkbox_naver);
        checkBoxpayco = findViewById(R.id.checkbox_payco);

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
                //체크박스 결과값 저장한후 검색탭으로 넘겨주기
                int i=0;
                if (checkBoxkg.isChecked()){
                    intent.putExtra(Integer.toString(i), "KG이니시스");
                    i++;
                }
                if(checkBoxnhn.isChecked()){
                    intent.putExtra(Integer.toString(i), "NHN KCP");
                    i++;
                }
                if(checkBoxnice.isChecked()){
                    intent.putExtra(Integer.toString(i), "나이스페이");
                    i++;
                }
                if(checkBoxlg.isChecked()){
                    intent.putExtra(Integer.toString(i), "LGU+");
                    i++;
                }
                if(checkBoxnaver.isChecked()){
                    intent.putExtra(Integer.toString(i), "네이버페이");
                    i++;
                }
                if(checkBoxpayco.isChecked()){
                    intent.putExtra(Integer.toString(i), "PAYCO");
                    i++;
                }
                intent.putExtra("number", i);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        DetailsActivity.screenCheck = true;
    }
}
