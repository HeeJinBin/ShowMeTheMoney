package com.example.smtm7.AirButton;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smtm7.DataBase.DBTransactionAdapter;
import com.example.smtm7.DetailsView.DetailsActivity;
import com.example.smtm7.DetailsView.ListAdapter;
import com.example.smtm7.DetailsView.TransactionItem;
import com.example.smtm7.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BottomSheetActivity extends AppCompatActivity {

    private LinearLayout bottomsheetLayout;

    private TextView textPG;
    private TextView textDate;
    private TextView textPrice;

    private String pg = "";
    private String date = "";
    private int price = -1;

    private Button moveButton;
    private ListView listView;
    private ArrayList<TransactionItem> itemList;
    private ListAdapter listAdapter;
    private DBTransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottomsheet);

        Intent intent = getIntent();

        bottomsheetLayout = findViewById(R.id.bottomsheet);

        textPG = findViewById(R.id.text_pg);
        textDate = findViewById(R.id.text_date);
        textPrice = findViewById(R.id.text_price);

        moveButton = findViewById(R.id.btn_move);

        if(!intent.getStringExtra("pg").equals("null")) {
            textPG.setText("PG사: " + intent.getStringExtra("pg"));
            pg = intent.getStringExtra("pg");
        }

        String str = intent.getStringExtra("date");
        if(!intent.getStringExtra("date").equals("null")) {
            String array[] = str.split("\\.");
            if(array[0].equals("0")){
                textDate.setText("날짜: " + array[1]+"."+array[2]);
                date=array[1]+array[2];
            } else if(array[0].length()==2) {
                textDate.setText("날짜: " + intent.getStringExtra("date"));
                date="20"+array[0]+array[1]+array[2];
            } else{
                textDate.setText("날짜: " + intent.getStringExtra("date"));
                date = array[0]+array[1]+array[2];
            }
        }

        if(!intent.getStringExtra("price").equals("null")) {
            textPrice.setText("가격: " + intent.getStringExtra("price") + "원");
            price = Integer.parseInt(intent.getStringExtra("price"));
        }
        else
            textPrice.setText("가격: ");

        listView = findViewById(R.id.bottomsheet_list);
        itemList = new ArrayList<>();
        transactionAdapter = new DBTransactionAdapter(this);
        transactionAdapter.open();

        Cursor cursor = transactionAdapter.searchhBottomSheetResult(pg,date,price);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String office = "";
            if (!cursor.getString(3).equals("none")) {
                office = cursor.getString(3);
            }

            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String priceString = decimalFormat.format(Double.parseDouble(cursor.getString(5))) + "원";
            String dateString = cursor.getString(2).substring(0,4)+"-"+cursor.getString(2).substring(4,6)+"-"+cursor.getString(2).substring(6,8);

            TransactionItem item = new TransactionItem(cursor.getString(1), dateString, office, cursor.getString(4), priceString);
            itemList.add(item);
            cursor.moveToNext();
        }
        transactionAdapter.close();

        listAdapter = new ListAdapter(itemList);
        listView.setAdapter(listAdapter);

        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomsheetLayout);
        behavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56.f, getResources().getDisplayMetrics()));
        behavior.setHideable(true);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        finish();
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        //btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service = new Intent(getApplicationContext(), FloatingService.class);
                stopService(service);

                Intent intent1 = new Intent(getApplicationContext(), DetailsActivity.class);
                startActivity(intent1);
                finish();
            }
        });
    }
}
