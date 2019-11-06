package com.example.smtm7.DetailsView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.smtm7.DataBase.DBTransactionAdapter;
import com.example.smtm7.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FragmentFiltering extends Fragment {

    private Button selectPG;
    private Button selectDate;
    private Button selectPrice;

    private ListView listView;
    private ArrayList<TransactionItem> itemList;
    private ListAdapter listAdapter;
    private DBTransactionAdapter transactionAdapter;

    private ArrayList<String> PGfilter;
    private String Datefilter;
    private int Pricefilter;

    private Context context;

    public static final int PG = 100; //PG 액티비티를 띄우기 위한 요청 코드
    public static final int DATE = 200; //DATE 액티비티를 띄우기 위한 요청 코드
    public static final int PRICE = 300; //PRICE 액티비티를 띄우기 위한 요청 코드
    public static final int RESULT_OK = -1; //request를 보내는 경우 요청 코드

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_filtering,container,false);

        context = v.getContext();

        selectPG = (Button) v.findViewById(R.id.btn_filter_pg);
        selectDate = (Button) v.findViewById(R.id.btn_filter_date);
        selectPrice = (Button) v.findViewById(R.id.btn_filter_price);

        listView = (ListView) v.findViewById(R.id.filter_listview);
        itemList = new ArrayList<>();
        transactionAdapter = new DBTransactionAdapter(getContext());

        //filter 초기화
        PGfilter = new ArrayList<>();
        Datefilter = null;
        Pricefilter = -1;

        selectPG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectPG.class);
                startActivityForResult(intent, PG);
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectDate.class);
                startActivityForResult(intent, DATE);
            }
        });

        selectPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectPrice.class);
                startActivityForResult(intent, PRICE);
            }
        });

        listAdapter = new ListAdapter(itemList);
        listView.setAdapter(listAdapter);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PG){
            if(resultCode == RESULT_OK){
                PGfilter.clear();
                int i = data.getIntExtra("number",-1);
                if(i != -1){
                    for(int j=0 ;j<i;j++){
                        PGfilter.add(data.getStringExtra(Integer.toString(j)));
                        Log.d("PGFilter", data.getStringExtra(Integer.toString(j)));
                    }
                }

                String textPG = "";
                //선택을 안한 경우
                if(PGfilter.size()==0){
                    selectPG.setText("PG사");
                    selectPG.setTextColor(Color.BLACK);
                    selectPG.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_filter_shape));
                } else{
                    selectPG.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_shape));
                    selectPG.setTextColor(ContextCompat.getColor(context, R.color.colorMain));

                    for(int x=0;x<PGfilter.size();x++){
                        textPG+=PGfilter.get(x);
                        if(x!=PGfilter.size()-1)
                            textPG+=",";
                    }

                    if(textPG.length()>10) {
                        textPG = textPG.substring(0,9);
                        textPG+="..";
                    }

                    selectPG.setText(textPG);
                }
                updateListView();
            }
        }
        else if(requestCode == DATE){
            if(resultCode == RESULT_OK){
                Datefilter = data.getStringExtra("Date");

                if(Datefilter != null){
                    selectDate.setText(Datefilter);
                    selectDate.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_shape));
                    selectDate.setTextColor(ContextCompat.getColor(context, R.color.colorMain));
                    Log.d("DateFilter", Datefilter);
                } else{ //선택을 안한 경우
                    selectDate.setText("날짜");
                    selectDate.setTextColor(Color.BLACK);
                    selectDate.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_filter_shape));
                }

                updateListView();
            }
        }
        else if(requestCode == PRICE){
            if(resultCode == RESULT_OK){
                Pricefilter = -1;
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                String price = data.getStringExtra("price");
                Log.d("PriceFilter", price);
                if(!price.equals(""))
                    Pricefilter = Integer.parseInt(price);

                if(Pricefilter != -1){
                    selectPrice.setText(decimalFormat.format(Double.parseDouble(Integer.toString(Pricefilter)))+"원");
                    selectPrice.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_shape));
                    selectPrice.setTextColor(ContextCompat.getColor(context, R.color.colorMain));
                } else{
                    selectPrice.setText("가격");
                    selectPrice.setTextColor(Color.BLACK);
                    selectPrice.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_filter_shape));
                }

                updateListView();
            }
        }
    }

    public void updateListView(){
        itemList.clear();
        if(PGfilter.size()!=0 || Datefilter!=null || Pricefilter!=-1) {
            transactionAdapter.open();
            Cursor cursor = transactionAdapter.searchTransaction(PGfilter, Datefilter, Pricefilter);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String office = "";
                if (!cursor.getString(3).equals("none")) {
                    office = cursor.getString(3);
                }

                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                String price = decimalFormat.format(Double.parseDouble(cursor.getString(5))) + "원";

                TransactionItem item = new TransactionItem(cursor.getString(1), cursor.getString(2), office, cursor.getString(4), price);

                Log.d("Date", cursor.getString(2));
                itemList.add(item);
                cursor.moveToNext();
            }
            transactionAdapter.close();
        }

        listAdapter = new ListAdapter(itemList);
        listView.setAdapter(listAdapter);
    }
}
