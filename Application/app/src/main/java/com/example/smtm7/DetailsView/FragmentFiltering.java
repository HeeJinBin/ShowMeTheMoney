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
    private int FirstDate;
    private int SecondDate;
    private int FirstPrice;
    private int SecondPrice;

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
        FirstDate = SecondDate = FirstPrice = SecondPrice = -1;

        selectPG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsActivity.screenCheck = false;
                Intent intent = new Intent(context, SelectPG.class);
                startActivityForResult(intent, PG);
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsActivity.screenCheck = false;
                Intent intent = new Intent(context, SelectDate.class);
                startActivityForResult(intent, DATE);
            }
        });

        selectPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsActivity.screenCheck = false;
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

                    String text[] = textPG.split(",");

                    if(text.length>2) {
                        textPG = text[0]+","+text[1]+"..";
                    }

                    selectPG.setText(textPG);
                }
                updateListView();
            }
        }
        else if(requestCode == DATE){
            if(resultCode == RESULT_OK){
                FirstDate = SecondDate = -1;
                String result = data.getStringExtra("Check");

                if(result.equals("True")){
                    FirstDate = Integer.parseInt(data.getStringExtra("First").replace("-",""));
                    SecondDate = Integer.parseInt(data.getStringExtra("Second").replace("-",""));

                    selectDate.setText(data.getStringExtra("First").substring(2).replace("-",".")+"~"+data.getStringExtra("Second").substring(2).replace("-","."));
                    selectDate.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_shape));
                    selectDate.setTextColor(ContextCompat.getColor(context, R.color.colorMain));
                    Log.d("DateFilter", FirstDate+"~"+SecondDate);
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
                FirstPrice = SecondPrice = -1;
                String result = data.getStringExtra("PriceCheck");
                DecimalFormat decimalFormat = new DecimalFormat("#,###");

                if(result.equals("True")){
                    FirstPrice = Integer.parseInt(data.getStringExtra("price1"));
                    SecondPrice = Integer.parseInt(data.getStringExtra("price2"));

                    selectPrice.setText(decimalFormat.format(Double.parseDouble(Integer.toString(FirstPrice)))+"~"+decimalFormat.format(Double.parseDouble(Integer.toString(SecondPrice))));
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
        if(PGfilter.size()!=0 || FirstDate!=-1 || FirstPrice!=-1) {
            transactionAdapter.open();
            Cursor cursor = transactionAdapter.searchTransaction(PGfilter, FirstDate, SecondDate, FirstPrice, SecondPrice);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String office = "";
                if (!cursor.getString(3).equals("none")) {
                    office = cursor.getString(3);
                }

                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                String price = decimalFormat.format(Double.parseDouble(cursor.getString(5))) + "원";
                String date = cursor.getString(2).substring(0,4)+"-"+cursor.getString(2).substring(4,6)+"-"+cursor.getString(2).substring(6,8);

                TransactionItem item = new TransactionItem(cursor.getString(1), date, office, cursor.getString(4), price);

                if(!itemList.contains(item))
                    itemList.add(item);
                cursor.moveToNext();
            }
            transactionAdapter.close();
        }

        listAdapter = new ListAdapter(itemList);
        listView.setAdapter(listAdapter);
    }
}
